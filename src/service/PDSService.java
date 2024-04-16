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
	public void pdsRent(List<Object> param, int libNo){
		pdao.pdsRent(param, libNo);
	}
	
	// 이미 예약되어있는지 여부 파악하기
	/**
	 * @param SEAT_NO, HOUR
	 * @param libNo
	 * @return true 대출가능, false 불가능
	 */
	public boolean pdsRentChk(List<Object> param, int libNo){
		Map<String, Object> map = pdao.pdsRentChk(param, libNo);
		if(map==null) {return true;}
		return false;
	} 
	
}
