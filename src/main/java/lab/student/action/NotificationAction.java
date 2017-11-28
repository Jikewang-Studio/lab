package lab.student.action;

import com.opensymphony.xwork2.ActionSupport;
import lab.student.service.NotificationService;
import lab.util.JsonUtil;

import java.io.IOException;

/**
 * @author 李文浩
 * @version 2017/11/27.
 */
public class NotificationAction extends ActionSupport {

    private int id;

    private String userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 学生得到未阅读的通知
     */
    public String getUnreadNotifications() {
        NotificationService notificationService = new NotificationService();
        try {
            JsonUtil.toJson(notificationService.getUnreadnotifications(userId));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 学生查看关于自己的所有通知
     */
    public String getALLNotifications() {
        NotificationService notificationService = new NotificationService();
        try {
            JsonUtil.toJson(notificationService.getALLNotifications(userId));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 学生阅读通知
     *
     */
    public String readNotification() {
        NotificationService notificationService = new NotificationService();
        try {
            JsonUtil.toJson(notificationService.readNotification(id));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}