package lab.teacher.service;

import lab.bean.Course;
import lab.bean.User;
import lab.util.DbUtil;
import lab.util.FileUtil;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class CourseService {


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
        try {
            conn = DbUtil.getConnection();
            String sql = "INSERT INTO teacherCourse(courseId,teacherId,addTime) VALUES (?,?,curdate())";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cos.getCourseId());
            ps.setString(2, cos.getUserId());
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
     * 教师删除自己添加的课程
     * （目录结构/lab/courseId/teacherId/实验次数）教师删除是只要删除teacherId下的文件和teacherId目录
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


    /**
     * 更新老师课程备注
     *
     * @param cos
     * @return
     */
    public boolean updateCourseById(Course cos) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean result = false;
        try {
            String sql = "UPDATE  `teachercourse` SET `remark` = ? WHERE `id` = ?";
            conn = DbUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, cos.getRemark());
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
     * 查看选这门课程的学生名单
     *
     * @param cos
     * @return
     */
    public Map<String, Object> getStudentsById(Course cos) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<User> users = new LinkedList<User>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        // 当前页
        int intPage = cos.getPage() == 0 ? 1 : cos.getPage();
        // 每页显示条数
        int rows = cos.getRows() == 0 ? 10 : cos.getRows();
        // 每页的开始记录
        int start = (intPage - 1) * rows;

        try {
            String sql = "SELECT COUNT(1) AS total FROM studentcourse,`user` WHERE studentcourse.studentId = `user`.userId " +
                    "AND studentcourse.teacherCourseId = ?";
            conn = DbUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cos.getTeacherCourseId());
            rs = ps.executeQuery();
            rs.next();
            map.put("total", rs.getInt("total"));
            sql = "SELECT `user`.userId,`user`.userName,`user`.cls,`user`.grade FROM studentcourse,`user` WHERE " +
                    "studentcourse.studentId = `user`.userId " +
                    "AND studentcourse.teacherCourseId = ? ";
            if (cos.getSort() == null) {
                cos.setSort("userId");
            }
            if (cos.getOrder() == null) {
                cos.setOrder("ASC");
            }
            sql += " ORDER BY " + cos.getSort() + " " + cos.getOrder() + " limit ?,?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cos.getTeacherCourseId());
            ps.setInt(2, start);
            ps.setInt(3, rows);
            rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getString("userId"));
                user.setUserName(rs.getString("userName"));
                user.setCls(rs.getString("cls"));
                user.setGrade(rs.getString("grade"));
                users.add(user);
            }
            map.put("rows", users);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.free(ps, conn);
        }
        return map;
    }
}
