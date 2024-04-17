package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import oracle.net.ns.SessionAtts;
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
			System.out.println("\t\t\t검색할 도서 정보를 선택해주세요");
			System.out.println("\t\t\t영어는 중복 선택이 가능하지만, 숫자 대출 상태는 1개만 선택해주세요");
			System.out.println("");
			System.out.println("\t\ta. 사용가능/폐기\tb. 도서관\tc. 카테고리");
			System.out.println("\t\td. 제목\te.출판사\tf.출판년도\tg.도서번호");
			System.out.println("\t\t1.대출가능\t2.대출예약\t3.대출불가");
			System.out.println("\t\t0.전체 조회");
			System.out.println("\t\t(전체조회를 선택하실 경우 선택여부와 관계없이 전체가 출력됩니다)");
			printMenuVar();
			System.out.println("예시 ) 사용가능한 한밭도서관에있는 카테고리가 자연과학인 제목이 과학에 들어가고 대출가능한 도서를 검색하고 싶을 때");
			System.out.println("입력 ▶ abcd1");
			printMenuVar();
			System.out.println("\t\t\t");
			printMenuVar();
			sel = ScanUtil.nextLine("입력 : ");
			if (!sel.contains("0")) {
				if (sel.contains("a")) {
					String state = "";
					while (true) {
						int input = ScanUtil.nextInt("\t\t1.사용가능\t\t2.폐기");
						if (input == 1) {
							state = "사용가능";
							break;
						} else if (input == 2) {
							state = "폐기";
							break;
						}
						System.out.println("잘못된 입력입니다");
					}
					param.add(state);
				}
				if (sel.contains("b")) {
					int libNo = 0;
					List<Map<String, Object>> libraryList = libraryService.librarylist();
					Map<Integer, Integer> num = printLibraryList(libraryList);
					while (true) {
						libNo = ScanUtil.nextInt("도서관 번호 : ");
						if (num.containsValue(libNo)) {
							break;
						}
						System.out.println("잘못된 입력입니다");
					}
					param.add(libNo);
				}

				if (sel.contains("c")) {
					int cateNo = 0;
					List<Map<String, Object>> cateList = bookService.cateName();
					printCateName(cateList);
					while (true) {
						cateNo = ScanUtil.nextInt("분류 번호 : ");
						if (cateNo >= 0 && cateNo <= 9) {
							break;
						}
						System.out.println("잘못된 입력입니다");
					}
					param.add(cateNo);
				}
				if (sel.contains("d")) {
					String title = ScanUtil.nextLine("제목 : ");
					param.add(title);
				}
				if (sel.contains("e")) {
					String pub = ScanUtil.nextLine("출판사 : ");
					param.add(pub);
				}
				if (sel.contains("f")) {
					String year = ScanUtil.nextLine("출판년도 : ");
					param.add(year);
				}
				if (sel.contains("g")) {
					String bookNo = ScanUtil.nextLine("도서번호 : ");
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
			System.out.println("검색 결과가 없습니다.");
			MainController.sessionStorage.remove("search");
			MainController.sessionStorage.remove("searchSel");
			MainController.sessionStorage.remove("pageEnd");
			System.out.println("1.재검색\t\t2.도서관리\t\t0.홈");
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
		System.out.println("1. 페이지 번호 입력");
		System.out.println("2. 재 검색");
		System.out.println("3. 도서 상태 수정");
		System.out.println("4. 도서 추가");
		System.out.println("0. 홈");
		String select = ScanUtil.nextLine("메뉴 : ");
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
				no = ScanUtil.nextInt("번호 입력 : ");
				if (no >= 1 && no <= pageEnd) {
					MainController.sessionStorage.put("pageNo", no);
					return View.ADMIN_BOOK_LIST;
				}
				System.out.println("페이지를 벗어났습니다.");
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
			System.out.println("잘못된 입력입니다.");
			return View.ADMIN_BOOK_LIST;
		}
	}

	public View updateBook() {
		System.out.println("도서 상태를 변경할 도서 번호를 입력해주세요");
		String bookNo = ScanUtil.bookNo();
		boolean bookChk = bookService.bookChk(bookNo);
		if (!bookChk) {
			System.out.println("없는 도서 번호입니다.");
			System.out.println("다시 확인하고 시도해주세요.");
			return View.ADMIN_BOOK;
		}
		List<Object> param = new ArrayList<Object>();
		param.add(bookNo);
		Map<String, Object> bookInfo = bookService.bookInformation(param);
		System.out.println("분류명 : " + bookInfo.get("CATE_NAME"));
		System.out.println("도서명 : " + bookInfo.get("BOOK_NAME"));
		System.out.println("작가명 : " + bookInfo.get("BOOK_AUTHOR"));
		System.out.println("출판사 : " + bookInfo.get("BOOK_PUB"));
		System.out.println("출판년도 : " + bookInfo.get("BOOK_PUB_YEAR"));
		System.out.println("도서상태 : " + bookInfo.get("BOOK_REMARK"));
		System.out.println("도서관이름 : " + bookInfo.get("LIB_NAME"));
		System.out.println("변경할 여부를 선택해주세요");
		System.out.println("\t\t1.도서관 이관\t\t2.도서상태 변경 (사용/폐기)");
		int sel = ScanUtil.menu();
		switch (sel) {
		case 1:
			boolean rentChk = bookService.bookRentChk(bookNo);
			if (rentChk) {
				System.out.println("어디 도서관으로 이동합니까?");
				int libNo = 0;
				List<Map<String, Object>> libraryList = libraryService.librarylist();
				Map<Integer, Integer> num = printLibraryList(libraryList);
				while (true) {
					libNo = ScanUtil.nextInt("도서관 번호 : ");
					if (num.containsValue(libNo)) {
						break;
					}
					System.out.println("잘못된 입력입니다");
				}
				List<Object> lib = new ArrayList<Object>();
				lib.add(libNo);
				lib.add(bookNo);
				adminService.bookEscalation(lib);
				System.out.println("이관이 완료되었습니다.");
				return View.ADMIN_BOOK;
			} else {
				System.out.println("도서관에 책이 없으므로 이관할 수 없습니다.");
				System.out.println("대출가능한 상태에서 시도해주세요");
				return View.ADMIN_BOOK;
			}

		case 2:
			rentChk = bookService.bookRentChk(bookNo);
			if (rentChk) {
			List<Object> remark = new ArrayList<Object>();
			String state = "";
			while (true) {
				int input = ScanUtil.nextInt("\t\t1.사용가능\t\t2.폐기");
				if (input == 1) {
					state = "사용가능";
					break;
				} else if (input == 2) {
					state = "폐기";
					break;
				}
				System.out.println("잘못된 입력입니다");
			}
			remark.add(state);
			remark.add(bookNo);
			adminService.bookStateUpdate(remark);
			System.out.println("변경이 완료되었습니다");
			return View.ADMIN_BOOK;
			} else {
				System.out.println("대출중 상태이므로 상태를 변경할 수 없습니다.");
				System.out.println("대출가능한 상태에서 시도해주세요");
				return View.ADMIN_BOOK;
			}

		default:
			System.out.println("잘못된 선택입니다.");
			System.out.println("처음으로 돌아갑니다.");
			return View.ADMIN_BOOK;
		}
	}

	public View insertBook() {
		System.out.println("도서의 분류번호를 선택해주세요");
		int cateNo = 0;
		List<Map<String, Object>> cateList = bookService.cateName();
		printCateName(cateList);
		while (true) {
			cateNo = ScanUtil.nextInt("분류 번호 : ");
			if (cateNo >= 0 && cateNo <= 9) {
				break;
			}
			System.out.println("잘못된 입력입니다");
		}
		System.out.println("도서를 보유할 도서관을 선택해주세요");
		int libNo = 0;
		List<Map<String, Object>> libraryList = libraryService.librarylist();
		Map<Integer, Integer> num = printLibraryList(libraryList);
		while (true) {
			libNo = ScanUtil.nextInt("도서관 번호 : ");
			if (num.containsValue(libNo)) {
				break;
			}
			System.out.println("잘못된 입력입니다");
		}
		String name = ScanUtil.nextLine("도서명 : ");
		String author = ScanUtil.nextLine("작가명 : ");
		String pub = ScanUtil.nextLine("출판사 : ");
		String year = ScanUtil.nextLine("출판년도 : ");
		
		String cateName = adminService.CateName(cateNo);
		String libName = adminService.libName(libNo);
		
		System.out.println(" 분류명 : "+cateName);
		System.out.println(" 도서명 : "+name);
		System.out.println(" 작가명 : "+author);
		System.out.println(" 출판사 : "+pub);
		System.out.println(" 출판년도 : "+year);
		System.out.println(" 책을 보유할 도서관 명 : "+libName);
		System.out.println();
		System.out.println("저장하시면 더이상 수정이 불가능 합니다");
		System.out.println("정말 도서를 추가하시겠습니까");
		while(true) {
		String sel = ScanUtil.nextLine("Y / N");
		if(sel.equalsIgnoreCase("Y")) {
			List<Object> paraminsert = new ArrayList<Object>();
			paraminsert.add(name);
			paraminsert.add(author);
			paraminsert.add(pub);
			paraminsert.add(year);
			paraminsert.add(libNo);
			adminService.bookInsert(paraminsert, cateNo);
			System.out.println("도서가 추가되었습니다.");
			return View.ADMIN_BOOK;
		} else if(sel.equalsIgnoreCase("N")) {
			System.out.println("도서 추가를 취소했습니다.");
			System.out.println("\t\t1. 다시 입력\t\t2.돌아가기\t\t0.홈");
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
		System.out.println("잘못된 입력입니다.");
		}
		
		
	}
}
