package controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import print.Print;
import service.AdminService;
import service.BookService;
import service.LibraryService;
import util.ScanUtil;
import util.View;

public class AdminController extends Print {
	private static AdminController instance;

	private AdminController() {

	}

	public static AdminController getInstance() {
		if (instance == null) {
			instance = new AdminController();
		}
		return instance;
	}

	AdminService adminService = AdminService.getInstance();
	BookService bookService = BookService.getInstance();
	LibraryService libraryService = LibraryService.getInstance();

	/**
	 * 세션에 sel input값 pageNo pageEnd저장하고 나갈때 삭제할것
	 * 
	 * @param sel 0 : 전체 조회 > ROWNUM
	 * @param sel a : 책 폐기/사용가능한 것들만 조회 > ROWNUM '사용가능' / '폐기'
	 * @param sel b : 도서관 선택 > ROWNUM libraryList
	 * @param sel c : 분류별 선택 > ROWNUM cate
	 * @param sel d : 이름 검색 > ROWNUM name
	 * @param sel e : 출판사 검색 > ROWNUM pub
	 * @param sel f : 발행년도 검색 > ROWNUM year
	 * @param sel 1 : 대출 가능
	 * @param sel 2 : 대출 예약
	 * @param sel 3 : 대출 불가 (예약한 회원정보도 함께 출력할 것 / 연락처 필수)
	 * 
	 */
	public View adminBook() {
		int cut = 5;
		int pageNo = 1;
		int pageEnd = 1;
		List<Object> param = new ArrayList<Object>();
		String sel = "";
		if (!MainController.sessionStorage.containsKey("search")) {
			printMenuOverVar();
			System.out.println(tap+"검색할 도서 정보를 선택해주세요");
			System.out.println(tap+"영어는 중복 선택이 가능하지만, 숫자 대출 상태는 1개만 선택해주세요");
			System.out.println("");
			System.out.println(tap+"a. 사용가능/폐기\tb. 도서관\tc. 카테고리");
			System.out.println(tap+"d. 제목\te.출판사\tf.출판년도\tg.도서번호");
			System.out.println(tap+"1.대출가능\t2.대출예약\t3.대출불가");
			System.out.println(tap+"0.전체 조회");
			System.out.println(tap+"(전체조회를 선택하실 경우 선택여부와 관계없이 전체가 출력됩니다)");
			printMenuVar();
			System.out.println(tap+"예시 ) 사용가능한 한밭도서관에있는 카테고리가 자연과학인 제목이 과학에 들어가고 대출가능한 도서를 검색하고 싶을 때");
			System.out.println(tap+"입력 ▶ abcd1");
			printMenuVar();
			sel = ScanUtil.menuStr();
			if (!sel.contains("0")) {
				if (sel.contains("a")) {
					String state = "";
					while (true) {
						System.out.println(var);
						int input = ScanUtil.nextInt(tap+"1.사용가능\t\t2.폐기");
						System.out.println(var);
						if (input == 1) {
							state = "사용가능";
							break;
						} else if (input == 2) {
							state = "폐기";
							break;
						}
						noticeNotRes();
					}
					param.add(state);
				}
				if (sel.contains("b")) {
					int libNo = 0;
					List<Map<String, Object>> libraryList = libraryService.librarylist();
					Map<Integer, Integer> num = printLibraryList(libraryList);
					while (true) {
						libNo = ScanUtil.nextInt(tap+"도서관 번호   >");
						if (num.containsValue(libNo)) {
							break;
						}
						noticeNotRes();
					}
					param.add(libNo);
				}

				if (sel.contains("c")) {
					int cateNo = 0;
					List<Map<String, Object>> cateList = bookService.cateName();
					printCateName(cateList);
					while (true) {
						cateNo = ScanUtil.nextInt(tap+"분류 번호   >");
						if (cateNo >= 0 && cateNo <= 9) {
							break;
						}
						noticeNotRes();
					}
					param.add(cateNo);
				}
				if (sel.contains("d")) {
					String title = ScanUtil.nextLine(tap+"제목  >");
					param.add(title);
				}
				if (sel.contains("e")) {
					String pub = ScanUtil.nextLine(tap+"출판사   >");
					param.add(pub);
				}
				if (sel.contains("f")) {
					String year = ScanUtil.nextLine(tap+"출판년도   >");
					param.add(year);
				}
				if (sel.contains("g")) {
					String bookNo = ScanUtil.nextLine(tap+"도서번호   >");
					param.add(bookNo);
				}
			}
			MainController.sessionStorage.put("searchSel", sel);
			MainController.sessionStorage.put("search", param);
		} else {
			sel = (String) MainController.sessionStorage.get("searchSel");
			param = (List<Object>) MainController.sessionStorage.get("search");
		}

		// pageEnd
		if (!MainController.sessionStorage.containsKey("pageEnd")) {
			pageEnd = adminService.bookAdminListCount(param, sel) / cut;
			if (pageEnd % cut != 0 || pageEnd == 0) {
				pageEnd++;
			}
			MainController.sessionStorage.put("pageEnd", pageEnd);
		} else {
			pageEnd = (int) MainController.sessionStorage.get("pageEnd");
		}
		// pageNo
		if (MainController.sessionStorage.containsKey("pageNo")) {
			pageNo = (int) MainController.sessionStorage.remove("pageNo");
		}
		int start = (pageNo - 1) * cut;
		int end = (pageNo) * cut;
		param.add(start);
		param.add(end);
		List<Map<String, Object>> list = adminService.bookAdminList(param, sel);
		if (list == null) {
			noticeNotSearch();
			MainController.sessionStorage.remove("search");
			MainController.sessionStorage.remove("searchSel");
			MainController.sessionStorage.remove("pageEnd");
			System.out.println(tap+"1.재검색\t\t2.도서관리\t\t0.홈");
			int select = ScanUtil.menu();
			switch (select) {
			case 1:
				return View.ADMIN_BOOK_LIST;
			case 2:
				return View.ADMIN_BOOK;
			default:
				return View.ADMIN;
			}
		}
		printOverVar();

		System.out.print("   순번\t비고\t");
		printBookIndex();

		printMiddleVar();
		for (Map<String, Object> map : list) {
			System.out.print("  " + map.get("RN") + "\t" + map.get("BOOK_REMARK") + "\t");
			printBookList(map);
		}
		if (pageNo != 1) {
			System.out.print("\t< 이전 페이지");
		} else {
			System.out.print("\t\t");
		}

		System.out.print("\t\t\t\t\t\t" + pageNo + " / " + pageEnd + "\t\t\t\t\t\t");
		if (pageNo != pageEnd) {
			System.out.print("다음 페이지 >");
		}
		System.out.println();
		printUnderVar();
		param.remove(param.size() - 1);
		param.remove(param.size() - 1);
		System.out.println(tap+"1. 페이지 번호 입력\t\t2. 재 검색");
		System.out.println(tap+"3. 도서 상태 수정\t\t4. 도서 추가");
		System.out.println(tap+"0. 홈");
		printMenuVar();
		String select = ScanUtil.menuStr();
		switch (select) {
		case "<":
			if (pageNo > 1) {
				MainController.sessionStorage.put("pageNo", --pageNo);
			}
			MainController.sessionStorage.put("pageNo", pageNo);
			return View.ADMIN_BOOK_LIST;
		case ">":
			if (pageNo < pageEnd) {
				MainController.sessionStorage.put("pageNo", ++pageNo);
			}
			MainController.sessionStorage.put("pageNo", pageNo);
			return View.ADMIN_BOOK_LIST;
		case "1":
			int no = 0;
			do {
				no = ScanUtil.menu();
				if (no >= 1 && no <= pageEnd) {
					MainController.sessionStorage.put("pageNo", no);
					return View.ADMIN_BOOK_LIST;
				}
				noticePageUp();
			} while (true);
		case "2":
			MainController.sessionStorage.remove("search");
			MainController.sessionStorage.remove("searchSel");
			MainController.sessionStorage.remove("pageEnd");
			return View.ADMIN_BOOK_LIST;
		case "3":
			MainController.sessionStorage.remove("search");
			MainController.sessionStorage.remove("searchSel");
			MainController.sessionStorage.remove("pageEnd");
			return View.ADMIN_BOOK_UPDATE;
		case "4":
			MainController.sessionStorage.remove("search");
			MainController.sessionStorage.remove("searchSel");
			MainController.sessionStorage.remove("pageEnd");
			return View.ADMIN_BOOK_INSERT;
		case "0":
			MainController.sessionStorage.remove("search");
			MainController.sessionStorage.remove("searchSel");
			MainController.sessionStorage.remove("pageEnd");
			return View.ADMIN;
		default:
			MainController.sessionStorage.put("pageNo", pageNo);
			noticeNotNo();
			return View.ADMIN_BOOK_LIST;
		}
	}

