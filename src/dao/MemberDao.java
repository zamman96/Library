package dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class MemberDao {
	private static MemberDao instance;

	private MemberDao() {

	}

	public static MemberDao getInstance() {
		if (instance == null) {
			instance = new MemberDao();
		}
		return instance;
	}
	
	JDBCUtil jdbc = JDBCUtil.getInstance();

	
	// 로그인
	public Map<String, Object> login(List<Object> param) {
		String sql = " SELECT * \r\n" +
					 " FROM MEMBER \r\n" +
					 " WHERE MEM_ID = ? \r\n" +
					 " AND MEM_PASS = ? " +
					 " AND DEL_YN = 'N' ";
		return jdbc.selectOne(sql,param);
	}
	
	// 회원가입
	public void sign(List<Object> param) {
		String sql ="  INSERT INTO MEMBER (MEM_NO, MEM_NAME, MEM_ID, MEM_PASS,MEM_TELNO)\r\n" + 
					" VALUES ((SELECT NVL(MAX(MEM_NO),0)+1 FROM MEMBER), \r\n" + 
					"        ?, ?, ?, ?)";
		jdbc.update(sql, param);
		
	} 
	
	// 아이디 중복 체크
	 public Map<String, Object> idcheck(List<Object> param) {
	        String sql = " SELECT MEM_ID\r\n" + 
	        			 " FROM MEMBER\r\n" + 
	        			 " WHERE MEM_ID = ?"  ;
	        return jdbc.selectOne(sql, param);
	    }
	
	
	// 아이디 찾기
	public String findId(String name, String tel) {
        String sql = "SELECT MEM_ID FROM MEMBER WHERE MEM_NAME = ? AND MEM_TELNO = ?";
        List<Object> param = new ArrayList<>();
        param.add(name);
        param.add(tel);
        Map<String, Object> result = jdbc.selectOne(sql, param);
        if (result != null && result.containsKey("MEM_ID")) {
            return (String) result.get("MEM_ID");
        } else {
            return null;
        }
    }
	
	// 비밀번호 찾기
	public String findPassword(String id, String name, String tel) {
        String sql = "SELECT MEM_PASS FROM MEMBER WHERE MEM_ID = ? AND MEM_NAME = ? AND MEM_TELNO = ?";
        List<Object> param = new ArrayList<>();
        param.add(id);
        param.add(name);
        param.add(tel);
        Map<String, Object> result = jdbc.selectOne(sql, param);
        if (result != null && result.containsKey("MEM_PASS")) {
            return (String) result.get("MEM_PASS");
        } else {
            return null;
        }
    }
	

	
	

}
