package goods.category.action;

import java.io.IOException;
import java.util.List;

import goods.category.domain.Category;
import goods.category.service.CategoryService;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class CategoryAction extends ActionSupport implements ModelDriven<Category>{
	@Resource private CategoryService categoryService;
	
	private Category category=new Category();

	@Override
	public Category getModel() {
		return category;
	}
	
	/**
	 * 查询所有分类
	 * @throws SQLException 
	 */
	public String findAll() {
		/*
		 * 1. 通过service得到所有的分类
		 * 2. 保存到request中，转发到left.jsp
		 */
		List<Category> parents = categoryService.findAll();
		ServletActionContext.getRequest().setAttribute("parents", parents);
		return "left";
	}
	
}