	public View updateBook() {
		printMenuVar();
		System.out.println(tap+"도서 상태를 변경할 도서 번호를 입력해주세요");
		printMenuVar();
		String bookNo = ScanUtil.bookNo();
		boolean bookChk = bookService.bookChk(bookNo);
		if (!bookChk) {
			noticeNotNo();
			return View.ADMIN_BOOK;
		}
		List<Object> param = new ArrayList<Object>();
		param.add(bookNo);
		Map<String, Object> bookInfo = bookService.bookInformation(param);
		System.out.println(menuU);
		System.out.println(tap+"분류명 : " + bookInfo.get("CATE_NAME"));
		System.out.println(tap+"도서명 : " + bookInfo.get("BOOK_NAME"));
		System.out.println(tap+"작가명 : " + bookInfo.get("BOOK_AUTHOR"));
		System.out.println(tap+"출판사 : " + bookInfo.get("BOOK_PUB"));
		System.out.println(tap+"출판년도 : " + bookInfo.get("BOOK_PUB_YEAR"));
		System.out.println(tap+"도서상태 : " + bookInfo.get("BOOK_REMARK"));
		System.out.println(tap+"도서관이름 : " + bookInfo.get("LIB_NAME"));
		System.out.println(menuD);
		System.out.println();
		System.out.println(tap+"변경할 여부를 선택해주세요");
		System.out.println();
		System.out.println(tap+"1.도서관 이관\t\t2.도서상태 변경 (사용/폐기)");
		int sel = ScanUtil.menu();
		switch (sel) {
		case 1:
			boolean rentChk = bookService.bookRentChk(bookNo);
			if (rentChk) {
				System.out.println(tap+"어디 도서관으로 이동합니까?");
				printMenuVar();
				int libNo = 0;
				List<Map<String, Object>> libraryList = libraryService.librarylist();
				Map<Integer, Integer> num = printLibraryList(libraryList);
				printMenuVar();
				while (true) {
					libNo = ScanUtil.nextInt(tap+"도서관 번호   >  ");
					if (num.containsValue(libNo)) {
						break;
					}
					noticeNotNo();
				}
				List<Object> lib = new ArrayList<Object>();
				lib.add(libNo);
				lib.add(bookNo);
				adminService.bookEscalation(lib);
				System.out.println(var);
				System.out.println(tap+"이관이 완료되었습니다.");
				System.out.println(var);
				return View.ADMIN_BOOK;
			} else {
				noticeNotHaveBook();
				return View.ADMIN_BOOK;
			}

		case 2:
			rentChk = bookService.bookRentChk(bookNo);
			if (rentChk) {
				List<Object> remark = new ArrayList<Object>();
				String state = "";
				while (true) {
					System.out.println(var);
					int input = ScanUtil.nextInt(tap+"1.사용가능\t\t2.폐기");
					System.out.println(var);
					if (input == 1) {
						state = "사용가능";
						break;
					} else if (input == 2) {
						state = "폐기";
						break;
					}
					noticeNotNo();
				}
				remark.add(state);
				remark.add(bookNo);
				adminService.bookStateUpdate(remark);
				System.out.println(var);
				System.out.println(tap+"변경이 완료되었습니다");
				System.out.println(var);
				return View.ADMIN_BOOK;
			} else {
				noticeNotHaveBook();
				return View.ADMIN_BOOK;
			}

		default:
			noticeFirst();
			return View.ADMIN_BOOK;
		}
	}

