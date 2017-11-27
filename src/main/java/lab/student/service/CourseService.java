package lab.student.service;

import lab.bean.Course;
import lab.util.DbUtil;
import lab.util.FileUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseService {

    /**
     * 学生添加教师发布的实验课程
     *
     * @param cos
     * @return
     */
    public boolean addCourse(Course cos) {
        boolean b = false;
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "SELECT DISTINCT courseNumber FROM studentcourse,teachercourse,course WHERE studentcourse.teacherCourseId = teachercourse.id " +
                "AND course.id = teachercourse.courseId AND studentcourse.studentId = ?";
        try {
            conn = DbUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, cos.getUserId());
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("courseNumber").equals(cos.getCourseNumber())) {
                    return b;
                }
            }
            sql = "INSERT IGNORE INTO studentCourse(teacherCourseId,studentId,addTime) VALUE(?,?,CURDATE())";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cos.getTeacherCourseId());
            ps.setString(2, cos.getUserId());
            if (ps.executeUpdate() > 0) {
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
     * 学生删除自己添加的实验课程
     *
     * @param cos
     * @return
     */
    public boolean deleteCourse(Course cos) {
        boolean b = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sqlDelstudentCourse = "delete from studentCourse where id=" + cos.getStudentCourseId();
        String sqlWork = "select url from work where studentCourseId=" + cos.getStudentCourseId();
        String sqlDelWork = "delete from work where studentCourseId=" + cos.getStudentCourseId();

        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            //查出学生作业
            //查出学生作业先将文件删除再将作业表记录删除，再将课程表记录删除
            ps = conn.prepareStatement(sqlWork);
            rs = ps.executeQuery();
            while (rs.next()) {
                FileUtil.deleteFile(rs.getString("url"));
            }
            //删除学生作业
            ps.executeUpdate(sqlDelWork);
            //删除学生课程
            ps.executeUpdate(sqlDelstudentCourse);
            conn.commit();
            conn.setAutoCommit(true);
            b = true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            DbUtil.free(rs, ps, conn);
        }
        return b;
    }
}
