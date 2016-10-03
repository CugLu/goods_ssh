package goods.cart.service;

import java.util.List;

import goods.cart.dao.CartItemDao;
import goods.cart.domain.CartItem;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.commons.CommonUtils;

@Transactional
public class CartItemService {
	@Resource private CartItemDao cartItemDao;
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	public List<CartItem> myCart(String uid) {
		return cartItemDao.findByUser(uid);
	}

	public void add(CartItem cartItem) {
		/*
		 * 1. 使用uid和bid去数据库中查询这个条目是否存在
		 */
		CartItem _cartItem = cartItemDao.findByUidAndBid(
				cartItem.getUser().getUid(),
				cartItem.getBook().getBid());
		if(_cartItem == null) {//如果原来没有这个条目，那么添加条目
			cartItem.setCartItemId(CommonUtils.uuid());
			cartItemDao.addCartItem(cartItem);
		} else {
			//如果原来有这个条目，修改数量
			// 使用原有数量和新条目数量之各，来做为新的数量
			int quantity = cartItem.getQuantity() + _cartItem.getQuantity();
			// 修改这个老条目的数量
			_cartItem.setQuantity(quantity);
			cartItemDao.updateQuantity(_cartItem);
		}
	}

	public CartItem updateQuantity(String cartItemId, int quantity) {
		CartItem _cartItem=cartItemDao.getCartItem(cartItemId);
		_cartItem.setQuantity(quantity);
		cartItemDao.updateQuantity(_cartItem);//这里之前忘加了，应该是错的
		return cartItemDao.getCartItem(cartItemId);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	public List<CartItem> loadCartItems(String cartItemIds) {
		return cartItemDao.loadCartItems(cartItemIds);
	}

	public void batchDelete(String cartItemIds) {
		cartItemDao.batchDelete(cartItemIds);
	}
	
}