	public View insertBook() {
		printMenuVar();
		System.out.println(tap+"도서의 분류번호를 선택해주세요");
		printMenuVar();
		int cateNo = 0;
		List<Map<String, Object>> cateList = bookService.cateName();
		printCateName(cateList);
		while (true) {
			cateNo = ScanUtil.nextInt(tap+"분류 번호    >   ");
			if (cateNo >= 0 && cateNo <= 9) {
				break;
			}
			noticeNotNo();
		}
		System.out.println(tap+"도서를 보유할 도서관을 선택해주세요");
		int libNo = 0;
		List<Map<String, Object>> libraryList = libraryService.librarylist();
		Map<Integer, Integer> num = printLibraryList(libraryList);
		while (true) {
			libNo = ScanUtil.nextInt(tap+"도서관 번호    > ");
			if (num.containsValue(libNo)) {
				break;
			}
			noticeNotNo();
		}
		String name = ScanUtil.nextLine(tap+"도서명   > ");
		String author = ScanUtil.nextLine(tap+"작가명   > ");
		String pub = ScanUtil.nextLine(tap+"출판사   > ");
		String year = ScanUtil.nextLine(tap+"출판년도   > ");

		String cateName = adminService.CateName(cateNo);
		String libName = adminService.libName(libNo);
		System.out.println(menuU);
		System.out.println(tap+" 분류명 : " + cateName);
		System.out.println(tap+" 도서명 : " + name);
		System.out.println(tap+" 작가명 : " + author);
		System.out.println(tap+" 출판사 : " + pub);
		System.out.println(tap+" 출판년도 : " + year);
		System.out.println(tap+" 책을 보유할 도서관 명 : " + libName);
		System.out.println(menuD);
		System.out.println();
		printMenuVar();
		System.out.println(tap+"저장하시면 더이상 수정이 불가능 합니다");
		System.out.println(tap+"정말 도서를 추가하시겠습니까");
		printMenuVar();
		while (true) {
			String sel = ScanUtil.nextLine(tap+"Y / N");
			if (sel.equalsIgnoreCase("Y")) {
				List<Object> paraminsert = new ArrayList<Object>();
				paraminsert.add(name);
				paraminsert.add(author);
				paraminsert.add(pub);
				paraminsert.add(year);
				paraminsert.add(libNo);
				adminService.bookInsert(paraminsert, cateNo);
				System.out.println(var);
				System.out.println(tap+"도서가 추가되었습니다.");
				System.out.println(var);
				return View.ADMIN_BOOK;
			} else if (sel.equalsIgnoreCase("N")) {
				System.out.println(var);
				System.out.println(tap+"도서 추가를 취소했습니다.");
				System.out.println(var);
				System.out.println();
				printMenuVar();
				System.out.println(tap+"1. 다시 입력\t\t2.돌아가기\t\t0.홈");
				printMenuVar();
				int seltwo = ScanUtil.menu();
				switch (seltwo) {
				case 1:
					return View.ADMIN_BOOK_INSERT;
				case 2:
					return View.ADMIN_BOOK;
				case 3:
					return View.ADMIN;

				default:
					return View.ADMIN_BOOK;
				}
			}
			noticeNotNo();
		}
	}

// 회원

