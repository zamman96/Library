package service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import dao.PDSDao;

public class PDSService {
	private static PDSService instance;

	private PDSService() {

	}

	public static PDSService getInstance() {
		if (instance == null) {
			instance = new PDSService();
		}
		return instance;
	}
	PDSDao pdao = PDSDao.getInstance();
	
	/** 전체 좌석 여부확인 가능
	 * @param libNo
	 * @return 
	 * seat_name="PC"+1~0
	 * seat_ref_hour=9~21
	 */
	public List<Map<String, Object>> pdsSeat(int libNo){
		return pdao.pdsSeat(libNo);
	}
	
	/**
	 * @param libNo
	 * @return 좌석 번호 확인
	 * 반복문 작성을 위해 확인 1~count까지
	 */
	public int seatCount(int libNo) {
		Map<String, Object> map = pdao.seatCount(libNo);
		int count = ((BigDecimal) map.get("COUNT")).intValue();
		return count;
	}
	
	/** 좌석 예약
	 * @param MEM_NO, HOUR, SEAT_NO
	 * @param libNo
	 */
	public void pdsRent(List<Object> param, int libNo, int seatNo){
		pdao.pdsRent(param, libNo, seatNo);
	}
	
	// 이미 예약되어있는지 여부 파악하기
	/**
	 * @param SEAT_NAME, HOUR
	 * @param libNo
	 * @return true 대출가능, false 불가능
	 */
	public boolean pdsRentChk(List<Object> param, int libNo, int seatNo){
		Map<String, Object> map = pdao.pdsRentChk(param, libNo, seatNo);
		if(map==null) {return true;}
		return false;
	} 
	
	/**
	 * @param HOUR, MEM_NO
	 * @return true 예약가능 
	 */
	public boolean pdsTimeDupChk(List<Object>param, int libNo){
		Map<String,Object> map = pdao.pdsTimeDupChk(param, libNo);
		if(map==null) {return true;}
		return false;
	}
	
	// 현재 도서관의 전체 취소
	/**
	 * @param MEM_NO,libNo
	 */
	public void pdsRentCancelAll(List<Object> param){
		pdao.pdsRentCancelAll(param);
	}
	
	// 부분취소
	/**
	 * @param MEM_NO, LIB_NO
	 * @param seatNo
	 */
	public void pdsRentCancel(List<Object> param, int sel) {
		pdao.pdsRentCancel(param, sel);
	}
	
	/**
	 * @param MEM_NO, LIB_NO
	 * @param seatNo
	 * @param sel 2번이면 부분취소이므로 param에 hour도 받아야함
	 * @return true 취소가능
	 */
	public boolean pdsResChk(List<Object>param, int sel){
		Map<String, Object> map = pdao.pdsResChk(param, sel);
		if(map==null) {return false;}
		return true;
	}
}
