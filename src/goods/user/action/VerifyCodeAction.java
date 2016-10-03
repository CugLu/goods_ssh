package goods.user.action;

import java.awt.image.BufferedImage;

import org.apache.struts2.ServletActionContext;

import cn.itcast.vcode.utils.VerifyCode;

import com.opensymphony.xwork2.ActionSupport;

public class VerifyCodeAction extends ActionSupport {

	@Override
	public String execute() throws Exception {
		VerifyCode vc = new VerifyCode();
		BufferedImage image = vc.getImage();//获取一次性验证码图片
		
		//把图片写到指定流中
		VerifyCode.output(image, ServletActionContext.getResponse().getOutputStream());
		
		// 把文本保存到session中，为LoginServlet验证做准备
		ServletActionContext.getRequest()
			.getSession().setAttribute("vCode", vc.getText());
		return NONE;
	}
	
}