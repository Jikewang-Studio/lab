package lab.student.service;

import lab.bean.Notification;
import lab.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 李文浩
 * @version 2017/11/27.
 */
public class NotificationService {

    public List<Notification> getUnreadnotifications(String userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Notification> notifications = new ArrayList<Notification>();

        try {
            String sql = "SELECT id,teacherId, studentId, content, isRead,createTime FROM notification " +
                    "WHERE studentId = ? AND isRead = 0";
            conn = DbUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Notification notification = new Notification();
                notification.setId(rs.getInt("id"));
                notification.setTeacherId(rs.getString("teacherId"));
                notification.setStudentId(rs.getString("studentId"));
                notification.setContent(rs.getString("content"));
                notification.setIsRead(rs.getInt("isRead"));
                notification.setCreateTime(rs.getDate("createTime"));
                notifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.free(ps, conn);
        }
        return notifications;
    }

    public List<Notification> getALLNotifications(String userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Notification> notifications = new ArrayList<Notification>();

        try {
            String sql = "SELECT id,teacherId, studentId, content, isRead,createTime FROM notification " +
                    "WHERE studentId = ?";
            conn = DbUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Notification notification = new Notification();
                notification.setId(rs.getInt("id"));
                notification.setTeacherId(rs.getString("teacherId"));
                notification.setStudentId(rs.getString("studentId"));
                notification.setContent(rs.getString("content"));
                notification.setIsRead(rs.getInt("isRead"));
                notification.setCreateTime(rs.getDate("createTime"));
                notifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.free(ps, conn);
        }
        return notifications;
    }

    public boolean readNotification(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean result = false;
        try {
            String sql = "UPDATE  `notification` SET `isRead` = 1 WHERE `id` = ?";
            conn = DbUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
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

}
