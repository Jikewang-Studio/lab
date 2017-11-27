package lab.teacher.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import lab.bean.Notification;
import lab.teacher.service.NotificationService;
import lab.util.JsonUtil;

import java.io.IOException;
import java.util.List;

/**
 * @author 李文浩
 * @version 2017/11/27.
 */
public class NotificationAction extends ActionSupport implements ModelDriven<Notification> {

    private String notificationJson;

    private Notification notification = new Notification();

    public String getNotificationJson() {
        return notificationJson;
    }

    public void setNotificationJson(String notificationJson) {
        this.notificationJson = notificationJson;
    }

    /**
     * 老师给单个学生推送通知
     */
    public String pushNotification() {
        System.out.println(notification);
        NotificationService notificationService = new NotificationService();
        Gson g = new Gson();
        try {
            JsonUtil.writeJson(g.toJson(notificationService.pushNotification(notification)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 老师给多个学生推送通知
     */
    public String pushNotificationBatch() {
        List<Notification> notifications = JsonUtil.fromJson(notificationJson,
                new TypeToken<List<Notification>>() {
                }.getType());
        System.out.println(notifications);
        NotificationService notificationService = new NotificationService();
        Gson g = new Gson();
        try {
            JsonUtil.writeJson(g.toJson(notificationService.pushNotificationBatch(notifications)));
//            JsonUtil.writeJson(g.toJson(false));
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
