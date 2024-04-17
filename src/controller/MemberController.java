package controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import print.Print;
import service.BookService;
import service.MemberService;
import util.ScanUtil;
import util.View;

public class MemberController extends Print {
	private static MemberController instance;

	private MemberController() {

	}

	public static MemberController getInstance() {
		if (instance == null) {
			instance = new MemberController();
		}
		return instance;
	}

	static public Map<String, Object> sessionStorage;
	MemberService memberService = MemberService.getInstance();
	BookService bookService = BookService.getInstance();

	/**
	 * @return 로그인과 도서관 선택에 따라 main화면이 다름
	 */
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

	// ID 이름 번호 받고
	// 3자리 뺴고 3자리는 *

	// pass id 이름 번호
	// 비번 수정

	/**
	 * <main> 로그인 비밀번호 회원가입 아이디 비밀번호 찾기 책 검색 자료실 좌석 조회
	 */

	// 아이디 찾기
	public View idfound() {
		String name = ScanUtil.nextLine("이름을 입력하세요 : ");
		String tel = ScanUtil.nextLine("전화번호를 입력하세요 : ");
		String id = memberService.findId(name, tel);
		if (id != null) {
			if (id.length() > 3) {
				String truncatedId = id.substring(0, id.length() - 3);
				System.out.println("회원님의 아이디는 " + truncatedId + "***" + "입니다.");
			}
		} else {
			System.out.println("일치하는 회원 정보가 없습니다.");
		}

		return mainMenu();
	}

	// 비번 찾기
	public View pwfound() {
		String name = ScanUtil.nextLine("이름을 입력하세요 : ");
		String id = ScanUtil.nextLine("아이디를 입력하세요 : ");
		String tel = ScanUtil.nextLine("전화번호를 입력하세요 : ");

		
			// 일치하면 저장하고 newpw이동
			MainController.sessionStorage.put("found", id);
			return View.NEWPW;
			Map = map.get	
			select mem_no
			

			// 일치하지않으면 메인으로 이동
			}
		}
	}
	        
//
//		String password = memberService.findPassword(id, name, tel);
//		if (password.length() > 3) {
//			String truncatedPw = password.substring(0, id.length() - 3);
//			System.out.println("회원님의 비밀번호는 " + truncatedPw + "***" + "입니다.");
//		} else {
//			System.out.println("일치하는 회원 정보가 없습니다.");
//		}

