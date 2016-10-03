package goods.order.service;

import goods.order.dao.OrderDao;
import goods.order.domain.Order;
import goods.pager.PageBean;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class OrderService {
	@Resource private OrderDao orderDao;

	public PageBean<Order> myOrders(String uid, int pc) {
		return orderDao.findByUser(uid, pc);
	}

	public void createOrder(Order order) {
		orderDao.add(order);
	}

	public Order load(String oid) {
		return orderDao.load(oid);
	}

	public void update(Order _order) {
		orderDao.update(_order);
	}

	public PageBean<Order> findAll(int pc) {
		PageBean<Order> pb = orderDao.findAll(pc);
		return pb;
	}

	public PageBean<Order> findByStatus(int status, int pc) {
		PageBean<Order> pb = orderDao.findByStatus(status, pc);
		return pb;
	}
	
}
