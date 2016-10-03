package goods.book.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import goods.book.domain.Book;
import goods.pager.Expression;
import goods.pager.PageBean;
import goods.pager.PageConstants;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

public class BookDao {
	@Resource
	private SessionFactory sessionFactory;

	public PageBean<Book> findByCategory(String cid, int pc) {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("cid", "=", cid));
		return findByCriteria(exprList, pc);
	}

	/**
	 * 通用的查询方法
	 * 
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	private PageBean<Book> findByCriteria(List<Expression> exprList, int pc) {
		/*
		 * 1. 得到ps 2. 得到tr 3. 得到beanList 4. 创建PageBean，返回
		 */
		/*
		 * 1. 得到ps
		 */
		int ps = PageConstants.BOOK_PAGE_SIZE;// 每页记录数
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
				params.add(expr.getValue());
			}
		}

		/*
		 * 3. 总记录数
		 */
		/*
		 * String sql = "select count(*) from t_book" + whereSql; Number number
		 * = (Number)qr.query(sql, new ScalarHandler(), params.toArray());
		 */
		String hql = "select count(*) from Book" + whereSql;
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for (int i = 0; i < params.size(); i++) {
			query.setParameter(i, params.get(i));
		}
		Number number = (Number) query.uniqueResult();
		int tr = number.intValue();// 得到了总记录数
		/*
		 * 4. 得到beanList，即当前页记录
		 */
		/*
		 * sql = "select * from t_book" + whereSql +
		 * " order by orderBy limit ?,?"; params.add((pc-1) * ps);//当前页首行记录的下标
		 * params.add(ps);//一共查询几行，就是每页记录数
		 * 
		 * List<Book> beanList = qr.query(sql, new
		 * BeanListHandler<Book>(Book.class), params.toArray());
		 */
		hql = "from Book" + whereSql + " order by orderBy";
		Query mquery = sessionFactory.getCurrentSession().createQuery(hql);
		for (int i = 0; i < params.size(); i++) {
			mquery.setParameter(i, params.get(i));
		}
		List<Book> beanList = mquery.setFirstResult((pc - 1) * ps)
				.setMaxResults(ps).list();
		/*
		 * 5. 创建PageBean，设置参数
		 */
		PageBean<Book> pb = new PageBean<Book>();
		/*
		 * 其中PageBean没有url，这个任务由Servlet完成
		 */
		pb.setBeanList(beanList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);

		return pb;
	}

	public Book findByBid(String bid) {
		String hql = "from Book where bid=?";
		Book book = (Book) sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter(0, bid).list().get(0);
		return book;
	}

	public PageBean<Book> findByAuthor(String author, int pc) {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("author", "like", "%" + author + "%"));
		return findByCriteria(exprList, pc);
	}

	public PageBean<Book> findByPress(String press, int pc) {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("press", "like", "%" + press + "%"));
		return findByCriteria(exprList, pc);
	}

	public PageBean<Book> findByBname(String bname, int pc) {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bname", "like", "%" + bname + "%"));
		return findByCriteria(exprList, pc);
	}

	public PageBean<Book> findByCombination(Book criteria, int pc) {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bname", "like", "%" + criteria.getBname()
				+ "%"));
		exprList.add(new Expression("author", "like", "%"
				+ criteria.getAuthor() + "%"));
		exprList.add(new Expression("press", "like", "%" + criteria.getPress()
				+ "%"));
		return findByCriteria(exprList, pc);
	}

	public int findBookCountByCategory(String cid) {
		String hql = "select count(*) from Book where cid=?";
		Number cnt = (Number) sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter(0, cid).uniqueResult();
		return cnt.intValue();
	}

	public void edit(Book book) {
		sessionFactory.getCurrentSession().update(book);
	}

	public void delete(String bid) {
		String hql = "delete from Book where bid=?";
		sessionFactory.getCurrentSession().createQuery(hql)
				.setParameter(0, bid).executeUpdate();
	}

	public void add(Book book) {
		sessionFactory.getCurrentSession().save(book);
	}

}
