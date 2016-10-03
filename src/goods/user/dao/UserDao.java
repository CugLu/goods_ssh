package goods.user.dao;

import java.util.List;

import goods.user.domain.User;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;

public class UserDao {
	@Resource private SessionFactory sessionFactory;
	
	public boolean ajaxValidateLoginname(String loginname) {
		String hql = "select count(*) from User where loginname=?";
		Number number = (Number)sessionFactory.getCurrentSession().createQuery(hql).
				setParameter(0, loginname).list().get(0);
		return number.intValue() == 0;
	}
	
	public boolean ajaxValidateEmail(String email) {
		String hql = "select count(*) from User where email=?";
		Number number = (Number)sessionFactory.getCurrentSession().createQuery(hql).
				setParameter(0, email).list().get(0);
		//另一种写法
		/*int count=(Integer) sessionFactory.getCurrentSession().createQuery(hql).
				setParameter(0, email).uniqueResult();*/
		return number.intValue() == 0;
	}

	public void add(User user) {
		sessionFactory.getCurrentSession().save(user);
	}

	public User findByCode(String code) {
		String hql = "from User where activationCode = ?";
		@SuppressWarnings("unchecked")
		List<User> list=sessionFactory.getCurrentSession().createQuery(hql).
				setParameter(0, code).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public void updateStatus(User user) {
		sessionFactory.getCurrentSession().update(user);
	}

	public User findByLoginnameAndLoginpass(String loginname, String loginpass) {
		String hql = "from User where loginname=? and loginpass=?";
		@SuppressWarnings("unchecked")
		List<User> list=sessionFactory.getCurrentSession().createQuery(hql).
			setParameter(0, loginname).setParameter(1, loginpass).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public User findByUidAndPassword(String uid, String loginpass) {
		String hql = "from User where uid=? and loginpass=?";
		@SuppressWarnings("unchecked")
		List<User> list=sessionFactory.getCurrentSession().createQuery(hql).
			setParameter(0, uid).setParameter(1, loginpass).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public void updatePassword(User user) {
		sessionFactory.getCurrentSession().update(user);
	}
	
}
