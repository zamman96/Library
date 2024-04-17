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
	
	public boolean login(List<Object> param) {
		Map<String, Object> member = memdao.login(param);
		//데이터가 없을 때 로그인 실패 member == null
		if(member == null) {
			return false;
		}
		MainController.sessionStorage.put("member", member);
		return true;
	}
	
	public void sign(List<Object>param) {
		memdao.sign(param);
	}

	
    public boolean idcheck(List<Object> param) {
    	Map<String, Object> result = memdao.idcheck(param);
    	if (result != null && !result.isEmpty()) {
            System.out.println("중복된 아이디입니다. 다시 시도해주세요" );
            return false; // 중복된 아이디가 있음을 나타내는 값을 반환
        }
        
        return true;
    	
    }

    
	public void logout() {
		MainController.sessionStorage.remove("member");
		System.out.println("로그아웃 되었습니다.");
	}
    
    
	public String findId(String name, String tel) {
        return memdao.findId(name, tel);
    }


	public String findPassword(String id, String name, String tel) {
		return memdao.findPassword(id, name, tel);
	}
	
	

	public void delete(List<Object> param) {
		memdao.delete(param);
		
	}
	
	public boolean checkPw(String id, String password) {
		String pw = ScanUtil.nextLine("PASS : ");
		
		List<Object> param = new ArrayList<Object>();
		param.add(pw);
		
		Map<String, Object> checkmem = (Map<String, Object>) MainController.sessionStorage.get("member");

		if (checkmem != null && checkmem.containsKey(param)) {
            String storedPassword = (String) checkmem.get(param);
            return storedPassword.equals(password);
        } else {
            return false; 
        }
    }
	
	

}
