package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class LibraryDao {
	private static LibraryDao instance;

	private LibraryDao() {

	}

	public static LibraryDao getInstance() {
		if (instance == null) {
			instance = new LibraryDao();
		}
		return instance;
	}

	JDBCUtil jdbc = JDBCUtil.getInstance();

	public List<Map<String,Object>> localName(){
		String sql = "SELECT *\r\n" + 
				"FROM LOCATION";
		return jdbc.selectList(sql);
	}
	
	// 지역선택 시 지역에 맞는 도서관 출럭
	public List<Map<String,Object>> localLibraryList(List<Object> param){
		String sql = "SELECT LIB_NO, LIB_NAME\r\n" + 
				"FROM LIBRARY\r\n" + 
				"WHERE LOC_NO=?";
		return jdbc.selectList(sql, param);
	}
	
	// 전체 도서관 출력
	public List<Map<String,Object>> librarylist(){
		String sql = "SELECT LIB_NO, LIB_NAME\r\n" + 
				"FROM LIBRARY\r\n";
		return jdbc.selectList(sql);
	}
}
