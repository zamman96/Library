package dao;

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
					 " AND DELYN = 'N' ";
		return jdbc.selectOne(sql,param);
	}
	
	// 회원가입
	public void sign(List<Object> param) {
		String sql ="  INSERT INTO MEMBER (MEM_NO, MEM_NAME, MEM_ID, MEM_PASS,MEM_TELNO)\r\n" + 
					" VALUES ((SELECT NVL(MAX(MEM_NO),0)+1 FROM MEMBER), \r\n" + 
					"        ?, ?, ?, ?)";
		jdbc.update(sql, param);
		
	} 

	
	

}
