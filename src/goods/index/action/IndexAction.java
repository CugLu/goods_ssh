package goods.index.action;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 访问首页
 * @author cailu
 *
 */
public class IndexAction extends ActionSupport {

	/**
	 * 执行的访问首页的方法:
	 */
	public String execute() throws Exception {
		return "index";
	}
	
}
