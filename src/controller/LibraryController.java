package controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import print.Print;
import service.LibraryService;
import util.ScanUtil;
import util.View;

public class LibraryController extends Print {
	private static LibraryController instance;

	private LibraryController() {

	}

	public static LibraryController getInstance() {
		if (instance == null) {
			instance = new LibraryController();
		}
		return instance;
	}

	LibraryService libraryService = LibraryService.getInstance();

	public View mainMenu() {
		if(MainController.sessionStorage.containsKey("member")&&MainController.sessionStorage.containsKey("library")) {
			return View.MAIN_ALL;
		}
		if(MainController.sessionStorage.containsKey("member")) {
			return View.MAIN_MEMBER;
		}
		if(MainController.sessionStorage.containsKey("library")) {
			return View.MAIN_LIBRARY;
		}
		return View.MAIN;
	}
	
	public View localLibrary() {
		List<Map<String, Object>> list = libraryService.localName();
		Map<Integer, Integer> map = printLocalList(list);
		System.out.println("0. 이전 화면");
		do {
			int locNo = ScanUtil.nextInt("선택 : ");
			if (locNo == 0) {
				return View.LIBRARY;
			}
			if (map.containsValue(locNo)) {
				libraryService.librarySel(locNo);
				MainController.sessionStorage.put("Location", locNo);
				return View.LIBRARY_LIST;
			} 
			System.out.println("유효하지 않은 숫자입니다.");
		} while (true);
	}

	public View librarylist() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (MainController.sessionStorage.containsKey("Location")) {
			List<Object> param = new ArrayList<Object>();
			param.add((int) MainController.sessionStorage.remove("Location"));
			list = libraryService.localLibraryList(param);
		} else {
			list = libraryService.librarylist();
		}
		Map<Integer, Integer> num = printLibraryList(list);
		System.out.println("0. 이전 화면");
		do {
			int libNo = ScanUtil.nextInt("선택 : ");
			if (libNo == 0) {
				return View.LIBRARY;
			}
			if (num.containsValue(libNo)) {
				libraryService.librarySel(libNo);
				if(MainController.sessionStorage.containsKey("View")) {
					View view = (View) MainController.sessionStorage.remove("View");
					return view;
				}
				return mainMenu();
			}
			System.out.println("유효하지 않은 숫자입니다.");
		} while (true);
	}

	public View searchLibrary() {
		String input = ScanUtil.nextLine("검색 : ");
		List<Map<String, Object>> list = libraryService.searchLibrary(input);
		if (list == null) {
			System.out.println("검색 결과가 없습니다.");
			return View.LIBRARY;
		}
		Map<Integer, Integer> map = printLibraryList(list);
		System.out.println("0. 이전 화면");
		do {
			int libNo = ScanUtil.nextInt("선택 : ");
			if (libNo == 0) {
				return View.LIBRARY;
			}
			if (map.containsValue(libNo)) {
				libraryService.librarySel(libNo);
				if(MainController.sessionStorage.containsKey("View")) {
					View view = (View) MainController.sessionStorage.remove("View");
					return view;
				}
				return mainMenu();
			}
			System.out.println("유효하지 않은 숫자입니다.");
		} while (true);
	}

}
