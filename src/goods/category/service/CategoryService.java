package goods.category.service;

import java.util.List;

import goods.category.dao.CategoryDao;
import goods.category.domain.Category;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CategoryService {
	@Resource private CategoryDao categoryDao;
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	public List<Category> findAll() {
		return categoryDao.findAll();
	}

	public void add(Category category) {
		categoryDao.add(category);
	}

	public List<Category> findParents() {
		return categoryDao.findParents();
	}

	public Category load(String cid) {
		return categoryDao.load(cid);
	}

	public void edit(Category category) {
		categoryDao.edit(category);
	}

	public int findChildrenCountByParent(String cid) {
		return categoryDao.findChildrenCountByParent(cid);
	}

	public void delete(String cid) {
		categoryDao.delete(cid);
	}

	public List<Category> findChildren(String pid) {
		return categoryDao.findByParent(pid);
	}

	
}
