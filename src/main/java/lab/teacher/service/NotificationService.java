package lab.teacher.service;

import lab.bean.Notification;
import lab.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author 李文浩
 * @version 2017/11/27.
 */
public class NotificationService {
    public boolean pushNotification(Notification notification) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean result = false;
        try {
            String sql = "INSERT INTO `notification` (`teacherId`, `studentId`, `content`, `createTime`) " +
                    "VALUES (?, ?, ?, NOW())";
            conn = DbUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, notification.getTeacherId());
            ps.setString(2, notification.getStudentId());
            ps.setString(3, notification.getContent());
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

    public boolean pushNotificationBatch(Notification notification, String[] studentIds) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean result = false;
        try {
            String sql = "INSERT INTO `notification` (`teacherId`, `studentId`, `content`, `createTime`) " +
                    "VALUES (?, ?, ?, NOW())";
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            for (String studentId : studentIds) {
                ps.setString(1, notification.getTeacherId());
                ps.setString(2, studentId);
                ps.setString(3, notification.getContent());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.free(ps, conn);
        }
        return result;
    }

}
