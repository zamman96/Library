package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class PDSDao {
	private static PDSDao instance;

	private PDSDao() {

	}

	public static PDSDao getInstance() {
		if (instance == null) {
			instance = new PDSDao();
		}
		return instance;
	}
	JDBCUtil jdbc = JDBCUtil.getInstance();

	/**
	 * @param libNo
	 * @return 좌석 번호 확인
	 * 반복문 작성을 위해 확인 1~count까지
	 */
	public Map<String,Object> seatCount(int libNo) {
		String sql = "SELECT count(*) as COUNT\r\n" + 
				"FROM PDS_SEAT\r\n" + 
				"WHERE LIB_NO="+libNo;
		return jdbc.selectOne(sql);
	}
	
	/** 전체 좌석 여부확인 가능
	 * @param libNo
	 * @return 
	 * seat_name="PC"+1~0
	 * seat_ref_hour=9~21
	 */
	public List<Map<String,Object>> pdsSeat(int libNo){
		String sql="SELECT *\r\n" + 
				"FROM PDS_REF A, PDS_SEAT B\r\n" + 
				"WHERE A.SEAT_NO=B.SEAT_NO\r\n" + 
				"AND A.SEAT_NO IN (\r\n" + 
				"SELECT SEAT_NO\r\n" + 
				"FROM PDS_SEAT\r\n" + 
				"WHERE LIB_NO="+libNo+")\r\n" + 
				"AND A.SEAT_REF_YN='N'\r\n" + 
				"AND TO_CHAR(A.SEAT_REF_DATE,'YYYYMMDD') = TO_CHAR(SYSDATE,'YYYYMMDD')";
		return jdbc.selectList(sql);
	}
	
	/** 좌석 예약
	 * @param MEM_NO, HOUR, SEAT_NO
	 * @param libNo
	 */
	public void pdsRent(List<Object> param, int libNo){
		String sql = "INSERT INTO PDS_REF(SEAT_NO, MEM_NO,SEAT_REF_HOUR)\r\n" + 
				"SELECT SEAT_NO, ?, ?\r\n" + 
				"FROM PDS_SEAT\r\n" + 
				"WHERE SEAT_NAME='PC'||?\r\n" + 
				"AND LIB_NO="+libNo;
		jdbc.update(sql, param);
	}
	
	// 이미 예약되어있는지 여부 파악하기
	/**
	 * @param SEAT_NO, HOUR
	 * @param libNo
	 * @return
	 */
	public Map<String, Object> pdsRentChk(List<Object> param, int libNo){
		String sql = "SELECT *\r\n" + 
				"FROM PDS_REF\r\n" + 
				"WHERE SEAT_NO IN(\r\n" + 
				"SELECT SEAT_NO\r\n" + 
				"FROM PDS_SEAT\r\n" + 
				"WHERE LIB_NO="+libNo+"\r\n" + 
				"AND SEAT_NAME='PC'||?)\r\n" + 
				"AND SEAT_REF_HOUR=?\r\n" + 
				"AND TO_CHAR(SEAT_REF_DATE,'YYYYMMDD') = TO_CHAR(SYSDATE,'YYYYMMDD')\r\n" + 
				"AND SEAT_REF_YN='N'";
		return jdbc.selectOne(sql, param);
	}
	
	// 전체 취소
	/**
	 * @param SEAT_NO, MEM_NO,SEAT_REF_HOUR
	 * @param libNo
	 * @return
	 */
	public Map<String, Object> pdsRentCancelAll(List<Object> param, int libNo){
		String sql = "UPDATE PDS_REF\r\n" + 
				"SET SEAT_REF_YN='Y'\r\n" + 
				"WHERE SEAT_NO IN (SELECT SEAT_NO\r\n" + 
				"FROM PDS_SEAT\r\n" + 
				"WHERE LIB_NO="+libNo+"\r\n" + 
				"AND SEAT_NAME='PC'||?)\r\n" + 
				"AND SEAT_REF_DATE=SYSDATE\r\n" + 
				"AND MEM_NO=?\r\n" + 
				"AND SEAT_REF_HOUR =?";
		return jdbc.selectOne(sql, param);
	}
	
	
	// 부분취소
	
	
	
}
