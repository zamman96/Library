package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import controller.MainController;
import controller.MemberController;
import dao.MemberDao;
import util.ScanUtil;

public class MemberService {
	private static MemberService instance;


	private MemberService() {
	}
	
		

	public static MemberService getInstance() {
		if (instance == null) {
			instance = new MemberService();
		}
		return instance;
	}
	
	MemberDao memdao = MemberDao.getInstance();
	
	// 로그인
	public boolean login(List<Object> param) {
		Map<String, Object> member = memdao.login(param);
		//데이터가 없을 때 로그인 실패 member == null
		if(member == null) {
			return false;
		}
		MainController.sessionStorage.put("member", member);
		return true;
	}
	
	// 회원가입
	public void sign(List<Object>param) {
		memdao.sign(param);
	}

	// 중복 아이디
    public boolean idcheck(List<Object> param) {
    	Map<String, Object> result = memdao.idcheck(param);
    	if (result != null && !result.isEmpty()) {
            System.out.println("중복된 아이디입니다. 다시 시도해주세요" );
            return false; // 중복된 아이디가 있음을 나타내는 값을 반환
        }
        return true;
    }

    // 로그아웃
	public void logout() {
		MainController.sessionStorage.remove("member");
		System.out.println("로그아웃 되었습니다.");
	}
    
    // 아이디 찾기
	public String findId(String name, String tel) {
        return memdao.findId(name, tel);
    }

	// 비번 찾기
	public String findPassword(String id, String name, String tel) {
		return memdao.findPassword(id, name, tel);
	}
	
	// 탈퇴
	public void delete(List<Object> param) {
	    // 탈퇴 불가능한 상태인지 확인
	    List<Map<String, Object>> rentBooks = memdao.mem_book_rent(param);
	    if (rentBooks != null && !rentBooks.isEmpty()) {
	        // 빌린 도서가 있는 경우 탈퇴 불가능
	    } else {
	        // 빌린 도서가 없는 경우 회원 삭제
	        memdao.delete(param);
	        System.out.println("탈퇴되었습니다.");
	    }
	}
	
	public List<Map<String, Object>> mem_book_rent (List<Object> param) {
		
		return memdao.mem_book_rent(param);
		
	}
	
	// 회원 정보 수정
	public void update(List<Object> param, int sel ) {
        memdao.update(param, sel);
    }
	
	
	// 비번 체크 (회원정보 수정 / 탈퇴 시 확인)
//	public boolean checkPw(String id, String password) {
//
//		Map<String, Object> checkmem = (Map<String, Object>) MainController.sessionStorage.get("member");
//
//		if (checkmem != null) {
//			return true;
//		} else {
//			return false;
//		}
//	}
	
	public boolean checkPw(String id, String inputPassword) {
	    // 현재 세션에 저장된 회원 정보 가져오기
	    Map<String, Object> member = (Map<String, Object>) MainController.sessionStorage.get("member");

	    // 세션에서 가져온 회원 정보가 null이 아니고, 입력한 비밀번호와 세션에서 가져온 비밀번호가 일치하는지 확인
	    if (member != null && member.containsKey("MEM_PASS")) {
	        String storedPassword = (String) member.get("MEM_PASS");
	        return storedPassword.equals(inputPassword);
	    } else {
	        return false; 
	    }
	}

	

}
