package service;

import java.util.List;

import dao.BookDao;
import util.JDBCUtil;

/**
 * @author 송예진
 *
 */
public class BookService {
	private static BookService instance;

	private BookService() {

	}

	public static BookService getInstance() {
		if (instance == null) {
			instance = new BookService();
		}
		return instance;
	}
	BookDao bdao = BookDao.getInstance();
	
	//
	public void bookReservation(List<Object> param) {
		bdao.bookReservation(param);
	}
}
