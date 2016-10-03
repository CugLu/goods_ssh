package goods.order.dao;

import goods.book.domain.Book;
import goods.order.domain.Order;
import goods.pager.Expression;
import goods.pager.PageBean;
import goods.pager.PageConstants;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

public class OrderDao {
	@Resource
	private SessionFactory sessionFactory;

	public PageBean<Order> findByUser(String uid, int pc) {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("uid", "=", uid));
		return findByCriteria(exprList, pc);
	}

	private PageBean<Order> findByCriteria(List<Expression> exprList, int pc) {
		/*
		 * 1. 得到ps 2. 得到tr 3. 得到beanList 4. 创建PageBean，返回
		 */
		/*
		 * 1. 得到ps
		 */
		int ps = PageConstants.ORDER_PAGE_SIZE;// 每页记录数
		/*
		 * 2. 通过exprList来生成where子句
		 */
		StringBuilder whereSql = new StringBuilder(" where 1=1");
		List<Object> params = new ArrayList<Object>();// SQL中有问号，它是对应问号的值
		for (Expression expr : exprList) {
			/*
			 * 添加一个条件上， 1) 以and开头 2) 条件的名称 3) 条件的运算符，可以是=、!=、>、< ... is null，is
			 * null没有值 4) 如果条件不是is null，再追加问号，然后再向params中添加一与问号对应的值
			 */
			whereSql.append(" and ").append(expr.getName()).append(" ")
					.append(expr.getOperator()).append(" ");
			// where 1=1 and bid = ?
			if (!expr.getOperator().equals("is null")) {
				whereSql.append("?");
				if(expr.getName().equals("status"))
					params.add(Integer.parseInt(expr.getValue()));
				else
					params.add(expr.getValue());
			}
		}

		/*
		 * 3. 总记录数
		 */
		String hql = "select count(*) from Order" + whereSql;
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for (int i = 0; i < params.size(); i++) {
			query.setParameter(i, params.get(i));
		}
		System.out.println(whereSql);
		Number number = (Number) query.uniqueResult();
		int tr = number.intValue();// 得到了总记录数

		/*
		 * 4. 得到beanList，即当前页记录
		 */
		hql = "from Order" + whereSql + " order by ordertime";
		Query mquery = sessionFactory.getCurrentSession().createQuery(hql);
		for (int i = 0; i < params.size(); i++) {
			mquery.setParameter(i, params.get(i));
		}
		List<Order> beanList = mquery.setFirstResult((pc - 1) * ps)
				.setMaxResults(ps).list();
		/*
		 * 5. 创建PageBean，设置参数
		 */
		PageBean<Order> pb = new PageBean<Order>();
		/*
		 * 其中PageBean没有url，这个任务由Servlet完成
		 */
		pb.setBeanList(beanList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);

		return pb;
	}

	public void add(Order order) {
		sessionFactory.getCurrentSession().save(order);
	}

	public Order load(String oid) {
		String hql = "from Order where oid=?";
		Order order = (Order) sessionFactory.getCurrentSession()
				.createQuery(hql).setParameter(0, oid).uniqueResult();
		return order;
	}

	public void update(Order _order) {
		sessionFactory.getCurrentSession().update(_order);
	}

	public PageBean<Order> findAll(int pc) {
		List<Expression> exprList = new ArrayList<Expression>();
		return findByCriteria(exprList, pc);
	}

	public PageBean<Order> findByStatus(int status, int pc) {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("status", "=", status + ""));
		return findByCriteria(exprList, pc);
	}

}
