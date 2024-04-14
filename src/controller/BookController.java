package controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import print.Print;
import service.BookService;
import util.ScanUtil;
import util.View;

public class BookController extends Print {
	private static BookController instance;
	
	private BookController() {
		
	}
	
	public static BookController getInstance() {
		if(instance == null) {	
			instance = new BookController();	
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

	public void bookTopAllList() {
		List<Map<String, Object>> list = bookService.bookTopAllList();
		printVar();
		System.out.print("순위\t");
		printBookIndex();
		printVar();
		int count = 0;
		for (Map<String, Object> map : list) {
			count++;
			System.out.print(count + "위\t");
			printBookList(map);
		}
		printVar();
	}

	public void bookTopList() {
		List<Object> param = libraryNo();
		List<Map<String, Object>> list = bookService.bookTopList(param);
		printVar();
		System.out.print("순위\t");
		printBookIndex();
		printVar();
		int count = 0;
		for (Map<String, Object> map : list) {
			count++;
			System.out.print(count + "위\t");
			printBookList(map);
		}
		printVar();
	}
	
	private View book() {
		bookTopAllList();
		return null;
	}

	/**
	 * 책의 전체 list를 볼 수 있는 곳 (선택한 도서관/ 전체도서관)
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

		printVar();

		printBookIndex();

		printVar();
		for (

		Map<String, Object> map : list) {
			printBookList(map);
		}

		printVar();
		if (pageNo != 1)

		{
			System.out.print("< 이전 페이지");
		}
		System.out.print("\t\t\t\t\t\t" + pageNo + " / " + pageEnd + "\t\t\t\t\t\t");
		if (pageNo != pageEnd) {
			System.out.print("다음 페이지 >");
		}
		System.out.println();
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
	
}
