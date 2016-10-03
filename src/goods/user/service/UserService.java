package goods.user.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import goods.user.dao.UserDao;
import goods.user.domain.User;
import goods.user.service.exception.UserException;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.Session;

import org.springframework.transaction.annotation.Transactional;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;

@Transactional
public class UserService {
	@Resource
	private UserDao userDao;

	/**
	 * 注册
	 * 
	 * @param user
	 */
	public void regist(User user) {
		/*
		 * 1. 数据的补齐
		 */
		user.setUid(CommonUtils.uuid());
		user.setStatus(false);
		user.setActivationCode(CommonUtils.uuid() + CommonUtils.uuid());
		/*
		 * 2. 向数据库插入
		 */
		userDao.add(user);
		/*
		 * 3. 发邮件
		 */
		/*
		 * 把配置文件内容加载到prop中
		 */
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader()
					.getResourceAsStream("email_template.properties"));
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		/*
		 * 登录邮件服务器，得到session
		 */
		String host = prop.getProperty("host");// 服务器主机名
		String name = prop.getProperty("username");// 登录名
		String pass = prop.getProperty("password");// 登录密码
		Session session = MailUtils.createSession(host, name, pass);

		/*
		 * 创建Mail对象
		 */
		String from = prop.getProperty("from");
		String to = user.getEmail();
		String subject = prop.getProperty("subject");
		// MessageForm.format方法会把第一个参数中的{0},使用第二个参数来替换。
		// 例如MessageFormat.format("你好{0}, 你{1}!", "张三", "去死吧"); 返回“你好张三，你去死吧！”
		String content = MessageFormat.format(prop.getProperty("content"),
				user.getActivationCode());
		Mail mail = new Mail(from, to, subject, content);
		/*
		 * 发送邮件
		 */
		try {
			MailUtils.send(session, mail);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 登录名校验
	 * 
	 * @param loginname
	 * @return
	 */
	public boolean ajaxValidateLoginname(String loginname) {
		return userDao.ajaxValidateLoginname(loginname);
	}

	/**
	 * email校验
	 * 
	 * @param email
	 * @return
	 */
	public boolean ajaxValidateEmail(String email) {
		return userDao.ajaxValidateEmail(email);
	}

	/**
	 * 注册
	 * 
	 * @param code
	 * @throws UserException
	 */
	public void activatioin(String code) throws UserException {
		/*
		 * 1. 通过激活码查询用户 2. 如果User为null，说明是无效激活码，抛出异常，给出异常信息（无效激活码） 3.
		 * 查看用户状态是否为true，如果为true，抛出异常，给出异常信息（请不要二次激活） 4. 修改用户状态为true
		 */
		User user = userDao.findByCode(code);
		if (user == null)
			throw new UserException("无效的激活码！");
		if (user.isStatus())
			throw new UserException("您已经激活过了，不要二次激活！");
		user.setStatus(true);
		userDao.updateStatus(user);// 修改状态
	}

	public User login(User user) {
		return userDao.findByLoginnameAndLoginpass(user.getLoginname(), user.getLoginpass());
	}

	public void updatePassword(String uid, String newpass, String loginpass) throws UserException {
			/*
			 * 1. 校验老密码
			 */
			User user = userDao.findByUidAndPassword(uid, loginpass);
			if (user == null)
				throw new UserException("老密码错误！");
			/*
			 * 2. 修改密码
			 */
			user.setLoginpass(newpass);
			userDao.updatePassword(user);
	}

}