	public View memberList() {
		int cut = 5;
		int pageNo = 1;
		int pageEnd = 1;
		List<Object> param = new ArrayList<Object>();
		int sel = 0;
		if (!MainController.sessionStorage.containsKey("searchSel")) {
			printMenuOverVar();
			System.out.println(tap+"\t검색할 도서 정보를 선택해주세요");
			System.out.println("");
			System.out.println(tap+"1.대출도서 보유 회원\t2.예약도서 보유 회원\t3.대출가능한 예약도서 보유 회원");
			System.out.println(tap+"4.연체도서 보유 회원\t5.대출불가능한 회원");
			System.out.println(tap+"0.전체 조회");
			printMenuVar();
			sel = ScanUtil.menu();

			MainController.sessionStorage.put("searchSel", sel);
		} else {
			sel = (int) MainController.sessionStorage.get("searchSel");
		}

		// pageEnd
		if (!MainController.sessionStorage.containsKey("pageEnd")) {
			if (sel == 0) {
				pageEnd = adminService.memberListCount() / cut;
			} else {
				pageEnd = adminService.memberSearchListCount(sel) / cut;
			}
			if (pageEnd % cut != 0 || pageEnd == 0) {
				pageEnd++;
			}
			MainController.sessionStorage.put("pageEnd", pageEnd);
		} else {
			pageEnd = (int) MainController.sessionStorage.get("pageEnd");
		}
		// pageNo
		if (MainController.sessionStorage.containsKey("pageNo")) {
			pageNo = (int) MainController.sessionStorage.remove("pageNo");
		}
		int start = (pageNo - 1) * cut;
		int end = (pageNo) * cut;
		param.add(start);
		param.add(end);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (sel == 0) {
			list = adminService.memberList(param);
		} else {
			list = adminService.memberSearchList(param, sel);
		}

		if (list == null) {
			noticeNotSearch();
			MainController.sessionStorage.remove("searchSel");
			MainController.sessionStorage.remove("pageEnd");
			System.out.println(tap+"1.재검색\t\t3.회원관리\t\t0.홈");
			int select = ScanUtil.menu();
			switch (select) {
			case 1:
				return View.ADMIN_MEMBER_SEARCH;
			case 2:
				return View.ADMIN_MEMBER;
			default:
				return View.ADMIN_MEMBER;
			}
		}
		printOverVar();

		printMemberIndex();

		printMiddleVar();
		for (Map<String, Object> map : list) {
			printMemberList(map);
		}
		if (pageNo != 1) {
			System.out.print("\t< 이전 페이지");
		} else {
			System.out.print("\t\t");
		}

		System.out.print("\t\t\t\t\t\t" + pageNo + " / " + pageEnd + "\t\t\t\t\t\t");
		if (pageNo != pageEnd) {
			System.out.print("다음 페이지 >");
		}
		System.out.println();
		printUnderVar();
		System.out.print(tap+"1. 페이지 번호 입력\t\t2. 재 검색\t\t3. 회원관리");
		if (MainController.sessionStorage.containsKey("manager")) {
			System.out.println("\t\t4. 관리자 임명");
		} else {System.out.println();}
		System.out.println(tap+"\t\t0. 홈");
		String select = ScanUtil.menuStr();
		switch (select) {
		case "<":
			if (pageNo > 1) {
				MainController.sessionStorage.put("pageNo", --pageNo);
			}
			MainController.sessionStorage.put("pageNo", pageNo);
			return View.ADMIN_MEMBER_SEARCH;
		case ">":
			if (pageNo < pageEnd) {
				MainController.sessionStorage.put("pageNo", ++pageNo);
			}
			MainController.sessionStorage.put("pageNo", pageNo);
			return View.ADMIN_MEMBER_SEARCH;
		case "1":
			int no = 0;
			do {
				no = ScanUtil.menu();
				if (no >= 1 && no <= pageEnd) {
					MainController.sessionStorage.put("pageNo", no);
					return View.ADMIN_MEMBER_SEARCH;
				}
				noticePageUp();
			} while (true);
		case "2":
			MainController.sessionStorage.remove("searchSel");
			MainController.sessionStorage.remove("pageEnd");
			return View.ADMIN_MEMBER_SEARCH;
		case "3":
			MainController.sessionStorage.remove("searchSel");
			MainController.sessionStorage.remove("pageEnd");
			return View.ADMIN_MEMBER;
		case "4":
			if (MainController.sessionStorage.containsKey("manager")) {
				MainController.sessionStorage.remove("searchSel");
				MainController.sessionStorage.remove("pageEnd");
				return View.ADMIN_APPOINT;
			}
			MainController.sessionStorage.put("pageNo", pageNo);
			noticeNotNo();
			return View.ADMIN_MEMBER_SEARCH;
		case "0":
			MainController.sessionStorage.remove("searchSel");
			MainController.sessionStorage.remove("pageEnd");
			return View.ADMIN;
		default:
			MainController.sessionStorage.put("pageNo", pageNo);
			noticeNotNo();
			return View.ADMIN_MEMBER_SEARCH;
		}
	}

