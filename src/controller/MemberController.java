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
		if (MainController.sessionStorage.containsKey("admin")
				|| MainController.sessionStorage.containsKey("manager")) {
			return View.ADMIN;
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
		String name = ScanUtil.nextLine(tap + "이름  >  ");
		System.out.println(var);
		System.out.println(tap + "전화번호는 숫자만 입력해주세요");
		System.out.println(tap + "예시 : 01012341234");
		System.out.println(var);
		System.out.println();
		String tel = "";
		while (true) {
			tel = ScanUtil.nextLine(tap + "전화번호  > ");
			if (tel.matches("\\d+")) {
				break;
			}
			noticeNotNo();
		}
		String num1 = tel.substring(0, 2);
		String num2 = tel.substring(3, 6);
		String num3 = tel.substring(7);
		tel = num1 + "-" + num2 + "-" + num3;
		String id = memberService.findId(name, tel);
		if (id != null) {
			if (id.length() > 3) {
				String truncatedId = id.substring(0, id.length() - 3);
				System.out.println(tap + "회원님의 아이디는 " + truncatedId + "***" + "입니다.");
			}
		} else {
			System.out.println(RED + var + END);
			System.out.println(tap + "일치하는 회원 정보가 없습니다.");
			System.out.println(RED + var + END);
		}

		return mainMenu();
	}

	// 비번 찾기
	public View pwfound() {
		String id = ScanUtil.nextLine(tap + "아이디 >  ");
		String name = ScanUtil.nextLine(tap + "이름  >  ");
		System.out.println(var);
		System.out.println(tap + "전화번호는 숫자만 입력해주세요");
		System.out.println(tap + "예시 : 01012341234");
		System.out.println(var);
		System.out.println();
		String tel = "";
		while (true) {
			tel = ScanUtil.nextLine(tap + "전화번호  > ");
			if (tel.matches("\\d+")) {
				break;
			}
			noticeNotNo();
		}
		String num1 = tel.substring(0, 2);
		String num2 = tel.substring(3, 6);
		String num3 = tel.substring(7);
		tel = num1 + "-" + num2 + "-" + num3;
		List<Object> param = new ArrayList<Object>();
		param.add(id);
		param.add(name);
		param.add(tel);

		Map<String, Object> map = memberService.findPassword(param);
		if (map == null) {
			System.out.println(RED + var + END);
			System.out.println(tap + "일치하는 회원 정보가 없습니다");
			System.out.println(RED + var + END);
			return mainMenu();
		}

		int memNo = ((BigDecimal) map.get("MEM_NO")).intValue();
		MainController.sessionStorage.put("found", memNo);
		return View.NEWPW;
	}

	// 회원가입
