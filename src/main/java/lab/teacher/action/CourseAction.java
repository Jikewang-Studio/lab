package lab.teacher.action;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import lab.bean.Course;
import lab.teacher.service.CourseService;
import lab.util.JsonUtil;

import java.io.File;
import java.io.IOException;

public class CourseAction extends ActionSupport implements ModelDriven<Course> {

    private static final long serialVersionUID = 1L;
    private Course cos = new Course();


    /**
     * 教师根据课程id添加自己的实验课程
     *
     * @return
     */
    public String addCourseByCourseId() {
        CourseService cs = new CourseService();
        Gson g = new Gson();
        //创建教师课程目录，用于存放学生作业

        //old dir  "E:\\lab\\"+cos.getCourseId()+"\\"+cos.getUserId()
        //update date 2016/8/14
        File dir = new File("E:\\work\\" + cos.getCourseId() + "\\" + cos.getUserId());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            JsonUtil.writeJson(g.toJson(cs.addCourseByCourseId(cos)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 教师根据教师课程id删除自己添加的实验课程
     *
     * @return
     */
    public String deleteCourseByTeacherCourseId() {
        CourseService cs = new CourseService();
        Gson g = new Gson();
        try {
            JsonUtil.writeJson(g.toJson(cs.deleteCourseByTeacherCourseId(cos)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 老师更新课程备注
     *
     * @return
     */
    public String updateCourseById() {
        CourseService cs = new CourseService();
        Gson g = new Gson();
        try {
            JsonUtil.writeJson(g.toJson(cs.updateCourseById(cos)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 老师更新课程备注
     *
     * @return
     */
    public String getStudentsById() {
        CourseService cs = new CourseService();
        Gson g = new Gson();
        try {
            JsonUtil.writeJson(g.toJson(cs.getStudentsById(cos)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Course getModel() {
        return cos;
    }
}
