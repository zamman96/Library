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
	
	/** O
	 * @param param MEM_NO
	 * @return 대출한 권 수
	 * 대출 조건 : 회원이 대출이 가능한지 확인 대출가능 권수 5-COUNT값이<1이면 대출 불가능   (회원대출가능여부)
	 */
	public Map<String, Object> memberRentYN(List<Object> param){
		String sql = "SELECT COUNT(*) AS COUNT \r\n" + 
				"FROM BOOK_RENT\r\n" + 
				"WHERE RETURN_YN='N'\r\n" + 
				"AND MEM_NO=?";
		return jdbc.selectOne(sql, param);
	}
	
	/** O
	 * @param BOOK_NO
	 * @return 대출가능할 경우 책 RETURN 없을 경우 NULL > 대출예약 여부
	 * 이미 대출예약이된 책이거나 대출이된 책을 제외한 리스트의 책 검색 조건
	 */
	public Map<String, Object> bookRentYN(String bookNo){
		String sql = "SELECT A.*\r\n" + 
				"FROM BOOK A\r\n" + 
				"LEFT OUTER JOIN BOOK_RENT B ON (A.BOOK_NO=B.BOOK_NO)\r\n" + 
				"WHERE A.BOOK_NO NOT IN (SELECT BOOK_NO\r\n" + 
				"FROM BOOK_RENT\r\n" + 
				"WHERE RETURN_YN='N')\r\n" + 
				"AND A.BOOK_NO NOT IN (SELECT DISTINCT A.BOOK_NO\r\n" + 
				"FROM BOOK_RENT A, BOOK_REF B\r\n" + 
				"WHERE A.BOOK_REF_NO=B.BOOK_REF_NO AND A.BOOK_REF_CHK=1)\r\n" + 
				"AND BOOK_REMARK='사용가능'\r\n" + 
				"AND A.BOOK_NO="+bookNo;
		return jdbc.selectOne(sql);
	}
