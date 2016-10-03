package goods.book.action;

import goods.book.domain.Book;
import goods.book.service.BookService;
import goods.pager.PageBean;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import cn.itcast.commons.CommonUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class BookAction extends ActionSupport implements ModelDriven<Book>{
	@Resource private BookService bookService;
	
	//模型驱动
	private Book book=new Book();
	
	@Override
	public Book getModel() {
		return book;
	}
	
	//接受cid参数
	private String cid;

	public void setCid(String cid) {
		this.cid = cid;
	}
	
	/**
	 * 获取当前页码
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req) {
		int pc = 1;
		String param = req.getParameter("pc");
		if(param != null && !param.trim().isEmpty()) {
			try {
				pc = Integer.parseInt(param);
			} catch(RuntimeException e) {}
		}
		return pc;
	}
	
	/**
	 * 截取url，页面中的分页导航中需要使用它做为超链接的目标！
	 * @param req
	 * @return
	 */
	private String getUrl(HttpServletRequest req) {
		String url = req.getRequestURI() + "?" + req.getQueryString();
		/*
		 * 如果url中存在pc参数，截取掉，如果不存在那就不用截取。
		 */
		int index = url.lastIndexOf("&pc=");
		if(index != -1) {
			url = url.substring(0, index);
		}
		return url;
	}
	
	/**
	 * 按分类查
	 */
	public String findByCategory() {
		/*
		 * 1. 得到pc：如果页面传递，使用页面的，如果没传，pc=1
		 */
		int pc = getPc(ServletActionContext.getRequest());
		/*
		 * 2. 得到url：...
		 */
		String url = getUrl(ServletActionContext.getRequest());
		/*
		 * 3. 获取查询条件，本方法就是cid，即分类的id
		 */
		//模型驱动做了
		/*
		 * 4. 使用pc和cid调用service#findByCategory得到PageBean
		 */
		PageBean<Book> pb = bookService.findByCategory(cid, pc);
		/*
		 * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		ServletActionContext.getRequest().setAttribute("pb", pb);
		return "list";
	}
	
	/**
	 * 按bid查询
	 */
	public String load() {
		Book mbook = bookService.load(book.getBid());//通过bid得到book对象
		ServletActionContext.getRequest().setAttribute("book", mbook);//保存到req中
		return "desc";//转发到desc.jsp
	}
	
	/**
	 * 按作者查
	 */
	public String findByAuthor() {
		/*
		 * 1. 得到pc：如果页面传递，使用页面的，如果没传，pc=1
		 */
		int pc = getPc(ServletActionContext.getRequest());
		/*
		 * 2. 得到url：...
		 */
		String url = getUrl(ServletActionContext.getRequest());
		/*
		 * 3. 获取查询条件
		 */
		//是因为在web.xml配置了，你在这里转也行
		String author = ServletActionContext.getRequest().getParameter("author");
		/*
		 * 4. 得到PageBean
		 */
		PageBean<Book> pb = bookService.findByAuthor(author, pc);
		/*
		 * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		ServletActionContext.getRequest().setAttribute("pb", pb);
		return "list";
	}
	
	/**
	 * 按出版社查询
	 */
	public String findByPress() {
		/*
		 * 1. 得到pc：如果页面传递，使用页面的，如果没传，pc=1
		 */
		int pc = getPc(ServletActionContext.getRequest());
		/*
		 * 2. 得到url：...
		 */
		String url = getUrl(ServletActionContext.getRequest());
		/*
		 * 3. 获取查询条件
		 */
		String press = ServletActionContext.getRequest().getParameter("press");
		/*
		 * 4. 得到PageBean
		 */
		PageBean<Book> pb = bookService.findByPress(press, pc);
		/*
		 * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		ServletActionContext.getRequest().setAttribute("pb", pb);
		return "list";
	}
	
	/**
	 * 按图名查
	 */
	public String findByBname() {
		/*
		 * 1. 得到pc：如果页面传递，使用页面的，如果没传，pc=1
		 */
		int pc = getPc(ServletActionContext.getRequest());
		/*
		 * 2. 得到url：...
		 */
		String url = getUrl(ServletActionContext.getRequest());
		/*
		 * 3. 获取查询条件
		 */
		String bname = ServletActionContext.getRequest().getParameter("bname");
		/*
		 * 4. 得到PageBean
		 */
		PageBean<Book> pb = bookService.findByBname(bname, pc);
		/*
		 * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		ServletActionContext.getRequest().setAttribute("pb", pb);
		return "list";
	}
	
	/**
	 * 多条件组合查询
	 */
	public String findByCombination() {
		/*
		 * 1. 得到pc：如果页面传递，使用页面的，如果没传，pc=1
		 */
		int pc = getPc(ServletActionContext.getRequest());
		/*
		 * 2. 得到url：...
		 */
		String url = getUrl(ServletActionContext.getRequest());
		/*
		 * 3. 获取查询条件
		 */
		//模型驱动
		/*
		 * 4. 得到PageBean
		 */
		PageBean<Book> pb = bookService.findByCombination(book, pc);
		/*
		 * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		ServletActionContext.getRequest().setAttribute("pb", pb);
		return "list";
	}
	
}