//		con - bool remove
//		없으면 메인 
//		세션이 있으면 마이페이지
//		
	

	// 회원가입
	public View sign() {

	    while (true) {
	        String nm = ScanUtil.nextLine("NAME : ");
	        String id = ScanUtil.nextLine("ID: ");
	        String pw = ScanUtil.nextLine("PASS : ");
	        String tel = ScanUtil.nextLine("TELL : ");

	        List<Object> param = new ArrayList<Object>();
	        param.add(nm);
	        param.add(id);
	        param.add(pw);
	        param.add(tel);
	        
	        List<Object> idList = new ArrayList<Object>();
	        idList.add(id);
	        
	        boolean Idchk = memberService.idcheck(idList);


	        return View.SIGN;
	    }
	}

	// 로그인
	public View login() {
		String id = ScanUtil.nextLine("ID : ");
		String pw = ScanUtil.nextLine("pass : ");
		List<Object> param = new ArrayList();
		param.add(id);
		param.add(pw);
		boolean loginchk = memberService.login(param);
		if (loginchk) {
			Map<String, Object> member = (Map<String, Object>) MainController.sessionStorage.get("member");
			System.out.println(member.get("MEM_NAME") + "님 환영합니다");
		} else if (!loginchk) {
			System.out.println("1. 재로그인");
			System.out.println("2. 홈");
			int sel = ScanUtil.menu();

			switch (sel) {
			case 1:
				return View.LOGIN;
			case 2:
				return mainMenu();
			default:
				return View.LOGIN;
			}
		}
		
		List<Object> no = memberNo();
		// 대출예약 기간 지난 리스트가 있으면 출력
		boolean bookTimeOverChk = bookService.refTimeOverChk(no);
		if (bookTimeOverChk) {
			System.out.println("대출예약기간이 지난 도서가 있습니다");
			List<Map<String, Object>> list = bookService.refTimeOver(no); // 알림창
			printOverVar();
			printBookIndex();
			printMiddleVar();
			for (Map<String, Object> map : list) {
				printBookList(map);
			}
			printUnderVar();

			bookService.refTimeOverUpdate(no);
		}
		// 로그인 성공 시 대출 예약한 것이 대출이 가능한지 확인
		boolean bookRes = bookService.bookRefYN(no);
		if (bookRes) {
			System.out.println("대출 가능한 예약도서가 있습니다."); // 알림창
			return View.BOOK_RESERVATION_LIST;
		}
		if(MainController.sessionStorage.containsKey("View")) {
			View view = (View) MainController.sessionStorage.remove("View");
			return view;
		}
		return mainMenu();
	}
	
	
	public View logout() {
        memberService.logout(); 
        return View.MAIN;
    }
	
	
	public View delete() {
		
		System.out.println("비밀번호를 입력해 주세요");
	    String inputPassword = ScanUtil.nextLine();
	    // 세션에서 회원 정보 가져오기
	    Map<String, Object> member = (Map<String, Object>) MainController.sessionStorage.get("member");
	    
	    // 회원 번호 가져오기
	    BigDecimal no = (BigDecimal) member.get("MEM_NO");
	    String storedPassword = (String) member.get("MEM_PASS");
	    int num = no.intValue();
	    
	    boolean isPasswordCorrect = memberService.checkPw("", inputPassword);

	    
	    // 회원이 빌린 도서가 있는지 확인
	    List<Object> param = new ArrayList<>();
	    param.add(num);
	    List<Map<String, Object>> rentBooks = memberService.mem_book_rent(param);
	    
	    // 빌린 도서가 있는 경우 탈퇴 불가 안내 메시지 출력
	    if (rentBooks != null && !rentBooks.isEmpty()) {
	        System.out.println("탈퇴할 수 없습니다: 대출 도서가 있습니다.");
	        return View.MYPAGE;
	        // 탈퇴 불가 시 에러 뷰를 반환하거나, 적절한 처리를 수행합니다.
	    }
	    
	    // 빌린 도서가 없는 경우 회원 삭제
	    memberService.delete(param);
	    
	    // 세션 클리어
	    MainController.sessionStorage.clear();
	    
	  
	    return View.MAIN;
	}

	public View update() {
	    System.out.println("비밀번호를 입력해 주세요");
	    String inputPassword = ScanUtil.nextLine();

	    // 현재 세션에 저장된 회원 정보 가져오기
	    Map<String, Object> member = (Map<String, Object>) MainController.sessionStorage.get("member");
	    String storedPassword = (String) member.get("MEM_PASS"); // 현재 세션에 저장된 비밀번호 가져오기

	    // 비밀번호 일치 여부 확인
	    boolean isPasswordCorrect = memberService.checkPw("", inputPassword);

	    if (isPasswordCorrect) {
	        System.out.println("비밀번호가 일치합니다.");

	        System.out.println("수정하실 정보를 선택해 주세요");
	        System.out.println("\t\t1. 비밀번호 수정\t2. 전화 번호 수정 \t3. 전체 수정");

	        int sel = ScanUtil.menu();
	        switch (sel) {
	            case 1:
	                return View.NEWPW;
	            case 2:
	                return View.NEWPHONE;
	            case 3:
	                return View.TOTALNEW;
	            default:
	                return View.UPDATE;
	        }
	    } else {
	        System.out.println("비밀번호가 일치하지 않습니다.");
	    }
	    return View.UPDATE;
	}
	

	public View newPassword() {
		System.out.print("새로운 비밀번호를 입력하세요:");
		String newPassword = ScanUtil.nextLine();

		// 회원 번호 가져오기
		if(MainController.sessionStorage.containsKey("member")) {
		Map<String, Object> member = (Map<String, Object>) MainController.sessionStorage.get("member");
		BigDecimal no = (BigDecimal) member.get("MEM_NO");
		int num = no.intValue();

		// 파라미터 구성
		List<Object> param = new ArrayList<>();
		param.add(newPassword);
		param.add(num);

		// 회원 정보 업데이트
		memberService.update(param, 1); // 1은 비밀번호 수정을 의미
		System.out.println("비밀번호가 성공적으로 수정되었습니다.");
		
		MainController.sessionStorage.remove("member");
		
		return View.MYPAGE;
		
		} else {
			String id = (String) MainController.sessionStorage.remove("found");
			List<Object> found = new ArrayList<Object>();
			found.add(newPassword);
			found.add(id);
			
			System.out.println("변경이 완료되었습니다");
			return View.MAIN;
		}
	}

	public View newPhonenumber() {
		// 전화번호 수정
		System.out.println("새로운 전화번호를 입력하세요:");
		String newPhoneNumber = ScanUtil.nextLine();

		// 회원 번호 가져오기
		Map<String, Object> member2 = (Map<String, Object>) MainController.sessionStorage.get("member");
		BigDecimal no2 = (BigDecimal) member2.get("MEM_NO");
		int num2 = no2.intValue();

		// 파라미터 구성
		List<Object> param2 = new ArrayList<>();
		param2.add(newPhoneNumber);
		param2.add(num2);

		// 회원 정보 업데이트
		memberService.update(param2, 2);
		System.out.println("전화번호가 수정되었습니다.");
		return View.MYPAGE;
	}

	public View totalNew() {
		// 전체 수정
		System.out.println("새로운 비밀번호를 입력하세요:");
		String newPassword = ScanUtil.nextLine();
		System.out.println("새로운 전화번호를 입력하세요:");
		String newPhoneNumber = ScanUtil.nextLine();

		// 회원 번호 가져오기
		Map<String, Object> member3 = (Map<String, Object>) MainController.sessionStorage.get("member");
		BigDecimal no3 = (BigDecimal) member3.get("MEM_NO");
		int num3 = no3.intValue();

		// 파라미터 구성
		List<Object> param3 = new ArrayList<>();
		param3.add(newPassword);
		param3.add(newPhoneNumber);
		param3.add(num3);

		// 전체 수정 메소드 호출
		memberService.update(param3, 3); // 3은 전체 수정을 의미
		System.out.println("비밀번호와 전화번호가 성공적으로 수정되었습니다.");
		return View.MYPAGE;
	}
	

}
