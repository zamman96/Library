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
//		View view = View.BOOK_LIST;
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
			case PDS:
				view = pds();
				break;
			default:
				break;
			}
		}
	}
	
	private View main() {
		View view = View.MAIN;
		if(sessionStorage.containsKey("MAIN")) {
			view = (View) sessionStorage.get("MAIN");
		}
		return view;
	}

	private View home() {
		System.out.println("1. 도서관 선택");
		System.out.println("2. 도서 조회");
		System.out.println("3. 자료실 좌석 조회");
		// 자료실 좌석 조회는 도서관을 선택한 후에 가능하기 때문에 Library로 이동
		System.out.println("4. 로그인");
		System.out.println("5. 아이디/비밀번호 찾기");
		System.out.println("6. 회원가입");
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
		System.out.println("1. 마이페이지");
		System.out.println("2. 도서관 선택");
		System.out.println("3. 도서 조회 ");
		System.out.println("4. 자료실 좌석 조회");
		System.out.println("5. 로그아웃");
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
		System.out.println("1. 도서 조회 ");
		System.out.println("2. 자료실 좌석 조회");
		System.out.println("3. 로그인");
		System.out.println("4. 아이디/비밀번호 찾기");
		System.out.println("5. 회원가입");
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
		System.out.println("1. 마이페이지");
		System.out.println("2. 도서 조회 ");
		System.out.println("3. 자료실 좌석 조회");
		System.out.println("4. 로그아웃");
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
		System.out.println("1. 모든 도서 조회");
		System.out.println("2. 분류별 도서 조회");
		System.out.println("3. 도서 검색");
		if (sessionStorage.containsKey("member")) {
			System.out.println("4. 대출");
			System.out.println("5. 대출 예약");
			System.out.println("6. 연장 / 반납");
		}
		System.out.println("0. 홈");
		int sel = ScanUtil.menu();
		// 회원으로 로그인하지 않으면 메뉴에 들어가지지않음
		if (!sessionStorage.containsKey("member") && sel >= 4 && sel <= 6) {
			return View.BOOK;
		}

		switch (sel) {
		case 1:
			return View.LIBRARY_LOCAL;
		case 2:
			return View.LIBRARY_SEARCH;
		case 3:
			return View.LIBRARY_LIST;
		case 4:
			return View.LIBRARY_LIST;
		case 5:
			return View.LIBRARY_LIST;
		case 6:
			return View.LIBRARY_LIST;
		case 0:
			return main();
		default:
			return View.BOOK;
		}
	}

	private View library() {
		System.out.println("1. 지역구 선택");
		System.out.println("2. 도서관 이름 검색");
		System.out.println("3. 전체 도서관 리스트");
		System.out.println("0. 홈");
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
			if(sessionStorage.containsKey("MAIN")) {
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
		System.out.println("1. 자료실 좌석 조회");
		System.out.println("2. 예약 좌석 조회 / 취소");
		System.out.println("0. 홈");
		int sel = ScanUtil.menu();
		switch (sel) {
//		case 1:
//			return View.LIBRARY_LOCAL;
//		case 2:
//			return View.LIBRARY_SEARCH;
		case 0:
			return main();
		default:
			return View.PDS;
		}

	}
}
