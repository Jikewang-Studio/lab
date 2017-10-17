package lab.teacher.action;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import lab.bean.Task;
import lab.teacher.service.TaskService;
import lab.util.JsonUtil;

import java.io.IOException;

public class TaskAction extends ActionSupport implements ModelDriven<Task> {

    private static final long serialVersionUID = 1L;

    private Task tk = new Task();

    /**
     * 教师删除自己发布的实验任务
     *
     * @return
     */
    public String deleteTaskById() {
        TaskService ts = new TaskService();
        Gson g = new Gson();
        try {
            JsonUtil.writeJson(g.toJson(ts.deleteTaskById(tk)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 教师关闭任务上传
     *
     * @return
     */
    public String updateTaskUploadById() {
        TaskService ts = new TaskService();
        try {
            JsonUtil.toJson(ts.updateTaskUploadById(tk));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Task getModel() {
        return tk;
    }
}
