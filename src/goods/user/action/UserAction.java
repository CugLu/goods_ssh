package goods.user.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import goods.user.domain.User;
import goods.user.service.UserService;
import goods.user.service.exception.UserException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import cn.itcast.commons.CommonUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class UserAction extends ActionSupport implements ModelDriven<User>{
	
	@Resource private UserService userService;
	
	// 模型驱动使用的对象
	private User user = new User();
	
	@Override
	public User getModel() {
		return user;
	}
	
	/**
	 * 修改密码　
	 */
	public String updatePassword() {
		/*
		 * 1. 封装表单数据到user中
		 * 2. 从session中获取uid
		 * 3. 使用uid和表单中的oldPass和newPass来调用service方法
		 *   > 如果出现异常，保存异常信息到request中，转发到pwd.jsp
		 * 4. 保存成功信息到rquest中
		 * 5. 转发到msg.jsp
		 */
		User muser = (User)ServletActionContext.getRequest().getSession().getAttribute("sessionUser");
		// 如果用户没有登录，返回到登录页面，显示错误信息
		if(muser == null) {
			ServletActionContext.getRequest().setAttribute("msg", "您还没有登录！");
			return "login";
		}
		
		Map<String,String> errors = validateRegist(user, ServletActionContext.getRequest().getSession());
		if(errors.size() > 0) {
			ServletActionContext.getRequest().setAttribute("user", user);
			ServletActionContext.getRequest().setAttribute("msg", "仔细看看吧，一定有什么错了！");
			return "pwd";
		}
		
		try {
			userService.updatePassword(muser.getUid(), user.getNewpass(), 
					user.getLoginpass());
			ServletActionContext.getRequest().setAttribute("msg", "修改密码成功");
			ServletActionContext.getRequest().setAttribute("code", "success");
			return "msg";
		} catch (UserException e) {
			ServletActionContext.getRequest().setAttribute("msg", e.getMessage());//保存异常信息到request
			ServletActionContext.getRequest().setAttribute("user", user);//为了回显
			return "pwd";
		}
	}
	
	/*
	 * 注册校验
	 * 对表单的字段进行逐个校验，如果有错误，使用当前字段名称为key，错误信息为value，保存到map中
	 * 返回map
	 */
	private Map<String,String> validateupdatePassword(User formUser, HttpSession session) {
		Map<String,String> errors = new HashMap<String,String>();
		/*
		 * 校验登录密码
		 */
		String loginpass = formUser.getLoginpass();
		if(loginpass == null || loginpass.trim().isEmpty()) {
			errors.put("loginpass", "密码不能为空！");
		} else if(loginpass.length() < 3 || loginpass.length() > 20) {
			errors.put("loginpass", "密码长度必须在3~20之间！");
		}
		
		/*
		 * 确认密码校验
		 */
		String newpass = formUser.getNewpass();
		if(newpass == null || newpass.trim().isEmpty()) {
			errors.put("newpass", "新密码不能为空！");
		} else if(newpass.length() < 3 || newpass.length() > 20) {
			errors.put("newpass", "密码长度必须在3~20之间！");
		}
		
		/*
		 * 确认密码校验
		 */
		String reloginpass = formUser.getReloginpass();
		if(reloginpass == null || reloginpass.trim().isEmpty()) {
			errors.put("reloginpass", "确认密码不能为空！");
		} else if(!reloginpass.equals(newpass)) {
			errors.put("reloginpass", "两次输入不一致！");
		}
		
		/*
		 * 5. 验证码校验
		 */
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if(!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "验证码错误！");
		}
		
		return errors;
	}
	
	/**
	 * 退出功能
	 */
	public String quit() {
		ServletActionContext.getRequest().getSession().invalidate();
		return "quit";
	}
	
	/**
	 * 登录功能
	 * @throws UnsupportedEncodingException 
	 */
	public String login() throws UnsupportedEncodingException {
		/*
		 * 1. 封装表单数据到User
		 * 2. 校验表单数据
		 * 3. 使用service查询，得到User
		 * 4. 查看用户是否存在，如果不存在：
		 *   * 保存错误信息：用户名或密码错误
		 *   * 保存用户数据：为了回显
		 *   * 转发到login.jsp
		 * 5. 如果存在，查看状态，如果状态为false：
		 *   * 保存错误信息：您没有激活
		 *   * 保存表单数据：为了回显
		 *   * 转发到login.jsp
		 * 6. 登录成功：
		 * 　　* 保存当前查询出的user到session中
		 *   * 保存当前用户的名称到cookie中，注意中文需要编码处理。
		 */
		/*
		 * 1. 封装表单数据到user
		 */
		//模型驱动做了
		/*
		 * 2. 校验
		 */
		Map<String,String> errors = validateLogin(user, ServletActionContext.getRequest().getSession());
		if(errors.size() > 0) {
			ServletActionContext.getRequest().setAttribute("user", user);
			ServletActionContext.getRequest().setAttribute("errors", errors);
			return "login";
		}
		
		/*
		 * 3. 调用userService#login()方法
		 */
		User muser = userService.login(user);
		/*
		 * 4. 开始判断
		 */
		if(muser == null) {
			ServletActionContext.getRequest().setAttribute("msg", "用户名或密码错误！");
			ServletActionContext.getRequest().setAttribute("user", user);
			return "login";
		} else {
			if(!muser.isStatus()) {
				ServletActionContext.getRequest().setAttribute("msg", "您还没有激活！");
				ServletActionContext.getRequest().setAttribute("user", user);
				return "login";				
			} else {
				// 保存用户到session
				ServletActionContext.getRequest().getSession().setAttribute("sessionUser", muser);
				// 获取用户名保存到cookie中
				String loginname = user.getLoginname();
				loginname = URLEncoder.encode(loginname, "utf-8");
				Cookie cookie = new Cookie("loginname", loginname);
				cookie.setMaxAge(60 * 60 * 24 * 10);//保存10天
				ServletActionContext.getResponse().addCookie(cookie);
				return "loginSuccess";
			}
		}
	}
	
	/*
	 * 登录校验方法
	 */
	private Map<String,String> validateLogin(User formUser, HttpSession session) {
		Map<String,String> errors = new HashMap<String,String>();
		
		//这里我只校验了验证码
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if(!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "验证码错误！");
		}
		return errors;
	}
	
	/**
	 * 激活功能
	 */
	public String activation() {
		/*
		 * 1. 获取参数激活码
		 * 2. 用激活码调用service方法完成激活
		 *   > service方法有可能抛出异常, 把异常信息拿来，保存到request中，转发到msg.jsp显示
		 * 3. 保存成功信息到request，转发到msg.jsp显示。
		 */
		String code = ServletActionContext.getRequest().getParameter("activationCode");
		try {
			userService.activatioin(code);
			ServletActionContext.getRequest().setAttribute("code", "success");//通知msg.jsp显示对号
			ServletActionContext.getRequest().setAttribute("msg", "恭喜，激活成功，请马上登录！");
		} catch (UserException e) {
			// 说明service抛出了异常
			ServletActionContext.getRequest().setAttribute("msg", e.getMessage());
			ServletActionContext.getRequest().setAttribute("code", "error");//通知msg.jsp显示X
		}
		return "msg";
	}
	
	/**
	 * ajax用户名是否注册校验
	 */
	public String ajaxValidateLoginname() throws IOException {
		/*
		 * 1. 获取用户名
		 */
		String loginname = ServletActionContext.getRequest().getParameter("loginname");
		/*
		 * 2. 通过service得到校验结果
		 */
		boolean b = userService.ajaxValidateLoginname(loginname);
		/*
		 * 3. 发给客户端
		 */
		ServletActionContext.getResponse().getWriter().print(b);
		
		return null;
	}
	
	/**
	 * ajax Email是否注册校验
	 */
	public String ajaxValidateEmail() throws IOException {
		/*
		 * 1. 获取Email
		 */
		String email = ServletActionContext.getRequest().getParameter("email");
		/*
		 * 2. 通过service得到校验结果
		 */
		boolean b = userService.ajaxValidateEmail(email);
		/*
		 * 3. 发给客户端
		 */
		ServletActionContext.getResponse().getWriter().print(b);
		
		return null;
	}
	
	/**
	 * ajax验证码是否正确校验
	 */
	public String ajaxValidateVerifyCode() throws IOException {
		/*
		 * 1. 获取输入框中的验证码
		 */
		String verifyCode = ServletActionContext.getRequest().getParameter("verifyCode");
		/*
		 * 2. 获取图片上真实的校验码
		 */
		String vcode = (String) ServletActionContext.getRequest().getSession().getAttribute("vCode");
		/*
		 * 3. 进行忽略大小写比较，得到结果
		 */
		boolean b = verifyCode.equalsIgnoreCase(vcode);
		/*
		 * 4. 发送给客户端
		 */
		ServletActionContext.getResponse().getWriter().print(b);
		
		return null;
	}
	
	/*
	 * 注册校验
	 * 对表单的字段进行逐个校验，如果有错误，使用当前字段名称为key，错误信息为value，保存到map中
	 * 返回map
	 */
	private Map<String,String> validateRegist(User formUser, HttpSession session) {
		Map<String,String> errors = new HashMap<String,String>();
		/*
		 * 1. 校验登录名
		 */
		String loginname = formUser.getLoginname();
		if(loginname == null || loginname.trim().isEmpty()) {
			errors.put("loginname", "用户名不能为空！");
		} else if(loginname.length() < 3 || loginname.length() > 20) {
			errors.put("loginname", "用户名长度必须在3~20之间！");
		} else if(!userService.ajaxValidateLoginname(loginname)) {
			errors.put("loginname", "用户名已被注册！");
		}
		
		/*
		 * 2. 校验登录密码
		 */
		String loginpass = formUser.getLoginpass();
		if(loginpass == null || loginpass.trim().isEmpty()) {
			errors.put("loginpass", "密码不能为空！");
		} else if(loginpass.length() < 3 || loginpass.length() > 20) {
			errors.put("loginpass", "密码长度必须在3~20之间！");
		}
		
		/*
		 * 3. 确认密码校验
		 */
		String reloginpass = formUser.getReloginpass();
		if(reloginpass == null || reloginpass.trim().isEmpty()) {
			errors.put("reloginpass", "确认密码不能为空！");
		} else if(!reloginpass.equals(loginpass)) {
			errors.put("reloginpass", "两次输入不一致！");
		}
		
		/*
		 * 4. 校验email
		 */
		String email = formUser.getEmail();
		if(email == null || email.trim().isEmpty()) {
			errors.put("email", "Email不能为空！");
		} else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
			errors.put("email", "Email格式错误！");
		} else if(!userService.ajaxValidateEmail(email)) {
			errors.put("email", "Email已被注册！");
		}
		
		/*
		 * 5. 验证码校验
		 */
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if(!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "验证码错误！");
		}
		
		return errors;
	}
	
	/**
	 * 用户注册的方法:
	 */
	public String regist() {
		/*
		 * 1. 封装表单数据到User对象
		 */
		//模型驱动完成了
		/*
		 * 2. 校验之, 如果校验失败，保存错误信息，返回到regist.jsp显示
		 */
		Map<String,String> errors = validateRegist(user, ServletActionContext.getRequest().getSession());
		if(errors.size() > 0) {
			ServletActionContext.getRequest().setAttribute("form", user);
			ServletActionContext.getRequest().setAttribute("errors", errors);
			return "regist";
		}
		/*
		 * 3. 使用service完成业务
		 */
		userService.regist(user);
		/*
		 * 4. 保存成功信息，转发到msg.jsp显示！
		 */
		ServletActionContext.getRequest().setAttribute("code", "success");
		ServletActionContext.getRequest().setAttribute("msg", "注册成功，请马上到邮箱激活！");
		return "msg";
	}
	
}
