package service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import dao.AdminDao;

public class AdminService {
	private static AdminService instance;

	private AdminService() {

	}

	public static AdminService getInstance() {
		if (instance == null) {
			instance = new AdminService();
		}
		return instance;
	}
	AdminDao adao = AdminDao.getInstance();
	
	// 전체 책 검색 폐기된 것도 조회가능
	// 대출된 책 조회/예약된 책 조회/연체된 책조회
	/**
	 * 세션에 sel input값 pageNo pageEnd저장하고 나갈때 삭제할것
	 * 
	 * @param sel 0 : 전체 조회 > ROWNUM
	 * @param sel a : 책 폐기/사용가능한 것들만 조회 > ROWNUM '사용가능' / '폐기'
	 * @param sel b : 도서관 선택 > ROWNUM libraryList
	 * @param sel c : 분류별 선택 > ROWNUM cate
	 * @param sel d : 이름 검색 > ROWNUM name
	 * @param sel e : 출판사 검색 > ROWNUM pub
	 * @param sel f : 발행년도 검색 > ROWNUM year
	 * @param sel 1 : 대출 가능
	 * @param sel 2 : 대출 예약
	 * @param sel 3 : 대출 불가 (예약한 회원정보도 함께 출력할 것 / 연락처 필수)
	 * 
	 */
	public List<Map<String, Object>> bookAdminList(List<Object> param, String sel) {
		return adao.bookAdminList(param, sel);
	}
	
	public int bookAdminListCount (List<Object> param, String sel) {
		Map<String, Object> map = adao.bookAdminListCount(param, sel);
		int count = ((BigDecimal)map.get("COUNT")).intValue();
		return count;
	}
	
	/**책 이관
	 * @param LIB_NO BOOK_NO
	 */
	public void bookEscalation(List<Object> param) {
		adao.bookEscalation(param);
	}
	

	/**책 상태 변경
	 * @param BOOK_REMARK, BOOK_NO
	 */
	public void bookStateUpdate(List<Object> param) {
		adao.bookStateUpdate(param);
	}
	
	public void bookInsert(List<Object> param, int cateNo) {
		adao.bookInsert(param, cateNo);
	}
	
	public String CateName(int cateNo){
		Map<String,Object> map = adao.CateName(cateNo);
		String name = (String) map.get("CATE_NAME");
		return name;
	}
	
	public String libName (int libNo){
		Map<String,Object> map = adao.libName(libNo);
		String name = (String) map.get("LIB_NAME");
		return name;
	}
}
