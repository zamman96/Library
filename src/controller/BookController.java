package controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import print.Print;
import service.BookService;

public class BookController extends Print {
	BookService bookService = BookService.getInstance();

	/**
	 * @return 도서관 번호저장된 List<Object> param
	 */
	public List<Object> libraryNo(){
		List<Object> param = new ArrayList<>();
		Map<String,Object> map = (Map<String, Object>) MainController.sessionStorage.get("library");
		String no = ""+((BigDecimal) map.get("LIB_NO")).intValue();
		param.add(no);
		return param;
	}
	
	public void bookTopList() {
		List<Object> param = libraryNo();
		List<Map<String, Object>> list = bookService.bookTopList(param);
		printVar();
		System.out.print("순위\t");
		printBookIndex();
		printVar();
		int count = 0;
		for(Map<String,Object> map:list) {
			count++;
			System.out.print(count+"위\t");
			printBookList(map);
		}
		printVar();
	}
}
