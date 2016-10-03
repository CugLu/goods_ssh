package goods.cart.action;

import goods.book.domain.Book;
import goods.cart.domain.CartItem;
import goods.cart.service.CartItemService;
import goods.user.domain.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import cn.itcast.commons.CommonUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class CartItemAction extends ActionSupport implements ModelDriven<CartItem>{
	@Resource private CartItemService cartItemService;
	
	//模型驱动
	private CartItem cartItem=new CartItem();
	
	@Override
	public CartItem getModel() {
		return cartItem;
	}
	
	private String bid;

	public void setBid(String bid) {
		this.bid = bid;
	}
	
	/**
	 * 加载多个CartItem
	 */
	public String loadCartItems() {
		/*
		 * 1. 获取cartItemIds参数
		 */
		String cartItemIds = ServletActionContext.getRequest().getParameter("cartItemIds");
		double total = Double.parseDouble(ServletActionContext.getRequest().getParameter("total"));
		/*
		 * 2. 通过service得到List<CartItem>
		 */
		List<CartItem> cartItemList = cartItemService.loadCartItems(cartItemIds);
		/*
		 * 3. 保存，然后转发到/cart/showitem.jsp
		 */
		ServletActionContext.getRequest().setAttribute("cartItemList", cartItemList);
		ServletActionContext.getRequest().setAttribute("total", total);
		ServletActionContext.getRequest().setAttribute("cartItemIds", cartItemIds);
		return "showitem";
	}
	
	public String updateQuantity() throws IOException {
		String cartItemId = ServletActionContext.getRequest().getParameter("cartItemId");
		int quantity = Integer.parseInt(ServletActionContext.getRequest().getParameter("quantity"));
		CartItem cartItem = cartItemService.updateQuantity(cartItemId, quantity);
		
		// 给客户端返回一个json对象
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"quantity\"").append(":").append(cartItem.getQuantity());
		sb.append(",");
		sb.append("\"subtotal\"").append(":").append(cartItem.getSubtotal());
		sb.append("}");

		ServletActionContext.getResponse().getWriter().print(sb);
		return null;
	}
	
	/**
	 * 批量删除功能
	 */
	public String batchDelete() {
		/*
		 * 1. 获取cartItemIds参数
		 * 2. 调用service方法完成工作
		 * 3. 返回到list.jsp
		 */
		String cartItemIds = ServletActionContext.getRequest().getParameter("cartItemIds");
		cartItemService.batchDelete(cartItemIds);
		return myCart();
	}
	
	/**
	 * 添加购物车条目
	 */
	public String add() {
		/*
		 * 1. 封装表单数据到CartItem(bid, quantity)
		 */
		User user = (User)ServletActionContext.getRequest().getSession().getAttribute("sessionUser");
		Book book=new Book();
		book.setBid(bid);
		cartItem.setBook(book);
		cartItem.setUser(user);
		
		/*
		 * 2. 调用service完成添加
		 */
		cartItemService.add(cartItem);
		/*
		 * 3. 查询出当前用户的所有条目，转发到list.jsp显示
		 */
		return myCart();
	}
	
	/**
	 * 我的购物车
	 */
	public String myCart() {
		/*
		 * 1. 得到uid
		 */
		User user = (User)ServletActionContext.getRequest().getSession().getAttribute("sessionUser");
		String uid = user.getUid();
		/*
		 * 2. 通过service得到当前用户的所有购物车条目
		 */
		List<CartItem> cartItemLIst = cartItemService.myCart(uid);
		/*
		 * 3. 保存起来，转发到/cart/list.jsp
		 */
		ServletActionContext.getRequest().setAttribute("cartItemList", cartItemLIst);
		return "list";
	}
}
