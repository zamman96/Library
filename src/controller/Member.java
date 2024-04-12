package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.MemberService;
import util.ScanUtil;
import util.View;

public class Member {
	
	static public Map<String, Object> sessionStorage = new HashMap<>();
	MemberService memberService = MemberService.getInstance();

	
	public static void main(String[] args) {
		new Member().main();
		
	}
	

	/**
	 * <main>
	 * 로그인
	 * 비밀번호
	 * 회원가입
	 * 아이디 비밀번호 찾기
	 * 책 검색
	 * 자료실 좌석 조회
	 */
	
	private void main() {
		View view = View.MAIN;
		while (true) {
			switch (view) {
			case MAIN:
				view = home();
				break;
			case LOGIN:
				view = login();
				break;
			case SIGN:
				view = sign();
				break;
			case FOUND:
				view = found();
				break;
			case BOOKSEARCH:
				view = booksearch();
				break;

			default:
				break;
			}
			
		}	
		
	}


	private View booksearch() {
		// TODO Auto-generated method stub
		return null;
	}


	private View found() {
		// TODO Auto-generated method stub
		return null;
	}


	private boolean isIdExists(String id) {
	    // 여기서는 가정으로서 이미 등록된 ID가 없다고 가정
	    // 만약 실제 데이터베이스나 저장소와의 통합이 필요하다면 그에 맞게 구현해야 함
	    return false;
	}
	
	private View sign() {
	    while (true) {
	        String nm = ScanUtil.nextLine("NAME : ");
	        String id = ScanUtil.nextLine("ID: ");
	        String pw = ScanUtil.nextLine("PASS : ");
	        String tel = ScanUtil.nextLine("TELL : ");
	        
	        // 회원 가입 전에 기존에 같은 ID가 있는지 확인
	        
	        
	        List<Object> param = new ArrayList<Object>();
	        param.add(nm);
	        param.add(id);
	        
	        param.add(pw);
	        param.add(tel);
	        memberService.sign(param);
	        
	        return View.LOGIN;
	    }
	}



	private View login() {
		String id = ScanUtil.nextLine("ID : ");
		String pw = ScanUtil.nextLine("pass : ");
		List<Object> param = new ArrayList();
		param.add(id);
		param.add(pw);
		boolean loginchk = memberService.login(param);
		if(loginchk) {
			Map<String, Object> member = (Map<String, Object>) sessionStorage.get("member");
			System.out.println(member.get("MEM_NAME")+"님 환영합니다");
		}
		
		return View.MAIN;
	}
	
	

	
	private View home() {
		System.out.println("1. 로그인");
		System.out.println("2. 회원가입");
		System.out.println("3. 책 검색");
//		System.out.println("4. 자료실 좌석 조회");
		System.out.println("4. 아이디 비밀번호 찾기");
		
		int sel = ScanUtil.menu();
		switch (sel) {
		case 1:
			return View.LOGIN;
		case 2:
			return View.SIGN;
		case 3:
			return View.BOOKSEARCH;
		case 4:
			return View.FOUND;
		default: return View.MAIN;
		}
	}
		
	

}
