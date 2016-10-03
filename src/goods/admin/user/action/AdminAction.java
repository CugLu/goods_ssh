package goods.admin.user.action;

import goods.admin.user.domain.Admin;
import goods.admin.user.service.AdminService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;


import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class AdminAction extends ActionSupport implements ModelDriven<Admin>{
	@Resource private AdminService adminService;
	
	private Admin admin =new Admin();

	@Override
	public Admin getModel() {
		return admin;
	}
	
	/**
	 * 登录功能
	 */
	public String login() {
		
		HttpServletRequest req = ServletActionContext.getRequest();
		/*
		 * 1. 封装表单数据到Admin
		 */
		//模型驱动
		Admin madmin = adminService.login(admin);
		if(madmin == null) {
			req.setAttribute("msg", "用户名或密码错误！");
			return "login";
		}
		req.getSession().setAttribute("admin", madmin);
		return "index";
	}
	
	/**
	 * 退出功能
	 */
	public String quit() {
		ServletActionContext.getRequest().getSession().invalidate();
		return "quit";
	}
	
}
