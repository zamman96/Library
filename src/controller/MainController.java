package controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import print.Print;
import service.BookService;
import util.ScanUtil;
import util.View;

public class MainController extends Print {
	BookController bookController = BookController.getInstance();
	LibraryController libraryController = LibraryController.getInstance();
	static public Map<String, Object> sessionStorage = new HashMap<>();
	// 로그인을 위해 MemberService 클래스 호출

	public static void main(String[] args) {
		new MainController().start();
	}

	private void start() {
		View view = View.MAIN;
//		View view = View.LIBRARY;
//		View view = View.BOOK_CATE_LIST;
		while (true) {
			switch (view) {
			case MAIN:
				view = home();
				break;
			case MAIN_MEMBER:
				view = home_member();
				break;
			case MAIN_LIBRARY:
				view = home_library();
				break;
			case MAIN_ALL:
				view = home_all();
				break;
			case LIBRARY:
				view = library();
				break;
			case LIBRARY_LOCAL:
				view = libraryController.localLibrary();
				break;
			case LIBRARY_SEARCH:
				view = libraryController.searchLibrary();
				break;
			case LIBRARY_LIST:
				view = libraryController.librarylist();
				break;
			case BOOK:
				view = book();
				break;
			case BOOK_LIST:
				view = bookController.bookList();
				break;
			case BOOK_CATE_LIST:
				view = bookController.bookCateList();
				break;
			case PDS:
				view = pds();
				break;
			default:
				break;
			}
		}
	}

	public View mainMenu() {
		if (sessionStorage.containsKey("member") && sessionStorage.containsKey("library")) {
			return View.MAIN_ALL;
		}
		if (sessionStorage.containsKey("member")) {
			return View.MAIN_MEMBER;
		}
		if (sessionStorage.containsKey("library")) {
			return View.MAIN_LIBRARY;
		}
		return View.MAIN;
	}

	private View home() {
		title();
		System.out.println();
		System.out.println();
		printMenuVar();
		System.out.println("\t\t1. 도서관 선택\t2. 도서 조회\t3. 자료실 좌석 조회\t\t");
		System.out.println("\t\t4. 로그인\t\t5. 아이디/비밀번호 찾기\t6. 회원가입");
		printMenuVar();

		int sel = ScanUtil.menu();
		switch (sel) {
		case 1:
			return View.LIBRARY;
		case 2:
			return View.BOOK;
		case 3:
			return View.PDS;

		default:
			return View.MAIN;
		}
	}

	private View home_member() {
		printMenuVar();
		System.out.println("\t\t1. 마이페이지\t2. 도서관 선택\t3. 도서 조회 ");
		System.out.println("\t\t4. 자료실 좌석 조회\t5. 로그아웃");
		printMenuVar();
		int sel = ScanUtil.menu();
		switch (sel) {
//		case 1:
//			return View.LIBRARY;
		case 2:
			return View.LIBRARY;
		case 3:
			return View.BOOK;
		case 4:
			return View.PDS;

		default:
			return View.MAIN_MEMBER;
		}
	}

	private View home_library() {
		Map<String, Object> library = (Map<String, Object>) sessionStorage.get("library");
		String name = (String) library.get("LIB_NAME");
		System.out.println("\t" + name);
		printMenuVar();
		System.out.println("\t\t1. 도서 조회\t2. 자료실 좌석 조회");
		System.out.println("\t\t3. 로그인\t4. 아이디/비밀번호 찾기\t5. 회원가입");
		printMenuVar();
		int sel = ScanUtil.menu();
		switch (sel) {
		case 1:
			return View.BOOK;
		case 2:
			return View.PDS;

		default:
			return View.MAIN_LIBRARY;
		}
	}

	private View home_all() {
		printMenuVar();
		System.out.println("\t\t1. 마이페이지\t2. 도서 조회 ");
		System.out.println("\t\t3. 자료실 좌석 조회\t4. 로그아웃");
		printMenuVar();
		int sel = ScanUtil.menu();
		switch (sel) {
//		case 1:
//			return View.LIBRARY;
		case 2:
			return View.BOOK;
		case 3:
			return View.PDS;

		default:
			return View.MAIN_ALL;
		}
	}

	private View book() {
		if (!sessionStorage.containsKey("libaray")) {
			noticeLibraryNoSel();
		}
		printMenuVar();
		System.out.println("\t\t1. 모든 도서 조회\t2. 분류별 도서 조회\t3. 도서 검색");
		if (sessionStorage.containsKey("member")) {
			System.out.println("\t\t4. 대출\t5. 대출 예약\t6. 연장 / 반납");
		}
		System.out.println("\t\t\t\t0. 홈");
		printMenuVar();
		int sel = ScanUtil.menu();
		// 회원으로 로그인하지 않으면 메뉴에 들어가지지않음
		if (!sessionStorage.containsKey("member") && sel >= 4 && sel <= 6) {
			return View.BOOK;
		}

		switch (sel) {
		case 1:
			return View.BOOK_LIST;
		case 2:
			return View.BOOK_CATE_LIST;
//		case 3:
//			return View.LIBRARY_LIST;
//		case 4:
//			return View.LIBRARY_LIST;
//		case 5:
//			return View.LIBRARY_LIST;
//		case 6:
//			return View.LIBRARY_LIST;
		case 0:
			return mainMenu();
		default:
			return View.BOOK;
		}
	}

	private View library() {
		printMenuVar();
		System.out.println("\t\t1. 지역구 선택\t2. 도서관 이름 검색\t3. 전체 도서관 리스트");
		System.out.println("\t\t\t\t0. 홈");
		printMenuVar();
		int sel = ScanUtil.menu();
		switch (sel) {
		case 1:
			return View.LIBRARY_LOCAL;
		case 2:
			return View.LIBRARY_SEARCH;
		case 3:
			return View.LIBRARY_LIST;
		case 0:
			View view = View.MAIN;
			if (sessionStorage.containsKey("MAIN")) {
				view = (View) sessionStorage.get("MAIN");
			}
			return view;
		default:
			return View.LIBRARY;
		}
	}

	private View pds() {
		if (!sessionStorage.containsKey("library")) {
			noticeLibrarySel();
			return View.LIBRARY;
		}
		printMenuVar();
		System.out.println("\t\t1. 자료실 좌석 조회\t2. 예약 좌석 조회 / 취소");
		System.out.println("\t\t\t0. 홈");
		printMenuVar();
		int sel = ScanUtil.menu();
		switch (sel) {
//		case 1:
//			return View.LIBRARY_LOCAL;
//		case 2:
//			return View.LIBRARY_SEARCH;
		case 0:
			return mainMenu();
		default:
			return View.PDS;
		}

	}
}
