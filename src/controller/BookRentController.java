package controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import print.Print;
import service.BookService;
import util.ScanUtil;
import util.View;

public class BookRentController extends Print {
	private static BookRentController instance;

	private BookRentController() {

	}

	public static BookRentController getInstance() {
		if (instance == null) {
			instance = new BookRentController();
		}
		return instance;
	}

	BookService bookService = BookService.getInstance();

	public View mainMenu() {
		if (MainController.sessionStorage.containsKey("member")
				&& MainController.sessionStorage.containsKey("library")) {
			return View.MAIN_ALL;
		}
		if (MainController.sessionStorage.containsKey("member")) {
			return View.MAIN_MEMBER;
		}
		if (MainController.sessionStorage.containsKey("library")) {
			return View.MAIN_LIBRARY;
		}
		return View.MAIN;
	}

	/**
	 * @return mem_no정보를 담고있는 param
	 */
	public List<Object> memberNo() {
		List<Object> param = new ArrayList<>();
		Map<String, Object> member = (Map<String, Object>) MainController.sessionStorage.get("member");
		String no = "" + ((BigDecimal) member.get("MEM_NO")).intValue();
		param.add(no);
		return param;
	}

	public View overdueChk(View view) {
		// 연체 여부 돌아와서 확인 후 돌아감
		if (MainController.sessionStorage.containsKey("overdue")) {
			overdueNotice();
			return mainMenu();
		}
		// 연체 확인받지 않을 경우 BOOK_OVERDUE_CHK View로 돌아감
		if (!MainController.sessionStorage.containsKey("Check")) {
			MainController.sessionStorage.put("View", view);
			return View.BOOK_OVERDUE_CHK;
		} 
		return view;
	}

	public View bookRent() {
		if (!MainController.sessionStorage.containsKey("member")) {
			noticeMemberSel();
			return View.LOGIN;
		}
		if(!MainController.sessionStorage.containsKey("Check")) {
			overdueChk(View.BOOK_RENT);
		}
		List<Object> param = memberNo();
		int vol = bookService.memberRentVol(param);
		System.out.println("대출할 도서의 도서번호를 입력해주세요");
		System.out.println("만약 이전 화면으로 돌아가고 싶다면 0번을 입력해주세요");
		String bookNo = ScanUtil.bookNo();
		if(bookNo.equals("0")) {
			return View.BOOK;
		}
		// 대출 가능여부
		boolean bookRentChk = bookService.bookRentChk(bookNo);
		if(!bookRentChk) {
			System.out.println("대출이 불가능한 책입니다.");
			System.out.println("메뉴를 선택해 주세요");
			System.out.println("1. 대출 예약\t\t2.다른 도서 대출");
			int sel = ScanUtil.menu();
			switch (sel) {
			case 1:
				MainController.sessionStorage.remove("Check");
				return View.BOOK_RESERVATION;
			case 2:
				return View.BOOK_RENT;
			default:
				return View.BOOK_RENT;
			}
		}
		
		//도서관일치 여부
		
		param.add(bookNo);
		
		// 업데이트 하기전에 책이 맞는 지 여부 확인
//		Map<String, Object> 
		
		// 확인 후 대출 가능 권수와 함께 빌린 책의 반납일을 고지할 것
		System.out.println("대출 가능 권 수는 "+(vol-1)+"권 입니다.");
		
		// 대출 가능한 권수로 인해 대출 가능여부가 달라지므로 check 지움
		MainController.sessionStorage.remove("Check");
		System.out.println("1. 재대출\t\t2.도서 조회\t\t0.홈");
		
		return View.BOOK;
	}

	/**
	 * 꼭 대출/예약/연장 View들어가고나면 여부 파악해주기
	 * 
	 * @return 대출/예약/연장 가능 여부
	 */
	public View bookOverdueChk() {
		// 기존 페이지로 돌아가서 overdue의 값에 따라 경고창을 다르게 띄우고
		// main창으로 돌아가게함 그리고 overdue 삭제 필수
		View view = (View) MainController.sessionStorage.remove("View");
		List<Object> param = memberNo();
		// 회원이 연체 중일 때
		boolean memberOverdue = bookService.memberOverdueChk(param);
		if (!memberOverdue) {
			MainController.sessionStorage.put("overdue", "member");
			return view;
		}
		// 연체된 책을 지니고있을때
		boolean bookOverdue = bookService.bookOverdueChk(param);
		if (!bookOverdue) {
			MainController.sessionStorage.put("overdue", "book");
			return view;
		}

		// 대출페이지에서 왔을 때 대출 가능 여부
		if (MainController.sessionStorage.get("View") == View.BOOK_RENT) {
			boolean memberVol = bookService.memberRentChk(param);
			if (!memberVol) {
				MainController.sessionStorage.put("overdue", "rent");
				return view;
			}
		}
		// 대출예약페이지에서 왔을 때
		if (MainController.sessionStorage.get("View") == View.BOOK_RESERVATION) {
			boolean memberVol = bookService.memberRefChk(param);
			if (!memberVol) {
				MainController.sessionStorage.put("overdue", "reservation");
				return view;
			}
		}
		MainController.sessionStorage.put("Check", "true");
		return view;
	}

	public void overdueNotice() {
		List<Object> param = memberNo();
		String overdue = (String) MainController.sessionStorage.remove("overdue");
		if (overdue.equals("member")) {
			Map<String, Object> member = bookService.memberOverdue(param);
			Date date = (Date) member.get("RENT_AVADATE");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
			System.out.println(dateFormat.format(date) + " 이후 대출이 가능합니다");
		}
		if (overdue.equals("book")) {
			List<Map<String, Object>> list = bookService.bookOverdue(param);
			printOverVar();
			printBookIndex();
			printMiddleVar();
			for (Map<String, Object> map : list) {
				printBookList(map);
			}
			printUnderVar();
			System.out.println("해당 도서가 연체되었습니다. 빠른 시일내에 해당 도서관에서 반납해주세요");
		}
		if (overdue.equals("rent")) {
			System.out.println("최대 대출 가능한 도서의 수를 넘겼습니다.");
			System.out.println("최대 대출 가능 도서 : 5권");
		}
		if (overdue.equals("reservation")) {
			System.out.println("최대 대출 예약 가능한 도서의 수를 넘겼습니다.");
			System.out.println("최대 대출 예약 가능 도서 : 3권");
		}
	}
}
