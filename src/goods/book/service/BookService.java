package goods.book.service;

import goods.book.dao.BookDao;
import goods.book.domain.Book;
import goods.pager.PageBean;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class BookService {
	@Resource private BookDao bookDao;
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	public PageBean<Book> findByCategory(String cid, int pc) {
		return bookDao.findByCategory(cid, pc);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	public Book load(String bid) {
		return bookDao.findByBid(bid);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	public PageBean<Book> findByAuthor(String author, int pc) {
		return bookDao.findByAuthor(author, pc);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	public PageBean<Book> findByPress(String press, int pc) {
		return bookDao.findByPress(press, pc);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	public PageBean<Book> findByBname(String bname, int pc) {
		return bookDao.findByBname(bname, pc);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	public PageBean<Book> findByCombination(Book criteria, int pc) {
		return bookDao.findByCombination(criteria, pc);
	}

	public int findBookCountByCategory(String cid) {
		return bookDao.findBookCountByCategory(cid);
	}

	public void edit(Book book) {
		bookDao.edit(book);
	}

	public void delete(String bid) {
		bookDao.delete(bid);
	}

	public void add(Book book) {
		bookDao.add(book);
	}
	
}
