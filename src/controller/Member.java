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


	private View sign() {
		// TODO Auto-generated method stub
		return null;
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
			System.out.println(member.get("name")+"님 환영합니다");
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
