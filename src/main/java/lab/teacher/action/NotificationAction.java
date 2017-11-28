package lab.teacher.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import lab.bean.Notification;
import lab.teacher.service.NotificationService;
import lab.util.JsonUtil;

import java.io.IOException;

/**
 * @author 李文浩
 * @version 2017/11/27.
 */
public class NotificationAction extends ActionSupport implements ModelDriven<Notification> {


    private Notification notification = new Notification();

    private String studentIds;

    public String getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(String studentIds) {
        this.studentIds = studentIds;
    }

    /**
     * 老师给单个学生推送通知
     */
    public String pushNotification() {
        NotificationService notificationService = new NotificationService();
        try {
            JsonUtil.toJson(notificationService.pushNotification(notification));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 老师给多个学生推送通知
     */
    public String pushNotificationBatch() {
        NotificationService notificationService = new NotificationService();
        try {
            JsonUtil.toJson(notificationService.pushNotificationBatch(notification, studentIds.split(",")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Notification getModel() {
        return notification;
    }
}
