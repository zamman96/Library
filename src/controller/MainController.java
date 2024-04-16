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
	BookListController bookListController = BookListController.getInstance();
	LibraryController libraryController = LibraryController.getInstance();
	BookRentController bookRentController = BookRentController.getInstance();
	MemberController memberController = MemberController.getInstance();
	BookService bookService = BookService.getInstance();
	PDSController pdsController = PDSController.getInstance();
	static public Map<String, Object> sessionStorage = new HashMap<>();
	// 로그인을 위해 MemberService 클래스 호출

	public static void main(String[] args) {
		new MainController().start();
	}

	private void start() {
		View view = View.MAIN;
//		View view = View.PDS_LIST;
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
			case LOGIN:
				view = memberController.login();
				break;
			case SIGN:
				view = memberController.sign();
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
			case BOOK_OVERDUE_CHK:
				view = bookRentController.bookOverdueChk();
				break;
			case BOOK:
				view = book();
				break;
			case BOOK_LIST:
				view = bookListController.bookList();
				break;
			case BOOK_CATE_LIST:
				view = bookListController.bookCateList();
				break;
			case BOOK_SEARCH_LIST:
				view = bookListController.bookSearchList();
				break;
			case BOOK_RENT:
				view = bookRentController.bookRent();
				break;
			case BOOK_RENT_LIST:
				view = bookRentController.bookRentList();	
				break;
			case BOOK_RENT_LIST_PAST:
				view = bookListController.bookRentPastList();
				break;
			case BOOK_RESERVATION:
				view = bookRentController.bookReservation();
				break;
			case BOOK_RESERVATION_LIST:
				view = bookRentController.bookRefRent();
				break;
			case BOOK_DELAY:
				view = bookRentController.bookDelay();
				break;
			case BOOK_DELAY_PART:
				view = bookRentController.Delay();
				break;
			case BOOK_RETURN:
				view = bookRentController.returnBook();
				break;
			case BOOK_RETURN_PART:
				view = bookRentController.returnBookPart();
				break;
			case PDS:
				view = pds();
				break;
			case PDS_LIST:
				view = pdsController.pdsList();
				break;
			case PDS_RESERVATION:
				view = pdsController.pdsReserve();
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
		//시작전에 대출 예약 마감일이 지난 회원들의 상태를 변경해주고
		//다음순번의 사람이 예약기회를 얻음
		bookService.timeOver();
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
		case 4:
			return View.LOGIN;
//		case 5:
//			return View.PDS;
		case 6:
			return View.SIGN;

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
		printMenuOverVar();
		System.out.println("\t\t\t\t📖" + name);
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
		case 3:
			return View.LOGIN;
		case 4:
			return View.PDS;
		case 5:
			return View.SIGN;

		default:
			return View.MAIN_LIBRARY;
		}
	}

	private View home_all() {
		Map<String, Object> library = (Map<String, Object>) sessionStorage.get("library");
		String name = (String) library.get("LIB_NAME");
		printMenuOverVar();
		System.out.println("\t\t\t\t📖" + name);
		printMenuVar();
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
		if (!sessionStorage.containsKey("library")) {
			noticeLibraryNoSel();
			printMenuOverVar();
			System.out.println("\t\t\t📖인기 도서 순위");
			printMenuVar();

		} else {
			Map<String, Object> library = (Map<String, Object>) sessionStorage.get("library");
			String name = (String) library.get("LIB_NAME");
			printMenuOverVar();
			System.out.println("\t\t\t📖" + name + "의 인기 도서 순위");
			printMenuVar();
		}
		bookListController.bookTopList();
		printMenuVar();
		System.out.println("\t\t1. 모든 도서 조회\t2. 분류별 도서 조회\t3. 도서 검색");
		if (sessionStorage.containsKey("member")) {
			System.out.println("\t\t4. 대출\t5. 대출 예약\t6. 연장\t7. 반납");
			System.out.println("\t8. 현재 대출/예약내역\t9.과거 대출내역");
		}
		System.out.println("\t\t\t\t0. 홈");
		printMenuVar();
		int sel = ScanUtil.menu();
		// 회원으로 로그인하지 않으면 메뉴에 들어가지지않음
		if (!sessionStorage.containsKey("member") && sel >= 4 && sel <= 8) {
			return View.BOOK;
		}

		switch (sel) {
		case 1:
			return View.BOOK_LIST;
		case 2:
			return View.BOOK_CATE_LIST;
		case 3:
			return View.BOOK_SEARCH_LIST;
		case 4:
			return View.BOOK_RENT;
		case 5:
			return View.BOOK_RESERVATION;
		case 6:
			return View.BOOK_DELAY;
		case 7:
			return View.BOOK_RETURN;
		case 8:
			return View.BOOK_RENT_LIST;
		case 9:
			return View.BOOK_RENT_LIST_PAST;
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
		Map<String, Object> library = (Map<String, Object>) sessionStorage.get("library");
		String name = (String) library.get("LIB_NAME");
		printMenuOverVar();
		System.out.println("\t\t\t\t📖" + name);
		printMenuVar();
		System.out.println("\t\t1. 자료실 좌석 조회\t2. 예약 좌석 조회 / 취소");
		System.out.println("\t\t\t0. 홈");
		printMenuVar();
		int sel = ScanUtil.menu();
		switch (sel) {
		case 1:
			return View.PDS_LIST;
		case 2:
			return View.PDS_RESERVATION;
		case 0:
			return mainMenu();
		default:
			return View.PDS;
		}

	}
	
	private View found() {
		System.out.println("1. ID 찾기");
		System.out.println("2. PASSWORD 찾기");
		int sel = ScanUtil.menu();
		switch (sel) {
			case 1:
				return View.IDFOUND;
			case 2:
				return View.PWFOUND;
			default:
				return View.FOUND;
		}
	}
}