/////////////////// 확인필요
	
	/**
	 * @param bookNo
	 * @return 대출되지않은대출 예약중인 도서
	 */
	public Map<String,Object> bookRefState(String bookNo){
		String sql = "SELECT A.*  \r\n" + 
				"				FROM BOOK A  \r\n" + 
				"				LEFT OUTER JOIN BOOK_RENT B ON (A.BOOK_NO=B.BOOK_NO)  \r\n" + 
				"				WHERE A.BOOK_NO NOT IN (SELECT BOOK_NO  \r\n" + 
				"				FROM BOOK_RENT  \r\n" + 
				"				WHERE RETURN_YN='N')  \r\n" + 
				"				AND A.BOOK_NO IN (SELECT DISTINCT A.BOOK_NO  \r\n" + 
				"				FROM BOOK_RENT A, BOOK_REF B  \r\n" + 
				"				WHERE A.BOOK_REF_NO=B.BOOK_REF_NO AND A.BOOK_REF_CHK=1)  \r\n" + 
				"				AND BOOK_REMARK='사용가능'  \r\n" + 
				"				AND A.BOOK_NO="+bookNo;
		return jdbc.selectOne(sql);
	}
	
	/** O
	 * @param param MEM_NO
	 * @return 대출 예약 갯수
	 * 대출 예약 조건 : 대출 예약은 3권까지 가능
	 */
	public Map<String, Object> memberRefYN(List<Object> param){
		String sql = "SELECT COUNT(*) AS COUNT\r\n" + 
				"FROM BOOK_RENT A, BOOK_REF B\r\n" + 
				"WHERE B.BOOK_REF_NO = A.BOOK_REF_NO\r\n" + 
				"AND A.BOOK_REF_CHK=1\r\n" + 
				"AND A.MEM_NO=?";
		return jdbc.selectOne(sql, param);
	}
	
	/** O
	 * @param param BOOK_NO, MEM_NO
	 * @return 이미 예약했다면 1  없다면  null
	 */
	public Map<String, Object> bookRefDup(List<Object> param){
		String sql = "SELECT 1\r\n" + 
				"FROM BOOK_RENT\r\n" + 
				"WHERE BOOK_REF_CHK=1\r\n" + 
				"AND BOOK_NO=?\r\n" + 
				"AND MEM_NO=?";
		return jdbc.selectOne(sql, param);
	}
	
	
	/** O
	 * @param param BOOK_NO,MEM_NO
	 * 대출 예약 2번째 수행 (book_rent insert)
	 */
	public void bookReservation(List<Object> param) {
		String sql = "    SELECT NVL(MAX(BOOK_REF_NO),0) AS NO\r\n" + 
				"    FROM BOOK_REF";
		int no = ((BigDecimal) jdbc.selectOne(sql).get("NO")).intValue();
		
		sql = "    INSERT INTO BOOK_RENT(BOOK_NO, MEM_NO, BOOK_REF_NO, BOOK_REF_CHK,RENT_NO)\r\n" + 
				"SELECT ?, ?,"+no+",1,NVL(MAX(RENT_NO),0)+1\r\n" + 
				"FROM BOOK_RENT)";
		jdbc.update(sql, param);
	}
	
	/** O
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
	 
	/**	대출하기 O
	 * @param param = BOOK_NO, MEM_NO
	 * @return 오늘 빌린 책의 반납일 띄우기 위해 book_rent 테이블 값 전체 리턴 
	 */
	public Map<String, Object> bookRent(List<Object> param) {
		String sql = "INSERT INTO BOOK_RENT(BOOK_NO,MEM_NO,RENT_DATE,RETURN_DATE,DELAY_YN,RETURN_YN,RENT_NO)\r\n" + 
				"SELECT ?, ?, SYSDATE,SYSDATE+14,'N','N',NVL(MAX(RENT_NO),0)+1\r\n" + 
				"FROM BOOK_RENT";
		jdbc.update(sql, param);
		sql = "SELECT *\r\n" + 
				"FROM BOOK_RENT\r\n" + 
				"WHERE BOOK_NO=?\r\n" + 
				"AND MEM_NO=?\r\n" + 
				"AND TO_CHAR(RENT_DATE,'YYYYMMDD')=TO_CHAR(SYSDATE,'YYYYMMDD')";
		return jdbc.selectOne(sql, param);
	}
	

	/** O
	 * @param param MEM_NO
	 * @return 대출예약 취소된 리스트 (다른 도서관 포함)
	 */
	public List<Map<String,Object>> refTimeOver(List<Object> param){
		String sql = "SELECT *\r\n" + 
				"    FROM BOOK_REF A, BOOK_RENT B, BOOK C, BOOK_CATEGORY D, LIBRARY F\r\n" + 
				"    WHERE A.BOOK_REF_NO=B.BOOK_REF_NO\r\n" + 
				"    AND B.BOOK_REF_CHK=1\r\n" + 
				"    AND A.BOOK_REF_DATE+7<SYSDATE\r\n" + 
				"    AND B.BOOK_NO=C.BOOK_NO\r\n" + 
				"    AND D.CATE_NO=C.CATE_NO\r\n" + 
				"    AND C.LIB_NO=F.LIB_NO\r\n" + 
				"    AND B.MEM_NO = ?";
		return jdbc.selectList(sql, param);
	}
	
	/** O
	 * @param param MEM_NO
	 * 대출 예약 취소 UPDATE refTimeOver 이후 실행
	 */
	public void refTimeOverUpdate(List<Object> param) {
		String sql = "UPDATE BOOK_RENT A\r\n" + 
				"SET BOOK_REF_CHK=4\r\n" + 
				"WHERE BOOK_REF_NO IN (SELECT BOOK_REF_NO\r\n" + 
				"                        FROM BOOK_REF\r\n" + 
				"                        WHERE BOOK_REF_DATE+7<SYSDATE)\r\n" + 
				"AND MEM_NO=?";
		jdbc.update(sql, param);
	}
	
	/**	O
	 * 만약에 전 사람의 대출 예약날짜 이후로 7일이 지났다면 예약가능 
	 * @param param = MEM_NO
	 * @return 현재 도서관에서 대출 가능한 목록 / null일 경우 빌릴수 있는 예약도서가 없음
	 * 예약할지 여부를 물어본 뒤 (현재 방문한 도서관에 있는 걸) BOOK_REF_CHK 변경해야함
	 */
	public List<Map<String, Object>> bookRefPossList(List<Object> param){
		String sql = "SELECT *\r\n" + 
				"FROM BOOK_REF A, BOOK_RENT B,\r\n" + 
				"    (SELECT B.BOOK_NO AS MNO, MIN(A.BOOK_REF_SEQ) AS MSEQ\r\n" + 
				"    FROM BOOK_REF A, BOOK_RENT B\r\n" + 
				"    WHERE A.BOOK_REF_NO=B.BOOK_REF_NO\r\n" + 
				"    AND B.BOOK_REF_CHK=1\r\n" + 
				"    AND A.BOOK_REF_DATE+7>SYSDATE\r\n" + 
				"    GROUP BY B.BOOK_NO) C, BOOK D, BOOK_CATEGORY F\r\n" + 
				"WHERE A.BOOK_REF_NO=B.BOOK_REF_NO\r\n" + 
				"AND C.MNO=B.BOOK_NO\r\n" + 
				"AND A.BOOK_REF_SEQ=C.MSEQ\r\n" + 
				"AND B.BOOK_NO=D.BOOK_NO\r\n" + 
				"AND D.CATE_NO=F.CATE_NO\r\n" + 
				"AND B.MEM_NO=?\r\n" + 
				"AND D.LIB_NO=?";
		return jdbc.selectList(sql, param);
	}
	// 다른 도서관에 예약도서 예약가능 알림을 띄울것
	
	/** O
	 * @param param = BOOK_NO, MEM_NO
	 * @return 반납일을 띄우기 위해 오늘 빌린 책의 정보를 출력
	 * 예약도서 대출 메소드 
	 */
	public Map<String, Object> bookRefRent(List<Object> param){
		String sql = "UPDATE BOOK_RENT\r\n" + 
				"SET RENT_DATE=SYSDATE,\r\n" + 
				"        RETURN_DATE = SYSDATE+14,\r\n" + 
				"        DELAY_YN='N',\r\n" + 
				"        RETURN_YN='N',\r\n" + 
				"        BOOK_REF_CHK=2\r\n" + 
				"WHERE BOOK_REF_NO IN (SELECT A.BOOK_REF_NO\r\n" + 
				"                                              FROM BOOK_REF A, BOOK_RENT B\r\n" + 
				"                                            WHERE A.BOOK_REF_NO=B.BOOK_REF_NO\r\n" + 
				"                                                 AND B.BOOK_REF_CHK=1\r\n" + 
				"                                                 AND B.BOOK_NO=?\r\n" + 
				"                                                AND B.MEM_NO=?)";
		jdbc.update(sql, param);
		sql = "SELECT *\r\n" + 
				"FROM BOOK_RENT\r\n" + 
				"WHERE BOOK_NO=?\r\n" + 
				"AND MEM_NO=?\r\n" + 
				"AND TO_CHAR(RENT_DATE,'YYYYMMDD')=TO_CHAR(SYSDATE,'YYYYMMDD')";
		return jdbc.selectOne(sql, param);
	}
	
	/**	O
	 * book_ref_chk=3(체크) 꼭 나가기전에 되묻기   (현재 도서관에 있는것만)
	 * @param param = MEM_NO, LIB_NO
	 */
	public void bookRefEnd(List<Object> param) {
		String sql = "UPDATE BOOK_RENT\r\n" + 
				"SET BOOK_REF_CHK=3\r\n" + 
				"WHERE BOOK_REF_NO IN (SELECT A.BOOK_REF_NO\r\n" + 
				"                                              FROM BOOK_REF A, BOOK_RENT B, BOOK C\r\n" + 
				"                                            WHERE A.BOOK_REF_NO=B.BOOK_REF_NO\r\n" + 
				"                                                 AND B.BOOK_NO=C.BOOK_NO\r\n" + 
				"                                                 AND B.BOOK_REF_CHK=1\r\n" + 
				"                                                AND B.MEM_NO=?\r\n" + 
				"                                                AND C.LIB_NO=?)";
		jdbc.update(sql, param);
	}
	
	/** O
	 * @param param MEM_NO
	 * @return 회원의 대출 예약 리스트
	 * 대출 예약 취소를 위해 만듬 
	 */
	public List<Map<String, Object>> bookRefList(List<Object> param){
		String sql = "SELECT *\r\n" + 
				"FROM BOOK A, BOOK_CATEGORY B, BOOK_RENT C, BOOK_REF D, LIBRARY E\r\n" + 
				"WHERE A.CATE_NO=B.CATE_NO\r\n" + 
				"AND A.BOOK_NO=C.BOOK_NO\r\n" + 
				"AND C.BOOK_REF_NO=D.BOOK_REF_NO\r\n" + 
				"AND A.LIB_NO=E.LIB_NO\r\n" + 
				"AND C.BOOK_REF_CHK=1\r\n" + 
				"AND C.MEM_NO=?";
		return jdbc.selectList(sql, param);
	}
	// 예약 취소
	/** O
	 * @param param BOOK_NO, MEM_NO
	 * 예약 취소 book_ref_chk=3
	 */
	public void bookRefCancel(List<Object> param) {
		String sql = "UPDATE BOOK_RENT\r\n" + 
				"SET BOOK_REF_CHK=3\r\n" + 
				"WHERE BOOK_REF_NO IN(\r\n" + 
				"SELECT BOOK_REF_NO\r\n" + 
				"FROM BOOK_RENT\r\n" + 
				"WHERE BOOK_NO=?\r\n" + 
				"AND MEM_NO=?\r\n" + 
				"AND BOOK_REF_CHK=1\r\n" + 
				")";
		jdbc.update(sql, param);
	}
	
	
	// 연장 (다른 도서관에서도 가능) 연장메뉴와 반납 메뉴를 다르게 둘것!
	/**
	 * @param param MEM_NO
	 * @return 연장 가능한 책 목록 리스트
	 * 연장 조건 : 반납일까지 7일 남았을 것, 
	 * 연장 메뉴 들어가기 전에 연체를 확인하고 연체일경우엔 연장메뉴로 넘어가지않음 반납일이 넘지않았을 것
	 */
	public List<Map<String,Object>> bookDelayList(List<Object> param) {
		String sql = "SELECT *\r\n" + 
				"FROM BOOK A, BOOK_RENT B, BOOK_CATEGORY C, LIBRARY L\r\n" + 
				"WHERE A.BOOK_NO=B.BOOK_NO\r\n" + 
				"AND A.CATE_NO=C.CATE_NO\r\n" + 
				"AND A.LIB_NO=L.LIB_NO\r\n" + 
				"AND B.DELAY_YN='N'\r\n" + 
				"AND B.RENT_DATE+7<SYSDATE\r\n" + 
				"AND B.MEM_NO=?";
		return jdbc.selectList(sql, param);
	}
	
	// 전체 연장
	/**O
	 * @param param MEM_NO
	 */
	public void bookDelayAll(List<Object> param) {
		String sql = "UPDATE BOOK_RENT\r\n" + 
				"SET DELAY_YN = 'Y',\r\n" + 
				"        RETURN_DATE=RETURN_DATE+7\r\n" + 
				"WHERE RENT_NO IN(\r\n" + 
				"SELECT RENT_NO\r\n" + 
				"FROM BOOK_RENT\r\n" + 
				"WHERE DELAY_YN='N'\r\n" + 
				"AND RENT_DATE+7<SYSDATE\r\n" + 
				"AND MEM_NO=?)";
		jdbc.update(sql,param);
	}
	
	// 부분 연장 
	/**O
	 * @param param BOOK_NO, MEM_NO
	 */
	public void bookDelay(List<Object> param) {
		String sql ="UPDATE BOOK_RENT\r\n" + 
				"SET DELAY_YN = 'Y',\r\n" + 
				"        RETURN_DATE=RETURN_DATE+7\r\n" + 
				"WHERE RENT_NO IN(\r\n" + 
				"SELECT RENT_NO\r\n" + 
				"FROM BOOK_RENT\r\n" + 
				"WHERE DELAY_YN='N'\r\n" + 
				"AND RENT_DATE+7<SYSDATE\r\n" + 
				"AND BOOK_NO=?\r\n" + 
				"AND MEM_NO=?)";
		jdbc.update(sql,param);
	}
	
	/** O
	 * @param param MEM_NO
	 * @return 연체된 책 리스트 (책이름과 도서관 이름)
	 * 어느 도서관에서 어떤 책을 반납해야되는 지도 리스트 뽑기 위해 리스트 출력
	 */
	public List<Map<String,Object>> bookOverdue(List<Object> param){
		String sql ="SELECT *\r\n" + 
				"FROM BOOK_RENT A, BOOK B, LIBRARY C, BOOK_CATEGORY D\r\n" + 
				"WHERE RETURN_DATE<SYSDATE\r\n" + 
				"AND A.BOOK_NO=B.BOOK_NO\r\n" + 
				"AND B.LIB_NO=C.LIB_NO\r\n" + 
				"AND B.CATE_NO=D.CATE_NO\r\n" + 
				"AND A.RETURN_YN='N'\r\n" + 
				"AND A.MEM_NO=?";
		return jdbc.selectList(sql, param);
		
	}
	
	/** O
	 * @param param MEM_NO
	 * @return 연체가 되었는지 아닌지 확인 null이면 대출가능
	 * 회원의 연체가능날짜 출력 예정
	 */
	public Map<String,Object> memberOverdue(List<Object> param){
		String sql = "SELECT *\r\n" + 
				"FROM MEMBER\r\n" + 
				"WHERE RENT_AVADATE>SYSDATE\r\n" + 
				"WHERE MEM_NO=?";
		return jdbc.selectOne(sql, param);
	}
	
	/**O	
	 * 반납해야할 책 목록
	 * @param param LIB_NO, MEM_NO
	 * @return 빌린 책 목록 리스트
	 */
	public List<Map<String,Object>> bookReturnList(List<Object> param) {
		String sql = "SELECT *\r\n" + 
				"FROM BOOK A, BOOK_RENT B, BOOK_CATEGORY C, LIBRARY L\r\n" + 
				"WHERE A.BOOK_NO=B.BOOK_NO\r\n" + 
				"AND A.CATE_NO=C.CATE_NO\r\n" + 
				"AND A.LIB_NO=L.LIB_NO\r\n" + 
				"AND A.LIB_NO=?\r\n" + 
				"AND B.MEM_NO=?";
		return jdbc.selectList(sql, param);		
	}
	
	//전체 반납 update
	/** O
	 * @param param LIB_NO, MEM_NO
	 */
	public void bookReturnAll(List<Object> param) {
		String sql = "UPDATE BOOK_RENT\r\n" + 
				"SET RETURN_DATE=SYSDATE,\r\n" + 
				"        RETURN_YN='Y'\r\n" + 
				"WHERE RENT_NO IN (\r\n" + 
				"SELECT RENT_NO\r\n" + 
				"FROM BOOK_RENT A, BOOK B\r\n" + 
				"WHERE A.BOOK_NO=B.BOOK_NO\r\n" + 
				"AND B.LIB_NO =?\r\n" + 
				"AND A.MEM_NO=?\r\n" + 
				"AND RETURN_YN='N')";
	}
	//부분 반납 update
	/** O
	 * @param param LIB_NO, BOOK_NO, MEM_NO
	 */
	public void bookReturn(List<Object> param) {
		String sql = "UPDATE BOOK_RENT\r\n" + 
				"SET RETURN_DATE=SYSDATE,\r\n" + 
				"        RETURN_YN='Y'\r\n" + 
				"WHERE RENT_NO IN (\r\n" + 
				"SELECT RENT_NO\r\n" + 
				"FROM BOOK_RENT A, BOOK B\r\n" + 
				"WHERE A.BOOK_NO=B.BOOK_NO\r\n" + 
				"AND B.LIB_NO =1\r\n" + 
				"AND A.BOOK_NO='10001'\r\n" + 
				"AND A.MEM_NO=1\r\n" + 
				"AND RETURN_YN='N')";
	}
	
	
	// 연체시 member avadate 업데이트 
	/**O
	 * @param param MEM_NO
	 * 연체 시에 전체 반납시 UPDATE
	 */
	public void memberOverdueUpdateAll(List<Object> param) {
		String sql="UPDATE MEMBER\r\n" + 
				"SET RENT_AVADATE=(\r\n" + 
				"SELECT SYSDATE+SUM(SYSDATE-RETURN_DATE)*2\r\n" + 
				"FROM BOOK_RENT A, BOOK B, LIBRARY C, BOOK_CATEGORY D\r\n" + 
				"WHERE RETURN_DATE<SYSDATE\r\n" + 
				"AND A.BOOK_NO=B.BOOK_NO\r\n" + 
				"AND B.LIB_NO=C.LIB_NO\r\n" + 
				"AND B.CATE_NO=D.CATE_NO\r\n" + 
				"AND A.RETURN_YN='N'\r\n" + 
				"WHERE MEM_NO=?";
		jdbc.update(sql, param);
		
	}
	
	/**O
	 * @param param BOOK_NO, MEM_NO
	 * 연체 시 부분 반납시 UPDATE
	 */
	public void memberOverdueUpdate(List<Object> param) {
		String sql = "UPDATE MEMBER\r\n" + 
				"SET RENT_AVADATE=(\r\n" + 
				"SELECT SYSDATE+SUM(SYSDATE-RETURN_DATE)*2\r\n" + 
				"FROM BOOK_RENT A, BOOK B, LIBRARY C, BOOK_CATEGORY D\r\n" + 
				"WHERE RETURN_DATE<SYSDATE\r\n" + 
				"AND A.BOOK_NO=B.BOOK_NO\r\n" + 
				"AND B.LIB_NO=C.LIB_NO\r\n" + 
				"AND B.CATE_NO=D.CATE_NO\r\n" + 
				"AND A.RETURN_YN='N'\r\n" + 
				"AND A.BOOK_NO=?)\r\n" + 
				"WHERE MEM_NO=?";
		jdbc.update(sql, param);
	}
	
	/**O
	 * @param param BOOK_NO
	 * 대출 insert시에 써야함 (대출 / 대출 예약)
	 */
	public void bookRentCount(List<Object>param) {
		String sql = "UPDATE BOOK\r\n" + 
				"SET BOOK_RENT_COUNT=BOOK_RENT_COUNT+1\r\n" + 
				"WHERE BOOK_NO = ?";
		jdbc.update(sql, param);
	}

