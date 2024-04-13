package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.BookService;
import service.LibraryService;
import util.ScanUtil;
import util.View;

public class MainController {
	BookService bookService = BookService.getInstance();
	LibraryService libraryService = LibraryService.getInstance();
			static public Map<String, Object> sessionStorage = new HashMap<>();
	// 로그인을 위해 MemberService 클래스 호출

	public static void main(String[] args) {
		new MainController().start();
	}

	private void start() {
		View view = View.MAIN;
		while (true) {
			switch (view) {
			case MAIN:
				view = home();
				break;
			default:
				break;
			}
		}
	}
	
	private View home() {
		String bno = "10001";
		int mno = 1;
		
		List<Object> param = new ArrayList<>();
		param.add(bno);
		param.add(mno);
		
		bookService.bookReservation(param);
		ScanUtil.menu();


		return null;
	}
}
