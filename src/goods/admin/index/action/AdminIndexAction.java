package goods.admin.index.action;

import com.opensymphony.xwork2.ActionSupport;

public class AdminIndexAction extends ActionSupport {
	/**
	 * 执行的访问首页的方法:
	 */
	public String execute() throws Exception {
		return "adminindex";
	}
}
