package goods.cart.dao;

import goods.cart.domain.CartItem;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;

public class CartItemDao {
	@Resource
	private SessionFactory sessionFactory;

	public List<CartItem> findByUser(String uid) {
		String hql = "from CartItem where uid=? order by orderBy";
		List<CartItem> cartItem = sessionFactory.getCurrentSession()
				.createQuery(hql).setParameter(0, uid).list();
		/*for (int i = 0; i < cartItem.size(); i++) {
			CartItem m=(CartItem) sessionFactory.getCurrentSession()
					.createQuery(hql).setParameter(0, cartItem.get(i).getCartItemId()).uniqueResult();
			cartItem.set(i, m);
		}*/
		return cartItem;
	}

	public CartItem findByUidAndBid(String uid, String bid) {
		String hql = "from CartItem where uid=? and bid=?";
		CartItem cartItem = (CartItem) sessionFactory.getCurrentSession()
				.createQuery(hql).setParameter(0, uid).setParameter(1, bid)
				.uniqueResult();
		return cartItem;
	}

	public void addCartItem(CartItem cartItem) {
		sessionFactory.getCurrentSession().save(cartItem);
	}

	public void updateQuantity(CartItem cartItem) {
		sessionFactory.getCurrentSession().update(cartItem);
	}

	public CartItem getCartItem(String cartItemId) {
		return (CartItem) sessionFactory.getCurrentSession().get(CartItem.class, cartItemId);
	}

	public List<CartItem> loadCartItems(String cartItemIds) {
		Object[] cartItemIdArray = cartItemIds.split(",");
		String hql = "from CartItem where cartItemId in(:cartItemIdList)";
		return sessionFactory.getCurrentSession().createQuery(hql).
		setParameterList("cartItemIdList", cartItemIdArray).list();
	}

	public void batchDelete(String cartItemIds) {
		Object[] cartItemIdArray = cartItemIds.split(",");
		String hql = "delete from CartItem where cartItemId in(:cartItemIdList)";
		sessionFactory.getCurrentSession().createQuery(hql).
			setParameterList("cartItemIdList", cartItemIdArray).executeUpdate();
	}
	
}
