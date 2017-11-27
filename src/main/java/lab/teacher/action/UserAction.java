package lab.teacher.action;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import lab.bean.User;
import lab.teacher.service.UserService;
import lab.util.JsonUtil;

import java.io.IOException;

public class UserAction extends ActionSupport implements ModelDriven<User>{

	private static final long serialVersionUID = 1L;
	private User user = new User();

	

	/**
	 * 修改学生信息
	 * */
	public String updateUser(){
		UserService us = new UserService();
		Gson g = new Gson();
		try {
			JsonUtil.writeJson(g.toJson(us.updateUser(user)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 教师通过学号或者姓名查找学生
	 * 
	 */
	public String getStudentById(){
		UserService us = new UserService();
		Gson g = new Gson();
/*		if(user.getUserId()==null&&user.getUserName()==null){
			Map<String, Object> map = new HashMap<String, Object>();
			int rows = user.getRows() == 0 ? 10 : user.getRows();
			map.put("total", 0);
			map.put("rows", 13);
			System.out.println("total");
			return null;
		}*/
		try {
			JsonUtil.writeJson(g.toJson(us.getStudentById(user)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 重置学生密码为123
	 */
	public String resetStudentPassword(){
		UserService us=new UserService();
		Gson g=new Gson();
		try {
			JsonUtil.writeJson(g.toJson(us.resetUserPassword(user)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(user.getUserId());
		
		return null;
	}

	public User getModel() {
		return user;
	}
	
}
