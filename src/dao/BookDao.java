package dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

/**
 * @author 송예진
 *
 */
public class BookDao {
	private static BookDao instance;

	private BookDao() {

	}

	public static BookDao getInstance() {
		if (instance == null) {
			instance = new BookDao();
		}
		return instance;
	}
	
	JDBCUtil jdbc = JDBCUtil.getInstance();
	
	// 대출여부 판단 > 데이터가 있을 때 > 대출(대출예약) / 없을 땐 대출
	public Map<String, Object> rentChk(List<Object> param){
		String sql = "SELECT 1\r\n" + 
				"FROM BOOK_RENT A, BOOK B\r\n" + 
				"WHERE A.BOOK_NO=?\r\n" + 
				"AND B.BOOK_REMARK = '사용가능' ";
		return jdbc.selectOne(sql, param);
	}
	
	// 대출가능 권 수 ( 대출가능 권 수 출력 + 대출 가능 boolean)
	public Map<String, Object> bookVol(List<Object> param){
		String sql = "SELECT COUNT(*)\r\n" + 
				"FROM MEMBER A, BOOK_RENT B\r\n" + 
				"WHERE A.MEM_NO=B.MEM_NO\r\n" + 
				"AND B.MEM_NO=?";
		return jdbc.selectOne(sql, param);
	}
	// 대출 예약
	public void bookReservation(List<Object> param) {
		String sql = "    SELECT NVL(MAX(BOOK_REF_NO),0)+1 AS NO\r\n" + 
				"    FROM BOOK_REF";
		int no = ((BigDecimal) jdbc.selectOne(sql).get("NO")).intValue();

		// book_ref_no 삽입
		sql = "    INSERT INTO BOOK_REF(BOOK_REF_NO)\r\n" + 
				"    VALUES (L_RNO) ";
		jdbc.update(sql);
		
		sql = "    INSERT INTO BOOK_RENT(BOOK_NO,MEM_NO,BOOK_REF_NO)\r\n" + 
				"    VALUES(?, ?, "+no+")";
		jdbc.update(sql, param);
	}
	
	// 대출 
	public void bookRent(List<Object> param) {
		String sql = "INSERT INTO BOOK_RENT(BOOK_NO,MEM_NO,RENT_DATE,RETURN_DATE,DELAY_YN,RETURN_YN)\r\n" + 
				"VALUES(?, ?, SYSDATE,SYSDATE+14,'N','N')";
		jdbc.update(sql, param);
	}
	// 반납
	
	
	// 연장
}