	public View appoint() {
		System.out.println(tap+"변경할 회원을 선택해주세요");
		int memNo = ScanUtil.nextInt(tap+"회원 번호   > ");
		boolean memberChk = adminService.memberChk(memNo);
		if (!memberChk) {
			noticeNotMember();
			return View.ADMIN_MEMBER;
		}
		Map<String, Object> map = adminService.memberInfo(memNo);
		int no = ((BigDecimal) map.get("ADMIN_NO")).intValue();
		if (no == 3) {
			noticeNotRight();
			return View.ADMIN_MEMBER;
		}

		String id = (String) map.get("MEM_ID");
		id = id.substring(0, id.length() - 3);
		id += "***";

		String admin = "";
		if (no == 1) {
			admin = "일반회원";
		} else if (no == 2) {
			admin = "관리자";
		}

		System.out.println("회원명" + map.get("MEM_NAME"));
		System.out.println("아이디" + id);
		System.out.println("전화번호" + map.get("MEM_TELNO"));
		System.out.println("관리자 여부" + admin);

		if (no == 1) {
			System.out.println(var);
			System.out.println(tap+"관리자로 임명하시겠습니까?");
			System.out.println(var);
			String select = ScanUtil.nextLine("Y / N");
			if (select.equalsIgnoreCase("Y")) {
				List<Object> app = new ArrayList<Object>();
				app.add(2);
				app.add(memNo);
				adminService.adminUpdate(app);
				System.out.println(var);
				System.out.println(tap+"관리자로 임명되었습니다.");
				System.out.println(var);
				return View.ADMIN_MEMBER;
			}
			noticeCancel();
			return View.ADMIN_MEMBER;
		} else {
			System.out.println(var);
			System.out.println(tap+"관리자를 삭제하시겠습니까?");
			System.out.println(var);
			String select = ScanUtil.nextLine("Y / N");
			if (select.equalsIgnoreCase("Y")) {
				List<Object> app = new ArrayList<Object>();
				app.add(1);
				app.add(memNo);
				adminService.adminUpdate(app);
				System.out.println(var);
				System.out.println("선택한 회원이 일반회원으로 전환 되었습니다.");
				System.out.println(var);
				return View.ADMIN_MEMBER;
			}
			System.out.println(var);
			System.out.println(tap+"취소되었습니다 회원관리 페이지로 돌아갑니다");
			System.out.println(var);
			return View.ADMIN_MEMBER;
		}

	}

