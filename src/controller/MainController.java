package controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.BookService;
import service.LibraryService;
import util.ScanUtil;
import util.View;

public class MainController {
	LibraryService libraryService = LibraryService.getInstance();
	BookController bookController = new BookController();
	static public Map<String, Object> sessionStorage = new HashMap<>();
	// 로그인을 위해 MemberService 클래스 호출

	public static void main(String[] args) {
		new MainController().start();
	}

	private void start() {
//		View view = View.MAIN;
		View view = View.LIBRARY;
		while (true) {
			switch (view) {
			case MAIN:
				view = home();
				break;
			case LIBRARY:
				view = library();
				break;
			case BOOK:
				view = book();
			default:
				break;
			}
		}
	}
	
	private View home() {
		System.out.println("1. 대전 전체 도서관 리스트");
		System.out.println("2. 대전 지역구별 도서관 리스트");
		System.out.println("3. 도서관 이름 검색");
		int sel = ScanUtil.menu();
		switch (sel) {
		case 1:
			return View.LIBRARY;
		default:
			return View.MAIN;
		}
	}
	
	private View library() {
		List<Map<String,Object>> list = libraryService.librarylist();
		for(Map<String,Object> map:list) {
			System.out.print(map.get("LIB_NO")+" : "+map.get("LIB_NAME")+"  ");
					if(((BigDecimal)map.get("LIB_NO")).intValue()%5==0) System.out.println();;
		}
		System.out.println();
//		int bookNo = ScanUtil.nextInt("선택 : ");
		int bookNo = 5;
		libraryService.librarySel(bookNo);
		return View.BOOK;
	}

	private View book() {
 		bookController.bookTopList();
		return null;
	}
}
