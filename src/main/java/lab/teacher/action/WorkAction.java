package lab.teacher.action;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import lab.bean.Work;
import lab.teacher.service.WorkService;
import lab.util.JsonUtil;

import java.io.IOException;

public class WorkAction extends ActionSupport implements ModelDriven<Work> {


    private static final long serialVersionUID = 1L;

    private Work wk = new Work();

    /**
     * 教师根据自己发布的实验任务获取学生作业
     *
     * @return
     */
    public String getWorkByTaskId() {
        WorkService ws = new WorkService();
        Gson g = new Gson();
        try {
            JsonUtil.writeJson(g.toJson(ws.getWorkByTaskId(wk)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 教师根据自己发布的实验任务获取未完成作业的学生
     *
     * @return
     */
    public String getUnfinishedStudentByTaskId() {
        WorkService ws = new WorkService();
        try {
            JsonUtil.toJson(ws.getUnfinishedStudentByTaskId(wk));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Work getModel() {
        return wk;
    }
}
