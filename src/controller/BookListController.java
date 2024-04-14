package controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import print.Print;
import service.BookService;
import util.ScanUtil;
import util.View;

public class BookListController extends Print {
	private static BookListController instance;
	
	private BookListController() {
		
	}
	
	public static BookListController getInstance() {
		if(instance == null) {	
			instance = new BookListController();	
		}
		return instance;
	}
	BookService bookService = BookService.getInstance();

	/**
	 * @return 도서관 번호저장된 List<Object> param
	 */
	public List<Object> libraryNo() {
		List<Object> param = new ArrayList<>();
		Map<String, Object> map = (Map<String, Object>) MainController.sessionStorage.get("library");
		String no = "" + ((BigDecimal) map.get("LIB_NO")).intValue();
		param.add(no);
		return param;
	}
	
	/**
	 *  책 순위
	 *  만약에 도서관의 정보가 있을 땐 현재 도서관의 순위
	 */
	public void bookTopList() {
		List<Object> param = new ArrayList<Object>();
		if (MainController.sessionStorage.containsKey("library")) {
			param = libraryNo();
		}
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if (MainController.sessionStorage.containsKey("library")) {
			list = bookService.bookTopList(param);
		} else {
			list = bookService.bookTopAllList();
		}
		printOverVar();
		System.out.print("   순위\t");
		printBookIndex();
		printMiddleVar();
		int count = 0;
		for (Map<String, Object> map : list) {
			count++;
			System.out.print("   "+count + " 위\t");
			printBookList(map);
		}
		printUnderVar();
	}
	
