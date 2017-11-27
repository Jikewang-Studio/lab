package lab.bean;

import java.util.Date;

/**
 * @author 李文浩
 * @version 2017/11/27.
 */
public class Notification {
    private int id;
    private String teacherId;
    private String studentId;
    private String content;
    private int isRead;
    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;

    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", teacherId='" + teacherId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", content='" + content + '\'' +
                ", isRead=" + isRead +
                ", createTime=" + createTime +
                '}';
    }
}