//	public View sign() {
//
//		while (true) {
//			boolean Idchk = true;
//			String id = "";
//			do {
//				id = ScanUtil.nextLine("아이디   > ");
//				List<Object> idList = new ArrayList<Object>();
//				idList.add(id);
//				Idchk = memberService.idcheck(idList);
//			} while (!Idchk);
//			String pw = ScanUtil.nextLine(tap + "비밀번호  >");
//			String nm = ScanUtil.nextLine(tap + "이름  > ");
//			System.out.println(var);
//			System.out.println(tap + "전화번호는 숫자만 입력해주세요");
//			System.out.println(tap + "예시 : 01012341234");
//			System.out.println(var);
//			System.out.println();
//			String tel = "";
//			while (true) {
//				tel = ScanUtil.nextLine(tap + "전화번호  > ");
//				if (tel.matches("\\d+")) {
//					break;
//				}
//				noticeNotNo();
//			}
//			String num1 = tel.substring(0, 2);
//			String num2 = tel.substring(3, 6);
//			String num3 = tel.substring(7);
//			tel = num1 + "-" + num2 + "-" + num3;
//
//			List<Object> param = new ArrayList<Object>();
//			param.add(id);
//			param.add(pw);
//			boolean loginChk = memberService.login(param); // 세션에 정보저장
//			param.add(nm);
//			param.add(tel);
//			memberService.sign(param);
//			System.out.println(tap + "회원가입이 완료되었습니다.");
//			return mainMenu();
//		}
//	}
	
	public View sign() {
	    while (true) {
	        boolean Idchk = true;
	        String id = "";
	        id = ScanUtil.nextLine("아이디   > ");
	        do {
	            // 아이디 길이 및 형식 검사
	            if (isValidId(id)) {
	                List<Object> idList = new ArrayList<Object>();
	                idList.add(id);
	                Idchk = memberService.idcheck(idList);
	            } else {
	                System.out.println("아이디는 5자 이상 15자 이하여야 합니다.");
	            }
	        } while (!Idchk);

	        String pw = "";
	        // 비밀번호 길이 검사
	        while (true) {
	            pw = ScanUtil.nextLine(tap + "비밀번호  >");
	            if (isValidPassword(pw)) {
	                break;
	            } else {
	                System.out.println("비밀번호는 5자 이상 15자 이하여야 합니다.");
	            }
	        }

	        String nm = "";
	        // 이름 길이 및 한글 여부 검사
	        while (true) {
	            nm = ScanUtil.nextLine(tap + "이름  > ");
	            if (isValidName(nm)) {
	                break;
	            } else {
	                System.out.println("이름은 10자 이내의 한글로 입력해야 합니다.");
	            }
	        }

	        System.out.println(var);
	        System.out.println(tap + "전화번호는 숫자만 입력해주세요");
	        System.out.println(tap + "예시 : 01012341234");
	        System.out.println(var);
	        System.out.println();

	        String tel = "";
	        // 전화번호 유효성 검사 및 형식 변환
	        while (true) {
	            tel = ScanUtil.nextLine(tap + "전화번호  > ");
	            if (isValidPhoneNumber(tel)) {
	                break;
	            } else {
	                System.out.println("전화번호 형식이 잘못되었습니다.");
	            }
	        }
	        tel = formatPhoneNumber(tel);

	        List<Object> param = new ArrayList<Object>();
	        param.add(id);
	        param.add(pw);
	        boolean loginChk = memberService.login(param); // 세션에 정보저장
	        param.add(nm);
	        param.add(tel);
	        memberService.sign(param);
	        System.out.println(tap + "회원가입이 완료되었습니다.");
	        return mainMenu();
	    }
	}
	
	// 아이디 유효성 검사
	public boolean isValidId(String id) {
	    return id.length() >= 5 && id.length() <= 15 && id.matches("^[a-z0-9]*$");
	}

	// 비밀번호 유효성 검사
	public boolean isValidPassword(String pw) {
	    return pw.length() >= 5 && pw.length() <= 15;
	}

	// 이름 유효성 검사
	public boolean isValidName(String nm) {
	    return nm.length() <= 10 && nm.matches("^[가-힣]*$");
	}

	// 전화번호 유효성 검사
	public boolean isValidPhoneNumber(String tel) {
	    return tel.matches("\\d{11}");
	}

	// 전화번호 형식 변환
	public String formatPhoneNumber(String tel) {
	    return tel.substring(0, 3) + "-" + tel.substring(3, 7) + "-" + tel.substring(7);
	}

	// 로그인
	public View login() {
		String id = ScanUtil.nextLine(tap + "아이디  > ");
		String pw = ScanUtil.nextLine(tap + "비밀번호  > ");
		List<Object> param = new ArrayList();
		param.add(id);
		param.add(pw);
		boolean loginchk = memberService.login(param);
		if (loginchk) {
			Map<String, Object> member = (Map<String, Object>) MainController.sessionStorage.get("member");
			System.out.println(tap + member.get("MEM_NAME") + "님 환영합니다");
			System.out.println(var);
			int no = ((BigDecimal) member.get("ADMIN_NO")).intValue();
			if (no == 2) {
				MainController.sessionStorage.remove("member");
				MainController.sessionStorage.put("admin", member);
			} else if (no == 3) {
				MainController.sessionStorage.remove("member");
				MainController.sessionStorage.put("manager", member);
			}
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
		if (MainController.sessionStorage.containsKey("View")) {
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

		System.out.println(tap + "비밀번호를 입력해 주세요");
		String inputPassword = ScanUtil.menuStr();
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
			System.out.println(RED + var + END);
			System.out.println(tap + "탈퇴할 수 없습니다: 대출 도서가 있습니다.");
			System.out.println(RED + var + END);
			return View.MYPAGE;
			// 탈퇴 불가 시 에러 뷰를 반환하거나, 적절한 처리를 수행합니다.
		}

		// 빌린 도서가 없는 경우 회원 삭제
		memberService.delete(param);

		// 세션 클리어
		MainController.sessionStorage.remove("member");

		return mainMenu();
	}

	public View update() {
		System.out.println(tap + "비밀번호를 입력해 주세요");
		String inputPassword = ScanUtil.menuStr();

		// 현재 세션에 저장된 회원 정보 가져오기
		Map<String, Object> member = (Map<String, Object>) MainController.sessionStorage.get("member");
		String storedPassword = (String) member.get("MEM_PASS"); // 현재 세션에 저장된 비밀번호 가져오기

		// 비밀번호 일치 여부 확인
		boolean isPasswordCorrect = memberService.checkPw("", inputPassword);

		if (isPasswordCorrect) {
			System.out.println(var);
			System.out.println(tap + "비밀번호가 일치합니다.");
			System.out.println(var);
			System.out.println();
			printMenuVar();
			System.out.println(tap + "수정하실 정보를 선택해 주세요");
			System.out.println(tap + "\t\t1. 비밀번호 수정\t2. 전화 번호 수정 \t3. 전체 수정");
			printMenuVar();

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
			System.out.println(var);
			System.out.println(tap + "비밀번호가 일치하지 않습니다.");
			System.out.println(tap + "마이페이지로 돌아갑니다");
			System.out.println(var);
			return View.MYPAGE;
		}
	}

	public View newPassword() {
		System.out.print(tap + "새로운 비밀번호를 입력하세요:");
		String newPassword = "";
		while (true) {
			newPassword = ScanUtil.nextLine(tap + "비밀번호       > ");
			String newPassword2 = ScanUtil.nextLine(tap + "비밀번호 확인   > ");

			if (newPassword.equals(newPassword2)) {
				break;
			}
			System.out.println(RED + var + END);
			System.out.println("비밀번호가 다릅니다");
			System.out.println("다시 입력해주세요");
			System.out.println(RED + var + END);
		}

		// 로그인되어있을때
		if (MainController.sessionStorage.containsKey("member")) {
			Map<String, Object> member = (Map<String, Object>) MainController.sessionStorage.get("member");
			BigDecimal no = (BigDecimal) member.get("MEM_NO");
			int num = no.intValue();

			// 파라미터 구성
			List<Object> param = new ArrayList<>();
			param.add(newPassword);
			param.add(num);

			// 회원 정보 업데이트
			memberService.update(param, 1); // 1은 비밀번호 수정을 의미
			System.out.println(var);
			System.out.println(tap + "비밀번호가 성공적으로 수정되었습니다.");
			System.out.println(var);

			MainController.sessionStorage.remove("member");

			return View.MYPAGE;
			// 안되어있을떄
		} else {
			String memNo = (String) MainController.sessionStorage.remove("found");
			List<Object> found = new ArrayList<Object>();
			found.add(newPassword);
			found.add(memNo);
			System.out.println(var);
			System.out.println(tap + "변경이 완료되었습니다");
			System.out.println(var);
			return mainMenu();
		}
	}

	public View newPhonenumber() {
		// 전화번호 수정
		System.out.println(var);
		System.out.println(tap + "전화번호는 숫자만 입력해주세요");
		System.out.println(tap + "예시 : 01012341234");
		System.out.println(var);
		System.out.println();
		System.out.println(tap + "새로운 전화번호를 입력하세요:");
		String newPhoneNumber = "";
		while (true) {
			newPhoneNumber = ScanUtil.nextLine(tap + "전화번호  > ");
			if (newPhoneNumber.matches("\\d+")) {
				break;
			}
			noticeNotNo();
		}
		String num1 = newPhoneNumber.substring(0, 2);
		String num2 = newPhoneNumber.substring(3, 6);
		String num3 = newPhoneNumber.substring(7);
		newPhoneNumber = num1 + "-" + num2 + "-" + num3;

		// 회원 번호 가져오기
		Map<String, Object> member2 = (Map<String, Object>) MainController.sessionStorage.get("member");
		BigDecimal no2 = (BigDecimal) member2.get("MEM_NO");
		int no = no2.intValue();

		// 파라미터 구성
		List<Object> param2 = new ArrayList<>();
		param2.add(newPhoneNumber);
		param2.add(no);

		// 회원 정보 업데이트
		memberService.update(param2, 2);
		System.out.println(var);
		System.out.println(tap + "전화번호가 수정되었습니다.");
		System.out.println(var);
		return View.MYPAGE;
	}

	public View totalNew() {
		// 전체 수정
		System.out.println(var);
		System.out.print(tap + "새로운 비밀번호를 입력하세요:");
		System.out.println(var);
		String newPassword = "";
		while (true) {
			newPassword = ScanUtil.nextLine(tap + "비밀번호       > ");
			String newPassword2 = ScanUtil.nextLine(tap + "비밀번호 확인   > ");

			if (newPassword.equals(newPassword2)) {
				break;
			}
			System.out.println(RED + var + END);
			System.out.println("비밀번호가 다릅니다");
			System.out.println("다시 입력해주세요");
			System.out.println(RED + var + END);
		}
		System.out.println(var);
		System.out.println(tap + "전화번호는 숫자만 입력해주세요");
		System.out.println(tap + "예시 : 01012341234");
		System.out.println(var);
		System.out.println();
		System.out.println(var);
		System.out.println(tap + "새로운 전화번호를 입력하세요:");
		System.out.println(var);
		String newPhoneNumber = "";
		while (true) {
			newPhoneNumber = ScanUtil.nextLine(tap + "전화번호  > ");
			if (newPhoneNumber.matches("\\d+")) {
				break;
			}
		}
		String num1 = newPhoneNumber.substring(0, 2);
		String num2 = newPhoneNumber.substring(3, 6);
		String num3 = newPhoneNumber.substring(7);
		newPhoneNumber = num1 + "-" + num2 + "-" + num3;

		// 회원 번호 가져오기
		Map<String, Object> member3 = (Map<String, Object>) MainController.sessionStorage.get("member");
		BigDecimal no3 = (BigDecimal) member3.get("MEM_NO");
		int no = no3.intValue();

		// 파라미터 구성
		List<Object> param3 = new ArrayList<>();
		param3.add(newPassword);
		param3.add(newPhoneNumber);
		param3.add(no);

		// 전체 수정 메소드 호출
		memberService.update(param3, 3); // 3은 전체 수정을 의미
		System.out.println(var);
		System.out.println(tap + "비밀번호와 전화번호가 성공적으로 수정되었습니다.");
		System.out.println(var);
		return View.MYPAGE;
	}

}
