package dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

/**
 * @author 송예진
 *
 */
/**
 * @author PC-13
 *
 */
/**
 * @author PC-13
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
	
	/**
	 * @param param MEM_NO
	 * @return 대출가능 권 수
	 * 대출가능 권수가 <1이면 대출 불가능
	 */
	public Map<String, Object> bookVol(List<Object> param){
		String sql = "SELECT COUNT(*)\r\n" + 
				"FROM MEMBER A, BOOK_RENT B\r\n" + 
				"WHERE A.MEM_NO=B.MEM_NO\r\n" + 
				"AND B.MEM_NO=?";
		return jdbc.selectOne(sql, param);
	}
	
	/**
	 * @param param
	 * 대출 예약 2번째 수행 (book_rent insert)
	 */
	public void bookReservation(List<Object> param) {
		String sql = "    SELECT NVL(MAX(BOOK_REF_NO),0) AS NO\r\n" + 
				"    FROM BOOK_REF";
		int no = ((BigDecimal) jdbc.selectOne(sql).get("NO")).intValue();
		
		sql = "    INSERT INTO BOOK_RENT(BOOK_NO,MEM_NO,BOOK_REF_NO)\r\n" + 
				"    VALUES(?, ?, "+no+")";
		jdbc.update(sql, param);
	}
	
	/**
	 * @param param BOOK_NO
	 * 대출 예약 1번째 수행 (book_ref insert)
	 */
	public void bookRefUpdate(List<Object> param) {
		String sql = "SELECT NVL(MAX(A.BOOK_REF_SEQ),0)+1 AS SEQ \r\n" + 
				"FROM BOOK_REF A, BOOK_RENT B\r\n" + 
				"WHERE A.BOOK_REF_NO=B.BOOK_REF_NO\r\n" + 
				"AND B.BOOK_NO=?";
		int seq = ((BigDecimal) jdbc.selectOne(sql, param).get("SEQ")).intValue();
		sql = "INSERT INTO BOOK_REF(BOOK_REF_NO, BOOK_REF_SEQ)                \r\n" + 
				"SELECT NVL(MAX(BOOK_REF_NO),0)+1, "+seq+"\r\n" + 
				"FROM BOOK_REF";
		jdbc.update(sql);
	}
	 
	/**	대출하기
	 * @param param = BOOK_NO, MEM_NO
	 */
	public void bookRent(List<Object> param) {
		String sql = "INSERT INTO BOOK_RENT(BOOK_NO,MEM_NO,RENT_DATE,RETURN_DATE,DELAY_YN,RETURN_YN)\r\n" + 
				"VALUES(?, ?, SYSDATE,SYSDATE+14,'N','N')";
		jdbc.update(sql, param);
	}
	
	// 
	/**
	 * @param param = MEM_NO
	 * @return 대출 가능한 예약도서목록 / null일 경우 빌릴수 있는 예약도서가 없음
	 * 예약할지 여부를 물어본 뒤 BOOK_REF_CHK 변경
	 */
	public List<Map<String, Object>> bookRefPossList(List<Object> param){
		String sql = "SELECT B.BOOK_NO, D.BOOK_NAME, D.BOOK_AUTHOR, D.BOOK_PUB, G.LIB_NAME, F.CATE_NAME\r\n" + 
				"FROM BOOK_REF A, BOOK_RENT B,\r\n" + 
				"    (SELECT B.BOOK_NO AS MNO, MIN(A.BOOK_REF_SEQ) AS MSEQ\r\n" + 
				"    FROM BOOK_REF A, BOOK_RENT B\r\n" + 
				"    WHERE A.BOOK_REF_NO=B.BOOK_REF_NO\r\n" + 
				"    AND A.BOOK_REF_CHK=1\r\n" + 
				"    GROUP BY B.BOOK_NO) C, BOOK D, BOOK_CATEGORY F, LIBRARY G\r\n" + 
				"WHERE A.BOOK_REF_NO=B.BOOK_REF_NO\r\n" + 
				"AND C.MNO=B.BOOK_NO\r\n" + 
				"AND A.BOOK_REF_SEQ=C.MSEQ\r\n" + 
				"AND B.BOOK_NO=D.BOOK_NO\r\n" + 
				"AND D.CATE_NO=F.CATE_NO\r\n" + 
				"AND D.LIB_NO=G.LIB_NO\r\n" + 
				"AND B.MEM_NO=?";
		return jdbc.selectList(sql, param);
	}
	// 
	/**
	 * @param param = BOOK_NO, MEM_NO
	 * 예약도서 대출 메소드
	 * UPDATE book_ref_chk =2(대출) > 트리거 trg_book_ref_chk_rent
	 */
	public void bookRefRent(List<Object> param){
		String sql = "UPDATE BOOK_RENT\r\n" + 
				"SET RENT_DATE=SYSDATE,\r\n" + 
				"        RETURN_DATE = SYSDATE,\r\n" + 
				"        DELAY_YN='N',\r\n" + 
				"        RETURN_YN='N'\r\n" + 
				"WHERE BOOK_REF_NO IN (SELECT A.BOOK_REF_NO\r\n" + 
				"                                              FROM BOOK_REF A, BOOK_RENT B\r\n" + 
				"                                            WHERE A.BOOK_REF_NO=B.BOOK_REF_NO\r\n" + 
				"                                                 AND A.BOOK_REF_CHK=1\r\n" + 
				"                                                 AND B.BOOK_NO=?\r\n" + 
				"                                                AND B.MEM_NO=?)";
		jdbc.update(sql, param);
	}
	

	/**	book_ref_chk=3(체크) 꼭 나가기전에 되묻기    
	 * @param param = MEM_NO
	 */
	public void bookRefChk(List<Object> param) {
		String sql = "UPDATE BOOK_REF\r\n" + 
				"SET BOOK_REF_CHK=3\r\n" + 
				"WHERE BOOK_REF_NO IN (SELECT A.BOOK_REF_NO\r\n" + 
				"                                              FROM BOOK_REF A, BOOK_RENT B\r\n" + 
				"                                            WHERE A.BOOK_REF_NO=B.BOOK_REF_NO\r\n" + 
				"                                                 AND A.BOOK_REF_CHK=1\r\n" + 
				"                                                AND B.MEM_NO=?)";
		jdbc.update(sql, param);
	}
	
	
	/**	반납/연장해야할 책 목록
	 * @param param MEM_NO
	 * @return 빌린 책 목록
	 */
	public List<Map<String,Object>> bookDelayChk(List<Object> param) {
		String sql = "SELECT A.BOOK_NO, A.BOOK_NAME, A.BOOK_AUTHOR, A.BOOK_PUB, A.BOOK_PUB_YEAR, C.CATE_NAME, L.LIB_NAME, B.RENT_DATE, B. RETURN_DATE\r\n" + 
				"FROM BOOK A, BOOK_RENT B, BOOK_CATEGORY C, LIBRARY L\r\n" + 
				"WHERE A.BOOK_NO=B.BOOK_NO\r\n" + 
				"AND A.CATE_NO=C.CATE_NO\r\n" + 
				"AND A.LIB_NO=L.LIB_NO\r\n" + 
				"AND B.MEM_NO=?";
		return jdbc.selectList(sql, param);		
	}
	// 연장
}