/////////////////////////////////////////
	/**
	 * @param param LIB_NO
	 * @return 한 도서관에서의 전체 순위 10위
	 */
	public List<Map<String,Object>> bookTopList(List<Object> param){
		String sql = "SELECT A.*\r\n" + 
				"FROM(SELECT A.BOOK_NO, A.BOOK_NAME, A.BOOK_AUTHOR, A.BOOK_PUB, A.BOOK_PUB_YEAR, B.CATE_NAME, A.BOOK_RENT_COUNT\r\n" + 
				"FROM BOOK A, BOOK_CATEGORY B\r\n" + 
				"WHERE A.CATE_NO=B.CATE_NO\r\n" + 
				"AND A.BOOK_REMARK='사용가능'\r\n" + 
				"AND A.LIB_NO=?\r\n" + 
				"ORDER BY A.BOOK_RENT_COUNT DESC)A\r\n" + 
				"WHERE ROWNUM<=10";
		return jdbc.selectList(sql, param);
	}
	
//	/**
//	 * @param param LIB_NO, CATE_NO
//	 * @return 한 도서관의 분류별 순위 5위
//	 */
//	public List<Map<String,Object>> bookCateTopList(List<Object> param){
//		String sql = "SELECT A.*\r\n" + 
//				"FROM(SELECT A.BOOK_NO, A.BOOK_NAME, A.BOOK_AUTHOR, A.BOOK_PUB, A.BOOK_PUB_YEAR, B.CATE_NAME, A.BOOK_RENT_COUNT\r\n" + 
//				"FROM BOOK A, BOOK_CATEGORY B\r\n" + 
//				"WHERE A.CATE_NO=B.CATE_NO\r\n" + 
//				"AND A.BOOK_REMARK='사용가능'\r\n" + 
//				"AND A.LIB_NO=?\r\n" + 
//				"AND A.CATE_NO=?\r\n" + 
//				"ORDER BY A.BOOK_RENT_COUNT DESC)A\r\n" + 
//				"WHERE ROWNUM<=5";
//	}

}
