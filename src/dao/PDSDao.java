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
	 * @param MEM_NO, HOUR
	 * @param libNo
	 * @param seatNo
	 */
	public void pdsRent(List<Object> param, int libNo, int seatNo){
		String sql = "INSERT INTO PDS_REF(SEAT_NO, MEM_NO,SEAT_REF_HOUR)\r\n" + 
				"SELECT SEAT_NO, ?, ?\r\n" + 
				"FROM PDS_SEAT\r\n" + 
				"WHERE SEAT_NAME='PC"+seatNo+"'\r\n" + 
				"AND LIB_NO="+libNo;
		jdbc.update(sql, param);
	}
	
	// 이미 예약되어있는지 여부 파악하기
	/**
	 * @param SEAT_NO, HOUR
	 * @param libNo
	 * @return
	 */
	public Map<String, Object> pdsRentChk(List<Object> param, int libNo, int seatNo){
		String sql = "SELECT *\r\n" + 
				"FROM PDS_REF\r\n" + 
				"WHERE SEAT_NO IN(\r\n" + 
				"SELECT SEAT_NO\r\n" + 
				"FROM PDS_SEAT\r\n" + 
				"WHERE LIB_NO="+libNo+"\r\n" + 
				"AND SEAT_NAME='PC"+seatNo+"'\r\n" + 
				"AND SEAT_REF_HOUR=?\r\n" + 
				"AND TO_CHAR(SEAT_REF_DATE,'YYYYMMDD') = TO_CHAR(SYSDATE,'YYYYMMDD')\r\n" + 
				"AND SEAT_REF_YN='N')";
		return jdbc.selectOne(sql, param);
	}
	
	
	/** 중복체크
	 * @param HOUR, MEM_NO
	 * @return
	 */
	public Map<String,Object> pdsTimeDupChk(List<Object>param, int libNo){
		String sql = "SELECT *\r\n" + 
				"FROM PDS_REF A, PDS_SEAT B\r\n" + 
				"WHERE TO_CHAR(A.SEAT_REF_DATE,'YYYYMMDD') = TO_CHAR(SYSDATE,'YYYYMMDD')\r\n" + 
				"AND A.SEAT_REF_HOUR=?\r\n" + 
				"AND A.SEAT_REF_YN='N'\r\n" + 
				"AND A.MEM_NO=?"+
				" AND A.SEAT_NO=B.SEAT_NO "
				+ " AND B.LIB_NO="+libNo;
		return jdbc.selectOne(sql, param);
	}
	
	// 현재 도서관의 전체 취소
	/** 현재시간 이후만 가능
	 * @param MEM_NO,libNo
	 */
	public void pdsRentCancelAll(List<Object> param){
		String sql = "UPDATE PDS_REF\r\n" + 
				"SET SEAT_REF_YN='Y'\r\n" + 
				"WHERE MEM_NO=?\r\n" + 
				"AND TO_CHAR(SEAT_REF_DATE,'YYYYMMDD') = TO_CHAR(SYSDATE,'YYYYMMDD')\r\n" + 
				"AND SEAT_REF_HOUR >= TO_CHAR(SYSDATE, 'HH24')\r\n" +
				"AND SEAT_NO IN (\r\n" + 
				"SELECT SEAT_NO\r\n" + 
				"FROM PDS_SEAT\r\n" + 
				"WHERE LIB_NO=?)";
		jdbc.update(sql, param);
	}

	// 부분취소
	/** 현재시간 이후만 가능
	 * @param MEM_NO, LIB_NO
	 * @param seatNo
	 */
	public void pdsRentCancel(List<Object> param, int sel) {
		String sql = "UPDATE PDS_REF\r\n" + 
				"SET SEAT_REF_YN='Y'\r\n" + 
				"WHERE MEM_NO=?\r\n" + 
				"AND TO_CHAR(SEAT_REF_DATE,'YYYYMMDD') = TO_CHAR(SYSDATE,'YYYYMMDD')\r\n" + 
				"AND SEAT_REF_HOUR >= TO_CHAR(SYSDATE, 'HH24')\r\n" + 
				"AND SEAT_NO IN (\r\n" + 
				"SELECT SEAT_NO\r\n" + 
				"FROM PDS_SEAT\r\n" + 
				"WHERE LIB_NO=?\r\n";
		if(sel==1) {
				sql+="AND SEAT_NAME='PC'||? )";}
		if(sel==2) {
			sql+="  AND SEAT_REF_HOUR=? ) ";
		}
		jdbc.update(sql, param);
	}
	
	/**
	 * @param MEM_NO, LIB_NO
	 * @param seatNo
	 * @param sel 2번이면 부분취소이므로 param에 hour도 받아야함
	 * @return
	 */
	public Map<String,Object> pdsResChk(List<Object>param, int sel){
		String sql= "SELECT *\r\n" + 
				"FROM PDS_REF A, PDS_SEAT B\r\n" + 
				"WHERE A.SEAT_NO=B.SEAT_NO\r\n" + 
				"AND A.MEM_NO=?\r\n" + 
				"AND B.LIB_NO=?\r\n";
		if(sel==1) {
				sql+="AND B.SEAT_NAME='PC'||?\r\n";
		}
		if(sel==2) {
				sql+="AND A.SEAT_REF_HOUR=?\r\n";}
				sql+="AND A.SEAT_REF_HOUR >= TO_CHAR(SYSDATE, 'HH24')";
		return jdbc.selectOne(sql, param);
	}

	/**
	 * @param memNo
	 * 회원 삭제시 현재 시간이후의 좌석 예약 삭제
	 */
	public void pdsResDelete(int memNo) {
		String sql ="UPDATE PDS_REF\r\n" + 
				"SET SEAT_REF_YN = 'Y'\r\n" + 
				"WHERE TO_CHAR(SEAT_REF_DATE,'YYYYMMDD')=TO_CHAR(SYSDATE, 'YYYYMMDD')\r\n" + 
				"AND TO_CHAR(SYSDATE, 'HH24') <=SEAT_REF_HOUR\r\n" + 
				"AND MEM_NO="+memNo;
		jdbc.update(sql);
	}
}
