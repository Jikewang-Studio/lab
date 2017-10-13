package lab.teacher.service;

import lab.bean.Course;
import lab.util.DbUtil;
import lab.util.FileUtil;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CourseService {

    /**
     * 更新老师课程备注
     *
     * @param cos
     * @return
     */
    public boolean updateCourseById(Course cos) {
        System.out.println(cos);
        Connection conn = null;
        PreparedStatement ps = null;
        boolean result = false;
        try {
            String sql = "UPDATE  `teachercourse` SET `remark` = ? WHERE `id` = ?";
            conn = DbUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1,cos.getRemark() );
            ps.setInt(2, cos.getId());
            if (ps.executeUpdate() > 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.free(ps, conn);
        }
        return result;
    }

    /**
     * 教师根据课程id添加自己的实验课程
     *
     * @param cos
     * @return
     */
    public boolean addCourseByCourseId(Course cos) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        try {
//            // 查询老师选择这门课的数量
//            String sql = "SELECT count(*) as rowCount FROM teacherCourse where courseId=? and teacherId=?";
            conn = DbUtil.getConnection();
//            ps = conn.prepareStatement(sql);
//            ps.setInt(1, cos.getCourseId());
//            ps.setString(2, cos.getUserId());
//            rs = ps.executeQuery();
//            rs.next();
            String sql = "INSERT INTO teacherCourse(courseId,teacherId,addTime) VALUES (?,?,curdate())";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cos.getCourseId());
            ps.setString(2, cos.getUserId());
            if (ps.executeUpdate() > 0) {
                result = true;
            }

//            if (rs.getInt("rowCount") == 0) {
//                System.out.println("第一次添加课程");
//                sql = "INSERT INTO teacherCourse(courseId,teacherId,addTime) VALUES (?,?,curdate())";
//                ps = conn.prepareStatement(sql);
//                ps.setInt(1, cos.getCourseId());
//                ps.setString(2, cos.getUserId());
//                if (ps.executeUpdate() > 0) {
//                    result = true;
//                }
//            } else if (rs.getInt("rowCount") == 1) { //如果存在，说明是老师第二次添加课程，需要输入两次的班级号
//                System.out.println("第二次添加课程");
//                //更新第一次选择的课程的班级号
//                sql = "UPDATE `teachercourse` SET `classNumber`=? WHERE courseId = ? AND teacherId = ?";
//                ps = conn.prepareStatement(sql);
////                ps.setString(1, "1");
////                String str = "1,2:3";
//                String[] classNumbers= cos.getClassNumber().split(":");
//                System.out.println(Arrays.asList(classNumbers));
//                ps.setString(1, classNumbers[0]);
//                ps.setInt(2, cos.getCourseId());
//                ps.setString(3, cos.getUserId());
//                Boolean result2 = false;
//                if (ps.executeUpdate() > 0) {
//                    result2 = true;
//                }
//                // 一次性插入第二次的课程和班级号
//                sql = "INSERT INTO teacherCourse(courseId,teacherId,addTime,classNumber) VALUES (?,?,curdate(),?)";
//                ps = conn.prepareStatement(sql);
//                ps.setInt(1, cos.getCourseId());
//                ps.setString(2, cos.getUserId());
////                ps.setString(3, "2");
//                ps.setString(3, classNumbers[1]);
////                        ps.setString(3, cos.getClassNumber().get(1));
//                // 两次数据库操作都成功，只是不可以使用事务
//                if (ps.executeUpdate() > 0) {
//                    return result2;
//                }
//            } else if (rs.getInt("rowCount") == 2) { // 只允许添加两次同样的课程
//                System.out.println("不允许第三次添加课程");
//                return result;
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.free(ps, conn);
        }
        return result;
    }

    /**
     * 教师删除自己添加的课程（目录结构/lab/courseId/teacherId/实验次数）教师删除是只要删除teacherId下的文件和teacherId目录
     *
     * @param cos
     * @return
     */
    public boolean deleteCourseByTeacherCourseId(Course cos) {
        boolean b = false;
        int[] n = new int[5];
        Connection conn = null;
        PreparedStatement ps = null;
        //删除任务(连指导书一起删除)
        String sqlDelTask = "delete from task where teacherCourseId=" + cos.getTeacherCourseId();
        //删除教师课程
        String sqlDelTeacherCourse = "delete from teacherCourse where id=" + cos.getTeacherCourseId();
        //删除学生作业（连作业一起删除）
        String sqlDelWork = "delete from work where taskId in (select id from task where teacherCourseId=" + cos.getTeacherCourseId() + ")";

        //删除学生课程
        String sqlDelStudentCourse = "delete from studentcourse where teachercourseId=" + cos.getTeacherCourseId();

        try {
            conn = DbUtil.getConnection();
            //删除学生作业（数据库）
            ps = conn.prepareStatement(sqlDelWork);
            n[0] = ps.executeUpdate();
            //删除学生课程
            ps = conn.prepareStatement(sqlDelStudentCourse);
            n[1] = ps.executeUpdate();
            //删除教师课程对应的实验任务
            ps = conn.prepareStatement(sqlDelTask);
            n[2] = ps.executeUpdate();
            //删除教师课程
            ps = conn.prepareStatement(sqlDelTeacherCourse);
            n[3] = ps.executeUpdate();


            if (n[0] >= 0 && n[1] >= 0 && n[2] >= 0 && n[3] > 0) {
                //删除学生作业(文件)
                File dir = new File("E:\\lab\\" + cos.getCourseId() + "\\" + cos.getUserId());
                FileUtil.deleteDir(dir);
                b = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.free(ps, conn);
        }
        return b;
    }
}
