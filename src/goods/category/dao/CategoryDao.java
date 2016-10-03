package goods.category.dao;

import goods.category.domain.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;

import cn.itcast.commons.CommonUtils;

public class CategoryDao {
	@Resource
	private SessionFactory sessionFactory;

	/**
	 * 返回所有分类
	 */
	public List<Category> findAll() {
		/*
		 * 1. 查询出所有一级分类
		 */
		String hql = "from Category where pid is null order by orderBy";
		List<Category> parents = sessionFactory.getCurrentSession()
				.createQuery(hql).list();

		return parents;
	}

	public void add(Category category) {
		sessionFactory.getCurrentSession().save(category);
	}

	public List<Category> findParents() {
		// 这里把二级的也查出来了
		return findAll();
	}

	public Category load(String cid) {
		String hql = "from Category where cid=?";
		Category category = (Category) sessionFactory.getCurrentSession()
				.createQuery(hql).setParameter(0, cid).uniqueResult();
		return category;
	}

	public void edit(Category category) {
		sessionFactory.getCurrentSession().update(category);
	}

	public int findChildrenCountByParent(String cid) {
		String hql = "select count(*) from Category where pid=?";
		Number cnt = (Number) sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter(0, cid).uniqueResult();
		return cnt.intValue();
	}

	public void delete(String cid) {
		String hql = "delete from Category where cid=?";
		Category category = load(cid);
		sessionFactory.getCurrentSession().delete(category);
	}

	public List<Category> findByParent(String pid) {
		String hql = "from Category where pid=? order by orderBy";
		List<Category> categoryList = sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter(0, pid).list();
		return categoryList;
	}

}