	public View overdueList() {
		int cut = 5;
		int pageNo = 1;
		int pageEnd = 1;

		// pageEnd
		if (!MainController.sessionStorage.containsKey("pageEnd")) {

			pageEnd = adminService.overdueListCount() / cut;

			if (pageEnd % cut != 0 || pageEnd == 0) {
				pageEnd++;
			}
			MainController.sessionStorage.put("pageEnd", pageEnd);
		} else {
			pageEnd = (int) MainController.sessionStorage.get("pageEnd");
		}
		// pageNo
		if (MainController.sessionStorage.containsKey("pageNo")) {
			pageNo = (int) MainController.sessionStorage.remove("pageNo");
		}
		int start = (pageNo - 1) * cut;
		int end = (pageNo) * cut;
		List<Map<String, Object>> list = adminService.overdueList(start, end);

		if (list == null) {
			noticeNoOverdue();
			MainController.sessionStorage.remove("searchSel");
			MainController.sessionStorage.remove("pageEnd");

			return View.ADMIN_MEMBER;

		}
		printOverVar();

		System.out.print("     반납일\t\t│회원명\t│아이디\t│전화번호\t\t");
		printBookIndex();
		printMiddleVar();
		for (Map<String, Object> map : list) {
			Date date = new Date(((Timestamp) map.get("RETURN_DATE")).getTime());
			String id = (String) map.get("MEM_ID");
			id = id.substring(0, id.length() - 3);
			id += "***";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
			System.out.print("   " + RED + dateFormat.format(date) + END + "\t│" + map.get("MEM_NAME") + "\t│" + id
					+ "\t│" + map.get("MEM_TELNO") + "\t");
			printBookList(map);
		}
		if (pageNo != 1) {
			System.out.print("\t< 이전 페이지");
		} else {
			System.out.print("\t\t");
		}

		System.out.print("\t\t\t\t\t\t" + pageNo + " / " + pageEnd + "\t\t\t\t\t\t");
		if (pageNo != pageEnd) {
			System.out.print("다음 페이지 >");
		}
		System.out.println();
		printUnderVar();
		System.out.println(tap+"1. 페이지 번호 입력\t\t2. 회원관리\t\t0. 홈");
		String select = ScanUtil.menuStr();
		switch (select) {
		case "<":
			if (pageNo > 1) {
				MainController.sessionStorage.put("pageNo", --pageNo);
			}
			MainController.sessionStorage.put("pageNo", pageNo);
			return View.ADMIN_OVERDUE_LIST;
		case ">":
			if (pageNo < pageEnd) {
				MainController.sessionStorage.put("pageNo", ++pageNo);
			}
			MainController.sessionStorage.put("pageNo", pageNo);
			return View.ADMIN_OVERDUE_LIST;
		case "1":
			int no = 0;
			do {
				no = ScanUtil.nextInt("번호 입력 : ");
				if (no >= 1 && no <= pageEnd) {
					MainController.sessionStorage.put("pageNo", no);
					return View.ADMIN_OVERDUE_LIST;
				}
				noticePageUp();
			} while (true);
		case "2":
			MainController.sessionStorage.remove("searchSel");
			MainController.sessionStorage.remove("pageEnd");
			return View.ADMIN_MEMBER;
		case "0":
			MainController.sessionStorage.remove("searchSel");
			MainController.sessionStorage.remove("pageEnd");
			return View.ADMIN;
		default:
			MainController.sessionStorage.put("pageNo", pageNo);
			System.out.println("잘못된 입력입니다.");
			return View.ADMIN_OVERDUE_LIST;
		}
	}

}
