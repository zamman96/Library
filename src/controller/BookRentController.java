package controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import print.Print;
import service.BookService;
import util.ScanUtil;
import util.View;

/**
 * @author PC-13
 *
 */
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

	public List<Object> libMemNo() {
		List<Object> param = new ArrayList<Object>();
		Map<String, Object> library = (Map<String, Object>) MainController.sessionStorage.get("library");
		Map<String, Object> member = (Map<String, Object>) MainController.sessionStorage.get("member");
		int lNo = ((BigDecimal) library.get("LIB_NO")).intValue();
		int mNo = ((BigDecimal) member.get("MEM_NO")).intValue();
		param.add(lNo);
		param.add(mNo);
		return param;
	}

	/**
	 * @return mem_no정보를 담고있는 param
	 */
	public List<Object> memberNo() {
		List<Object> param = new ArrayList<>();
		Map<String, Object> member = (Map<String, Object>) MainController.sessionStorage.get("member");
		int no = ((BigDecimal) member.get("MEM_NO")).intValue();
		param.add(no);
		return param;
	}

	/**
	 * 꼭 대출/예약/연장 View들어가고나면 여부 파악해주기
	 * 
	 * @return 대출/예약/연장 가능 여부
	 */
	public View bookOverdueChk() {
		// 기존 페이지로 돌아가서 overdue의 값에 따라 경고창을 다르게 띄우고
		// main창으로 돌아가게함 그리고 overdue 삭제 필수
		List<Object> param = memberNo();
		// 회원이 연체 중일 때
		boolean memberOverdue = bookService.memberOverdueChk(param);
		if (!memberOverdue) {
			MainController.sessionStorage.put("overdue", "member");
			Map<String, Object> member = bookService.memberOverdue(param);
			Date date = new Date(((Timestamp) member.get("RENT_AVADATE")).getTime());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
			System.out.println(dateFormat.format(date) + " 이후 대출이 가능합니다");
		}
		// 연체된 책을 지니고있을때
		boolean bookOverdue = bookService.bookOverdueChk(param);
		if (bookOverdue) {
			MainController.sessionStorage.put("overdue", "book");
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

		// 대출페이지에서 왔을 때 대출 가능 여부
		if (MainController.sessionStorage.get("View") == View.BOOK_RENT) {
			boolean memberVol = bookService.memberRentChk(param);
			if (!memberVol) {
				System.out.println("최대 대출 가능한 도서의 수를 넘겼습니다.");
				System.out.println("최대 대출 가능 도서 : 5권");
				MainController.sessionStorage.put("overdue", "rent");
			}

		}
		// 대출예약페이지에서 왔을 때
		if (MainController.sessionStorage.get("View") == View.BOOK_RESERVATION) {
			boolean memberVol = bookService.memberRefChk(param);
			if (!memberVol) {
				MainController.sessionStorage.put("overdue", "reservation");
				System.out.println("최대 대출 예약 가능한 도서의 수를 넘겼습니다.");
				System.out.println("최대 대출 예약 가능 도서 : 3권");
			}

		}

		// 연체 여부 돌아와서 확인 후 돌아감
		if (MainController.sessionStorage.containsKey("overdue")) {
			MainController.sessionStorage.remove("overdue");
			return View.BOOK;
		}
		MainController.sessionStorage.put("Check", "true");
		View view = (View) MainController.sessionStorage.remove("View");
		return view;
	}

	/**
	 * 책 대출
	 * 
	 * @return
	 */
	public View bookRent() {
		// 로그인 여부
		if (!MainController.sessionStorage.containsKey("member")) {
			noticeMemberSel();
			return View.LOGIN;
		}
		// 도서관 선택 여부
		if (!MainController.sessionStorage.containsKey("library")) {
			noticeLibrarySel();
			return View.LIBRARY;
		}
		// 연체 여부
		if (!MainController.sessionStorage.containsKey("Check")) {
			MainController.sessionStorage.put("View", View.BOOK_RENT);
			return View.BOOK_OVERDUE_CHK;
		}
		MainController.sessionStorage.remove("Check");
		Map<String, Object> lib = (Map<String, Object>) MainController.sessionStorage.get("library");
		int libNo = ((BigDecimal) lib.get("LIB_NO")).intValue();
		List<Object> param = memberNo();
		System.out.println("대출할 도서의 도서번호를 입력해주세요");
		System.out.println("만약 이전 화면으로 돌아가고 싶다면 0번을 입력해주세요");

		String bookNo = "";
		while (true) {
			bookNo = ScanUtil.bookNo();
			if (bookNo.equals("0")) {
				return View.BOOK;
			}
			// 책이 있는 번호인지 확인
			boolean bookChk = bookService.bookChk(bookNo);
			if (bookChk) {
				break;
			}
			System.out.println("없는 번호입니다.");
			System.out.println("다시 입력해주세요.");

		}
		int vol = bookService.memberRentVol(param);	//MEM_NO
		param.add(bookNo);
		boolean pass = true;
		if (MainController.sessionStorage.containsKey("libraryRent")) {
			Map<String, String> map = (Map<String, String>) MainController.sessionStorage.get("libraryRent");
			// 만약 예약대출인 경우//
			if (map.containsKey(bookNo)) {
				return bookReservationRent(bookNo);
			}
		}

		// 도서관일치 여부
		List<Object> book = new ArrayList<Object>();
		book.add(bookNo);
		boolean bookLibraryChk = bookService.bookLibraryChk(book, libNo);
		Map<String, Object> bookInfo = bookService.bookInformation(book);
		if (!bookLibraryChk) {
			System.out.println(bookInfo.get("LIB_NAME") + "에 위치한 도서입니다");
			System.out.println("현재 위치한 도서관에서 대출불가합니다");
			System.out.println("1. 다른 도서 대출");
			System.out.println("2. 다른 도서관 선택");
			System.out.println("3. 홈");
			int number = ScanUtil.menu();
			switch (number) {
			case 1:
				return View.BOOK_RENT;
			case 2:
				MainController.sessionStorage.remove("library");
				return View.LIBRARY;
			case 3:
				return mainMenu();
			default:
				return View.BOOK_RENT;
			}
		}
		
		// 대출 가능여부
		boolean bookRentChk = bookService.bookRentChk(bookNo);
		if (!bookRentChk) {
			System.out.println("대출이 불가능한 책입니다.");
			System.out.println("메뉴를 선택해 주세요");
			System.out.println("1. 대출 예약\t\t2.다른 도서 대출");
			System.out.println("\t\t0.홈");
			int sel = ScanUtil.menu();
			switch (sel) {
			case 1:
				return View.BOOK_RESERVATION;
			case 2:
				return View.BOOK_RENT;
			case 0:
				return mainMenu();
			default:
				return View.BOOK_RENT;
			}
		}

		// 업데이트 하기전에 책이 맞는 지 여부 확인
		System.out.println("도서명 : " + bookInfo.get("BOOK_NAME"));
		System.out.println("작가명 : " + bookInfo.get("BOOK_AUTHOR"));
		System.out.println("출판사 : " + bookInfo.get("BOOK_PUB"));
		System.out.println("출판년도 : " + bookInfo.get("BOOK_PUB_YEAR"));
		System.out.println("이 도서를 대출하시겠습니까?");
		while (true) {
			String yn = ScanUtil.nextLine("Y / N");
			if (yn.equalsIgnoreCase("N") || yn.equals(">")) {
				System.out.println("대출을 취소합니다");
				System.out.println("이전페이지로 돌아갑니다");
				return View.BOOK;
			} else if (yn.equalsIgnoreCase("Y") || yn.equals("<")) {
				break;
			}
		}

		// 대출 update
		Map<String, Object> date = bookService.bookRent(param);
		// book_RENT_COUNT추가
		bookService.bookRentCount(bookNo);

		// 확인 후 대출 가능 권수와 함께 빌린 책의 반납일을 고지할 것
		System.out.println("대출이 완료되었습니다.");
		Date returnDate = new Date(((Timestamp) date.get("RETURN_DATE")).getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
		System.out.println("반납일 : " + dateFormat.format(returnDate));
		System.out.println("남은 대출 가능 권 수는 " + (vol - 1) + "권 입니다.");
		System.out.println("1. 대출\t\t2.도서 조회\t\t0.홈");
		int lastSel = ScanUtil.menu();
		switch (lastSel) {
		case 1:
			return View.BOOK_RENT;
		case 2:
			return View.BOOK;
		case 0:
			return mainMenu();
		default:
			return View.BOOK_RENT;
		}
	}

	/**
	 * 대출 예약 대출예약은 다른 도서관에 있는 책 예약도 가능하므로 도서관 검사는 하지않음
	 */
	public View bookReservation() {
		// 로그인 여부
		if (!MainController.sessionStorage.containsKey("member")) {
			noticeMemberSel();
			return View.LOGIN;
		}
		// 연체 여부
		if (!MainController.sessionStorage.containsKey("Check")) {
			MainController.sessionStorage.put("View", View.BOOK_RESERVATION);
			return View.BOOK_OVERDUE_CHK;
		}
		MainController.sessionStorage.remove("Check");
		List<Object> param = memberNo();
		System.out.println("대출 예약할 도서의 도서번호를 입력해주세요");

		String bookNo = "";
		while (true) {
			bookNo = ScanUtil.bookNo();
			if (bookNo.equals("0")) {
				return View.BOOK;
			}
			// 책이 있는 번호인지 확인
			boolean bookChk = bookService.bookChk(bookNo);
			if (bookChk) {
				break;
			}
			System.out.println("없는 번호입니다.");
			System.out.println("다시 입력해주세요.");
		}
		int vol = bookService.memberRefVol(param);
		param.add(bookNo); // param MEM_NO, BOOK_NO저장
		// 만약 대출이 가능할 경우 대출
		// 대출 가능여부
		List<Object> book = new ArrayList<Object>();
		book.add(bookNo);

		// 책 정보
		Map<String, Object> map = bookService.bookInformation(book);
		boolean bookRentChk = bookService.bookRentChk(bookNo);
		if (bookRentChk) {
			System.out.println("대출이 가능한 책입니다.");
			System.out.println(map.get("LIB_NAME") + "에 위치한 도서입니다");
			System.out.println("해당 도서관을 선택하여 대출하세요.");
			System.out.println("1. 다른 도서 대출 예약\t\t2.대출\t\t3.도서관변경");
			System.out.println("\t\t0.홈");
			int sel = ScanUtil.menu();
			switch (sel) {
			case 1:
				return View.BOOK_RESERVATION;
			case 2:
				return View.BOOK_RENT;
			case 3:
				return View.LIBRARY;
			case 0:
				return mainMenu();
			default:
				return View.BOOK_RESERVATION;
			}
		}
		// 이미 대출 예약을 했는지 중복확인
		boolean bookRefDupChk = bookService.bookRefDupChk(param);
		if (!bookRefDupChk) {
			System.out.println("이미 대출 예약한 책입니다");
			System.out.println("이전 페이지로 돌아갑니다");
			return View.BOOK;
		}
		// 자신이 대출한 책을 예약하는 것은 아닌지 확인
		boolean bookMyRentChk = bookService.bookRentChk(param);
		if (!bookMyRentChk) {
			System.out.println("이미 대출한 책입니다");
			System.out.println("대출 예약이 불가능합니다.");
			System.out.println("이전 페이지로 돌아갑니다");
			return View.BOOK;
		}

		// 업데이트 하기전에 책이 맞는 지 여부 확인
		System.out.println("도서명 : " + map.get("BOOK_NAME"));
		System.out.println("작가명 : " + map.get("BOOK_AUTHOR"));
		System.out.println("출판사 : " + map.get("BOOK_PUB"));
		System.out.println("출판년도 : " + map.get("BOOK_PUB_YEAR"));
		System.out.println("이 도서를 대출 예약하시겠습니까?");
		while (true) {
			String yn = ScanUtil.nextLine("Y / N");
			if (yn.equalsIgnoreCase("N") || yn.equals(">")) {
				System.out.println("대출예약을 취소합니다");
				System.out.println("이전페이지로 돌아갑니다");
				return View.BOOK;
			} else if (yn.equalsIgnoreCase("Y") || yn.equals("<")) {
				break;
			}
		}
		// 책 대출 예약완료
		bookService.bookRefUpdate(book);
		bookService.bookReservation(param);
		System.out.println("남은 대출 예약 가능 권 수는 " + (vol - 1) + "권 입니다.");

		// 대출 예약 순번을 보여줄 것
		int seq = bookService.bookMySeq(param, bookNo);
		System.out.println("예약 순번은 " + seq + "번 째입니다");

		System.out.println("1. 대출예약\t\t2.도서 조회\t\t0.홈");
		int lastSel = ScanUtil.menu();
		switch (lastSel) {
		case 1:
			return View.BOOK_RESERVATION;
		case 2:
			return View.BOOK;
		case 0:
			return mainMenu();
		default:
			return View.BOOK_RENT;
		}

	}

	/**
	 * @return 로그인시 만약에 대출가능하면 이 화면으로 전환 대출은 해당 도서관에서 해야하므로 리스트만 출력
	 */
	public View bookRefRent() {
		List<Object> param = memberNo();
		List<Map<String, Object>> list = bookService.bookRefPossList(param);
		printOverVar();
		System.out.print("   예약마감일\t");
		printBookIndex();
		printMiddleVar();
		for (Map<String, Object> map : list) {
			Date refDate = new Date(((Timestamp) map.get("BOOK_REF_DATE")).getTime() + 7);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
			System.out.print("   " + dateFormat.format(refDate) + "\t");
			printBookList(map);
		}
		printUnderVar();
		return mainMenu();
	}

	/**
	 * 예약도서 대출시 들어오는 view
	 * 
	 * @param bookNo
	 * @return 별개의 view는 없으며 대출에서 들어올때 사용
	 */
	public View bookReservationRent(String bookNo) {
		List<Object> param = memberNo();
		Map<String, Object> lib = (Map<String, Object>) MainController.sessionStorage.get("library");
		int libNo = ((BigDecimal) lib.get("LIB_NO")).intValue();
		// 도서관 확인
		List<Object> book = new ArrayList<Object>();
		book.add(bookNo);
		boolean bookLibraryChk = bookService.bookLibraryChk(book, libNo);
		Map<String, Object> map = bookService.bookInformation(book);
		if (!bookLibraryChk) {
			System.out.println(map.get("LIB_NAME") + "에 위치한 도서입니다");
			System.out.println("현재 위치한 도서관에서 대출불가합니다");
			System.out.println("1. 다른 도서 대출");
			System.out.println("2. 다른 도서관 선택");
			System.out.println("3. 홈");
			int number = ScanUtil.menu();
			switch (number) {
			case 1:
				return View.BOOK_RENT;
			case 2:
				MainController.sessionStorage.remove("library");
				return View.LIBRARY;
			case 3:
				return mainMenu();
			default:
				return View.BOOK_RENT;
			}
		}

		int vol = bookService.memberRentVol(param);
		// 업데이트 하기전에 책이 맞는 지 여부 확인
		System.out.println("도서명 : " + map.get("BOOK_NAME"));
		System.out.println("작가명 : " + map.get("BOOK_AUTHOR"));
		System.out.println("출판사 : " + map.get("BOOK_PUB"));
		System.out.println("출판년도 : " + map.get("BOOK_PUB_YEAR"));
		System.out.println("이 도서를 대출하시겠습니까?");
		while (true) {
			String yn = ScanUtil.nextLine("Y / N");
			if (yn.equalsIgnoreCase("N") || yn.equals(">")) {
				System.out.println("대출을 취소합니다");
				System.out.println("이전페이지로 돌아갑니다");
				return View.BOOK;
			} else if (yn.equalsIgnoreCase("Y") || yn.equals("<")) {
				break;
			}
		}

		// 대출 가능한 권수로 인해 대출 가능여부가 달라지므로 check 지움
		MainController.sessionStorage.remove("RentCheck");
		// 대출 update
		param.add(bookNo);
		Map<String, Object> date = bookService.bookRefRent(param);
		// 다음 순번의 사람에게 ref_date부여
		bookService.updateRefDate(bookNo);
		// book_RENT_COUNT추가
		bookService.bookRentCount(bookNo);

		// 확인 후 대출 가능 권수와 함께 빌린 책의 반납일을 고지할 것
		System.out.println("대출이 완료되었습니다.");
		Date returnDate = new Date(((Timestamp) date.get("RETURN_DATE")).getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
		System.out.println("반납일 : " + dateFormat.format(returnDate));
		System.out.println("남은 대출 가능 권 수는 " + (vol - 1) + "권 입니다.");
		System.out.println("1. 대출\t\t2.도서 조회\t\t0.홈");
		int lastSel = ScanUtil.menu();
		switch (lastSel) {
		case 1:
			return View.BOOK_RENT;
		case 2:
			return View.BOOK;
		case 0:
			return mainMenu();
		default:
			return View.BOOK_RENT;
		}
	}

	/**
	 * 연장
	 * 
	 * @return 연장은 도서관제한없이 가능 반납일이 7일 남았을 때부터 연장 가능
	 * 
	 *         다만 연체인 경우엔 불가능
	 */
	public View bookDelay() {
		// 연체 여부
		if (!MainController.sessionStorage.containsKey("Check")) {
			MainController.sessionStorage.put("View", View.BOOK_DELAY);
			return View.BOOK_OVERDUE_CHK;
		}
		MainController.sessionStorage.remove("Check");
		List<Object> param = memberNo();
		List<Map<String, Object>> bookList = bookService.bookDelayList(param);
		if (bookList == null) {
			System.out.println("연장 가능한 도서가 없습니다");
			System.out.println("이전 화면으로 돌아갑니다.");
			return View.BOOK;
		}
		Map<String, Integer> save = new HashMap<>();
		printOverVar();
		System.out.print("  반납일\t");
		printBookIndex();
		printMiddleVar();
		for (Map<String, Object> map : bookList) {
			Date refDate = new Date(((Timestamp) map.get("RETURN_DATE")).getTime());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
			System.out.print("   " + dateFormat.format(refDate) + "\t");
			printBookList(map);
			save.put((String) map.get("BOOK_NO"), 1);
		}
		MainController.sessionStorage.put("delay", save);
		printUnderVar();

		System.out.println("\t\t1.전체 연장\t2.부분 연장\t3.도서조회");
		System.out.println("\t\t\t\t0.홈");
		int sel = ScanUtil.menu();
		switch (sel) {
		case 1:
			return allDelay();
		case 2:
			return View.BOOK_DELAY_PART;
		case 3:
			return View.BOOK;
		case 0:
			return mainMenu();

		default:
			return View.BOOK_DELAY;
		}
	}

	/**
	 * 모든 도서반환
	 * 
	 * @return
	 */
	public View allDelay() {
		List<Object> param = memberNo();
		bookService.bookDelayAll(param);
		System.out.println("모든 도서의 반납일이 7일 연장 되었습니다.");
		MainController.sessionStorage.remove("delay");
		return View.BOOK;
	}

	/**
	 * 부분도서반환 부분의 세션만 삭제, 다 했을 경우엔 경고띄우고 도서로감
	 * 
	 * @return
	 */
	public View Delay() {
		if (!MainController.sessionStorage.containsKey("delay")) {
			System.out.println("연장할 도서가 없습니다");
			System.out.println("이전 화면으로 돌아갑니다.");
			return View.BOOK;
		}
		List<Object> param = memberNo();
		String bookNo = "";
		Map<String, String> map = (Map<String, String>) MainController.sessionStorage.remove("delay");
		while (true) {
			System.out.println("연장시킬 도서번호를 입력해주세요");
			bookNo = ScanUtil.bookNo();
			if (bookNo.equals("0")) {
				return View.BOOK;
			}
			if (map.containsKey(bookNo)) {
				break;
			}
			System.out.println("올바르지 않은 번호입니다.");
		}
		param.add(bookNo);
		bookService.bookDelay(param);
		System.out.println("해당 도서의 반납일이 7일 연장되었습니다");
		map.remove(bookNo, 1);
		if (map != null) {
			MainController.sessionStorage.put("delay", map);
		}
		System.out.println("\t\t1. 다른 책 연장\t2.도서조회");
		System.out.println("\t\t\t\t0.홈");
		int sel = ScanUtil.menu();
		switch (sel) {
		case 1:
			return View.BOOK_DELAY_PART;
		case 2:
			return View.BOOK;
		case 0:
			return mainMenu();
		default:
			return View.BOOK_DELAY_PART;
		}
	}

	public View returnBook() {
		// 도서관 선택 여부
		if (!MainController.sessionStorage.containsKey("library")) {
			noticeLibrarySel();
			return View.LIBRARY;
		}
		List<Object> param = libMemNo();
		System.out.println("현재 도서관에서 반납 가능한 목록");
		List<Map<String, Object>> list = bookService.bookReturnList(param);
		if (list == null) {
			System.out.println("반납할 도서가 없습니다.");
			return View.BOOK;
		}
		Date today = new Date();
		Map<String, Integer> save = new HashMap<>();
		printOverVar();
		System.out.print("  반납예정일\t");
		printBookIndex();
		printMiddleVar();
		Map<String,Object> bookNoSave = new HashMap<String, Object>();
		for (Map<String, Object> map : list) {
			Date refDate = new Date(((Timestamp) map.get("RETURN_DATE")).getTime());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
			if (refDate.before(today)) {
				System.out.print(RED + "   " + dateFormat.format(refDate) + "\t" + END);
				bookNoSave.put((String) map.get("BOOK_NO"), 1);
			} else {
				System.out.print("   " + dateFormat.format(refDate) + "\t");
			}
			printBookList(map);
			save.put((String) map.get("BOOK_NO"), 1);
		}
		MainController.sessionStorage.put("return", save);
		System.out.println("\t\t1.전체 반납\t\t2.부분반납\t\t3.도서조회");
		System.out.println("\t\t\t\t0.홈");
		int sel = ScanUtil.menu();
		switch (sel) {
		case 1:
			MainController.sessionStorage.put("over", bookNoSave);
			return returnBookAll(); // 전체
		case 2:
			MainController.sessionStorage.put("over", bookNoSave);
			return View.BOOK_RETURN_PART; // 부분
		case 3:
			return View.BOOK;
		case 0:
			return mainMenu();
		default:
			return View.BOOK_RETURN;
		}
	}

	/**
	 * @return 전체 반납
	 */
	public View returnBookAll() {
		List<Object> libMemNo = libMemNo();
		// 만약 연체시 member에 연체정보 저장
		bookService.memberOverdueUpdateAll(libMemNo);
		bookService.bookReturnAll(libMemNo);
		List<Object> memParam = new ArrayList<>();
		Map<String, Object> member = (Map<String, Object>) MainController.sessionStorage.get("member");
		int memNo = ((BigDecimal) member.get("MEM_NO")).intValue();
		memParam.add(memNo);

		if (MainController.sessionStorage.containsKey("over")) {	
			MainController.sessionStorage.remove("over");
			String overdueDate = bookService.memberOverdueInfo(memNo);	
			System.out.println("반납일을 넘어서 반납하셨습니다.");
			System.out.println(overdueDate + "부터 대출이 가능합니다.");
		}
		Map<String, String> map = (Map<String, String>) MainController.sessionStorage.remove("return");
		List<String> bookNoList = new ArrayList<>(map.keySet());
		for (String no : bookNoList) {
			bookService.bookRefDate(no);
		}
		MainController.sessionStorage.remove("return");
		return View.BOOK;
	}

	/**
	 * @return 부분 반납
	 */
	public View returnBookPart() {
		if (!MainController.sessionStorage.containsKey("return")) {
			System.out.println("반납할 도서가 없습니다");
			System.out.println("이전 화면으로 돌아갑니다.");
			return View.BOOK;
		}
		Map<String, String> map = (Map<String, String>) MainController.sessionStorage.remove("return");
		Map<String, Object> over = (Map<String, Object>) MainController.sessionStorage.remove("over");		//연체된 책 목록
		String bookNo = "";
		List<Object> param = new ArrayList<>();
		Map<String, Object> member = (Map<String, Object>) MainController.sessionStorage.get("member");
		int memNo = ((BigDecimal) member.get("MEM_NO")).intValue();
		while (true) {
			System.out.println("반납 할 도서번호를 입력해주세요");
			bookNo = ScanUtil.bookNo();
			if (bookNo.equals("0")) {
				return View.BOOK;
			}
			if (map.containsKey(bookNo)) {
				break;
			}
			System.out.println("올바르지 않은 번호입니다.");
		}

		map.remove(bookNo, 1);
		if (map != null) {
			MainController.sessionStorage.put("return", map);
		}
		param.add(bookNo);
		param.add(memNo);
		bookService.memberOverdueUpdate(param);
		bookService.bookReturn(param);
		bookService.bookRefDate(bookNo);
		List<Object> memParam = new ArrayList<>();
		memParam.add(memNo);
		if (over.containsKey(bookNo)) {
			String overdueDate = bookService.memberOverdueInfo(memNo);
			System.out.println("반납일을 넘어서 반납하셨습니다.");
			System.out.println(overdueDate + "부터 대출이 가능합니다.");
		}
		System.out.println("\t\t1. 다른 책 반납\t2.도서조회");
		System.out.println("\t\t\t\t0.홈");
		int sel = ScanUtil.menu();
		switch (sel) {
		case 1:
			return View.BOOK_RETURN_PART;
		case 2:
			return View.BOOK;
		case 0:
			return mainMenu();
		default:
			return View.BOOK_RETURN_PART;
		}
	}
}
