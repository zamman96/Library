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
// 책관리
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
	
// 회원 정보
	// 관리자 임명
	/**
	 * @param admin_no, mem_no
	 */
	public void adminUpdate(List<Object> param) {
		adao.adminUpdate(param);
	}
	
	/**
	 * @param ROWNUM
	 * @return
	 */
	public List<Map<String, Object>> memberList(List<Object> param){
		return adao.memberList(param);
	}
	
	public int memberListCount() {
		Map<String, Object> map = adao.memberListCount();
		int count = ((BigDecimal)map.get("COUNT")).intValue();
		return count;
	}
	
	/**
	 * @param ROWNUM
	 * @param sel 1 : 대출한 사람
	 * @param sel 2 : 예약중인 사람
	 * @param sel 3 : 예약대기중인 사람
	 * @param sel 4 : 반납연체
	 * @param sel 5 : 대출불가능 회원
	 * @return
	 */
	public List<Map<String, Object>> memberSearchList(List<Object> param, int sel){
		return adao.memberSearchList(param, sel);
	}
	
	public int memberSearchListCount(int sel) {
		Map<String, Object> map = adao.memberSearchListCount(sel);
		int count = ((BigDecimal)map.get("COUNT")).intValue();
		return count;
	}
	
	public Map<String,Object> memberInfo(int memNo){
		return adao.memberInfo(memNo);
	}
	
	public boolean memberChk(int memNo) {
		Map<String, Object> map = adao.memberInfo(memNo);
		if(map==null) {return false;	//회원정보없음
		}
		return true;
	}
	
	public List<Map<String, Object>> overdueList(int start, int end){
		return adao.overdueList(start, end);
	}
	
	public int overdueListCount(){
		Map<String, Object> map = adao.overdueListCount();
		int count=((BigDecimal)map.get("COUNT")).intValue();
		return count;
	}
}