	/**
	 * 책의 전체 list (선택한 도서관/ 전체 도서관)
	 * 
	 * @return
	 */
	public View bookList() {
		int cut = 5;
		int pageNo = 1;
		int pageEnd = 0;
		List<Object> param = new ArrayList<Object>();
		if (MainController.sessionStorage.containsKey("library")) {
			param = libraryNo();
		}
		if (!MainController.sessionStorage.containsKey("pageEnd")) {
			if (MainController.sessionStorage.containsKey("library")) {
				param = libraryNo();
				pageEnd = bookService.bookListCount(param) / cut;
			} else {
				pageEnd = bookService.bookAllListCount() / cut;
			}
			if (pageEnd % cut != 0) {
				pageEnd++;
			}
			MainController.sessionStorage.put("pageEnd", pageEnd);
		} else {
			pageEnd = (int) MainController.sessionStorage.get("pageEnd");
		}

		if (MainController.sessionStorage.containsKey("pageNo")) {
			pageNo = (int) MainController.sessionStorage.remove("pageNo");
		}

		int start = (pageNo - 1) * cut;
		int end = (pageNo) * cut;
		param.add(start);
		param.add(end);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (MainController.sessionStorage.containsKey("library")) {
			list = bookService.bookList(param);
		} else {
			list = bookService.bookAllList(param);
		}

		printOverVar();

		System.out.print("   순번\t");
		printBookIndex();

		printMiddleVar();
		for (Map<String, Object> map : list) {			
			System.out.print("  "+map.get("RN")+"\t");
			printBookList(map);
		}

		if (pageNo != 1){
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
		System.out.println("1. 페이지 번호 입력");
		System.out.println("2. 대출 하기");
		System.out.println("3. 도서정보");
		String sel = ScanUtil.nextLine("메뉴 : ");
		switch (sel) {
		case "<":
			if (pageNo > 1) {
				MainController.sessionStorage.put("pageNo", --pageNo);
			}
			MainController.sessionStorage.put("pageNo", pageNo);
			return View.BOOK_LIST;
		case ">":
			if (pageNo < pageEnd) {
				MainController.sessionStorage.put("pageNo", ++pageNo);
			}
			MainController.sessionStorage.put("pageNo", pageNo);
			return View.BOOK_LIST;
		case "1":
			int no = 0;
			do {
				no = ScanUtil.nextInt("번호 입력 : ");
				if (no >= 1 && no <= pageEnd) {
					MainController.sessionStorage.put("pageNo", no);
					return View.BOOK_LIST;
				}
				System.out.println("페이지를 벗어났습니다.");
			} while (true);
		case "2":
			if (!MainController.sessionStorage.containsKey("library")) {
				System.out.println("전체 도서 검색은 대출이 불가능합니다.");
				System.out.println("도서관 선택창으로 돌아갑니다.");
				return View.LIBRARY;
			}
			return View.BOOK_RENT;
		case "3":
			return View.BOOK;
		default:
			MainController.sessionStorage.put("pageNo", pageNo);
			System.out.println("잘못된 입력입니다.");
			return View.BOOK_LIST;
		}
	}
	
	/**책의 카테고리별 list(선택한 도서관 / 전체 도서관)
	 * @return
	 */
	public View bookCateList() {
		int cut = 5;
		int pageNo = 1;
		int pageEnd = 0;
		List<Object> param = new ArrayList<Object>();
		if (MainController.sessionStorage.containsKey("library")) {
			param = libraryNo();
		}
		int cateNo = 0;
		
		// cate 확인
		if(!MainController.sessionStorage.containsKey("cate")) {
		List<Map<String, Object>> cate = bookService.cateName();
			printCateName(cate);
			while(true) {
				cateNo=ScanUtil.nextInt("번호 : ");
				if(cateNo>=0&&cateNo<=9) {
				break;
				}
				System.out.println("유효하지 않은 번호입니다.");
			}
			MainController.sessionStorage.put("cate", cateNo);
		} else {
			cateNo=(int) MainController.sessionStorage.get("cate");
		}
			param.add(cateNo);
			//pageEnd확인
		if (!MainController.sessionStorage.containsKey("pageEnd")) {
			if (MainController.sessionStorage.containsKey("library")) {
				pageEnd = bookService.bookCateListCount(param) / cut;
			} else {
				pageEnd = bookService.bookCateAllListCount(param) / cut;
			}
			if (pageEnd % cut != 0) {
				pageEnd++;
			}
			MainController.sessionStorage.put("pageEnd", pageEnd);
		} else {
			pageEnd = (int) MainController.sessionStorage.get("pageEnd");
		}

			//pageNo 확인
		if (MainController.sessionStorage.containsKey("pageNo")) {
			pageNo = (int) MainController.sessionStorage.remove("pageNo");
		}
		int start = (pageNo - 1) * cut;
		int end = (pageNo) * cut;
		param.add(start);
		param.add(end);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (MainController.sessionStorage.containsKey("library")) {
			list = bookService.bookCateList(param);
		} else {
			list = bookService.bookCateAllList(param);
		}
		printOverVar();

		System.out.print("   순번\t");
		printBookIndex();

		printMiddleVar();
		for (Map<String, Object> map : list) {			
			System.out.print("  "+map.get("RN")+"\t");
			printBookList(map);
		}

		if (pageNo != 1){
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
		System.out.println("1. 페이지 번호 입력");
		System.out.println("2. 대출 하기");
		System.out.println("3. 도서정보");
		String sel = ScanUtil.nextLine("메뉴 : ");
		switch (sel) {
		case "<":
			if (pageNo > 1) {
				MainController.sessionStorage.put("pageNo", --pageNo);
			}
			MainController.sessionStorage.put("pageNo", pageNo);
			return View.BOOK_CATE_LIST;
		case ">":
			if (pageNo < pageEnd) {
				MainController.sessionStorage.put("pageNo", ++pageNo);
			}
			MainController.sessionStorage.put("pageNo", pageNo);
			return View.BOOK_CATE_LIST;
		case "1":
			int no = 0;
			do {
				no = ScanUtil.nextInt("번호 입력 : ");
				if (no >= 1 && no <= pageEnd) {
					MainController.sessionStorage.put("pageNo", no);
					return View.BOOK_CATE_LIST;
				}
				System.out.println("페이지를 벗어났습니다.");
			} while (true);
		case "2":
			if (!MainController.sessionStorage.containsKey("library")) {
				System.out.println("전체 도서 검색은 대출이 불가능합니다.");
				System.out.println("도서관 선택창으로 돌아갑니다.");
				return View.LIBRARY;
			}
			return View.BOOK_RENT;
		case "3":
			return View.BOOK;
		default:
			MainController.sessionStorage.put("pageNo", pageNo);
			System.out.println("잘못된 입력입니다.");
			return View.BOOK_CATE_LIST;
		}
	

	}

	public View bookSearchList() {
		int cut = 5;
		int pageNo = 1;
		int pageEnd = 0;
		List<Object> param = new ArrayList<Object>();
		if (MainController.sessionStorage.containsKey("library")) {
			param = libraryNo();
		}
		int sel = 0;
		if(!MainController.sessionStorage.containsKey("search")) {
		printMenuOverVar();
		System.out.println("\t\t\t검색할 도서 정보를 선택해주세요");
		printMenuVar();
		System.out.println("\t\t1.제목\t\t2.작가\t\t3.출판사");
		printMenuVar();
		sel = ScanUtil.nextInt("번호 : ");
		if(sel==1) {
			String name = ScanUtil.nextLine("제목 검색 : ");
			param.add(name);
			MainController.sessionStorage.put("search", name);
		}
		if(sel==2) {
			String author = ScanUtil.nextLine("작가 검색 : ");
			param.add(author);
			MainController.sessionStorage.put("search", author);
		}
		if(sel==3) {
			String pub = ScanUtil.nextLine("출판사 검색 : ");
			param.add(pub);
			MainController.sessionStorage.put("search", pub);
		}
		MainController.sessionStorage.put("searchSel", sel);
		} else {
			String search = (String) MainController.sessionStorage.get("search");
			param.add(search);
			sel = (int) MainController.sessionStorage.get("searchSel");
		}
		// pageEnd
		if (!MainController.sessionStorage.containsKey("pageEnd")) {
			if (MainController.sessionStorage.containsKey("library")) {
				pageEnd = bookService.bookSearchListCount(param, sel) / cut;
			} else {
				pageEnd = bookService.bookSearchAllListCount(param, sel) / cut;
			}
			if (pageEnd % cut != 0) {
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
		if (MainController.sessionStorage.containsKey("library")) {
			list = bookService.bookSearchList(param, sel);
		} else {
			list = bookService.bookSearchAllList(param, sel);
		}
		printOverVar();

		System.out.print("   순번\t");
		printBookIndex();

		printMiddleVar();
		for (Map<String, Object> map : list) {			
			System.out.print("  "+map.get("RN")+"\t");
			printBookList(map);
		}

		if (pageNo != 1){
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
		System.out.println("1. 페이지 번호 입력");
		System.out.println("2. 대출 하기");
		System.out.println("3. 도서정보");
		String select = ScanUtil.nextLine("메뉴 : ");
		switch (select) {
		case "<":
			if (pageNo > 1) {
				MainController.sessionStorage.put("pageNo", --pageNo);
			}
			MainController.sessionStorage.put("pageNo", pageNo);
			return View.BOOK_SEARCH_LIST;
		case ">":
			if (pageNo < pageEnd) {
				MainController.sessionStorage.put("pageNo", ++pageNo);
			}
			MainController.sessionStorage.put("pageNo", pageNo);
			return View.BOOK_SEARCH_LIST;
		case "1":
			int no = 0;
			do {
				no = ScanUtil.nextInt("번호 입력 : ");
				if (no >= 1 && no <= pageEnd) {
					MainController.sessionStorage.put("pageNo", no);
					return View.BOOK_CATE_LIST;
				}
				System.out.println("페이지를 벗어났습니다.");
			} while (true);
		case "2":
			if (!MainController.sessionStorage.containsKey("library")) {
				System.out.println("전체 도서 검색은 대출이 불가능합니다.");
				System.out.println("도서관 선택창으로 돌아갑니다.");
				return View.LIBRARY;
			}
			return View.BOOK_RENT;
		case "3":
			return View.BOOK;
		default:
			MainController.sessionStorage.put("pageNo", pageNo);
			System.out.println("잘못된 입력입니다.");
			return View.BOOK_SEARCH_LIST;
		}
	

		
	}
}
