package controller;

import java.util.ArrayList;
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

	public View localLibrary() {
		List<Map<String, Object>> list = libraryService.localName();
		Map<Integer, Integer> map = printLocalList(list);
		System.out.println("0. 이전 화면");
		do {
			int locNo = ScanUtil.nextInt("선택 : ");
			if (locNo == 0) {
				return View.LIBRARY;
			}
			if (map.get(locNo) != null) {
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
		Map<Integer, Integer> map = printLibraryList(list);
		System.out.println("0. 이전 화면");
		do {
			int libNo = ScanUtil.nextInt("선택 : ");
			if (libNo == 0) {
				return View.LIBRARY;
			}
			if (map.get(libNo) != null) {
				libraryService.librarySel(libNo);
				return View.BOOK_LIST;
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
			if (map.get(libNo)!=null ) {
				libraryService.librarySel(libNo);
				return View.BOOK_LIST;
			}
			System.out.println("유효하지 않은 숫자입니다.");
		} while (true);
	}

}
