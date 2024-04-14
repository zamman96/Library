package service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import dao.BookDao;


/**
 * @author 송예진
 *
 */
public class BookService {
	private static BookService instance;

	private BookService() {

	}

	public static BookService getInstance() {
		if (instance == null) {
			instance = new BookService();
		}
		return instance;
	}
	BookDao bdao = BookDao.getInstance();
	
	
// 회원의 연체 확인
/* 연체 확인 view를 만들 것
 * 회원 자체의 연체 불가능 상태이다 memberOverdueChk를 하고 memberOverdue 를 통해 연체 가능날짜 띄움
 * 연체된 책을 반납하지 않았다  bookOverdueChk를 하고 반납할 위치와 책 정보를 bookOverdue를 통해 띄움
 * 이미 대출한 책이 5권 이상이다 memberRentChk로 확인한 후 "최대 대출 가능한 도서의 수를 넘겼습니다."
 * 이미 대출예약한 책이 3권 이상이다 memberRefChk로 "최대 대출 예약 가능한 도서의 수를 넘겼습니다."
 * 하고 예전의 View상태로 돌아가기
 */
	/**
	 * @param MEM_NO
	 * @return 연체된 책이 있으면 false (대출 불가능)/ 없으면 true (대출가능)
	 */
	public boolean bookOverdueChk(List<Object> param) {
		List<Map<String,Object>> list = bdao.bookOverdue(param);
		if(list==null) {
			return false;
		}
		return true;
	}
	
	/**
	 * @param MEM_NO
	 * @return 현재 대출한 책 중에 연체된 책의 리스트
	 */
	public List<Map<String, Object>> bookOverdue(List<Object> param){
		return bdao.bookOverdue(param);
	}
	
	/**
	 * @param MEM_NO
	 * @return 대출불가하면 false (대출 불가능)/ 가능하면 true (대출가능)
	 */
	public boolean memberOverdueChk(List<Object> param) {
		Map<String, Object> map = bdao.memberOverdue(param);
		if(map==null) {
			return false;
		}
		return true;
	}
	
	/**
	 * @param MEM_NO
	 * @return 연체가능한 일정 뽑기 map.get("RENT_AVADATE")
	 */
	public Map<String,Object> memberOverdue(List<Object> param){
		return bdao.memberOverdue(param);
	}
	
	
	/**
	 * @param BOOK_NO
	 * 대출할 시 book_rent_count 추가할 것
	 */
	public void bookRentCount(List<Object> param) {
		bdao.bookRentCount(param);
	}
	
// 대출
	
	/**
	 * @param MEM_NO
	 * @return 대출 가능권 수
	 */
	public int memberRentVol(List<Object> param){
		Map<String, Object> vol = bdao.memberRentYN(param);
		int num = ((BigDecimal) vol.get("COUNT")).intValue();
		return 5-num;
	}

	/**	회원 관점의 대출 여부 (대출한 권수, 기존 대출 연체 여부, 대출 가능 상태)
	 * @param MEM_NO
	 * @return true > 대출 가능, false > 대출불가능
	 */
	public boolean memberRentChk(List<Object> param) {
		int rentVol = memberRentVol(param);	// 대출 가능 권수
		if(rentVol>0) return true;
		return false;
	}
	
	/**	책 관점의 대출 여부
	 * @param BOOK_NO
	 * @return true > 대출 가능, false > 대출불가능, 대출예약여부확인
	 */
	public boolean bookRentChk(String bookNo) {
		Map<String,Object> book = bdao.bookRentYN(bookNo);
		if(book==null) return false;	// 대출 불가능
		return true;
	}
	
	/**대출하기 (이미 빌리고있는 책이 연체된경우엔 대출 불가능 )
	 * @param BOOK_NO, MEM_NO
	 * @return 반납일 띄우기 위해 book_rent 테이블 값 전체 리턴
	 */
	public Map<String,Object> bookRent(List<Object> param) {
		return bdao.bookRent(param);
	}
	
	
	
// 대출 예약
	
	/**
	 * @param MEM_NO
	 * @return 대출예약 가능 권 수
	 */
	public int memberRefVol(List<Object> param){
		Map<String, Object> vol = bdao.memberRefYN(param);
		int num = ((BigDecimal) vol.get("COUNT")).intValue();
		return 3-num;
	}
	
	/** 회원 관점의 대출 예약 여부 (대출한 권수, 기존 대출 연체 여부, 대출 가능 상태)
	 * @param MEM_NO
	 * @return true > 대출예약 가능, false > 대출예약불가능
	 */
	public boolean memberRefChk(List<Object> param) {
		int refVol = memberRefVol(param);	// 대출 가능 권수
		if(refVol>0) return true;
		return false;
	}
	
	/** 책관점의 예약 중복 확인
	 * @param BOOK_NO, MEM_NO
	 * @return 이미 예약했다면 true, 대출 예약을 진행하는 것은 false
	 */
	public boolean bookRefDupChk(List<Object> param) {
		Map<String, Object> map = bdao.bookRefDup(param);
		if(map==null) return false;
		return true;
	}
	
	/**
	 * @param BOOK_NO
	 * 대출 예약 1번째(book_ref insert)
	 */
	public void bookRefUpdate(List<Object> param) {
		bdao.bookRefUpdate(param);
	}
	
	/**
	 * @param BOOK_NO,MEM_NO
	 *  대출 예약 2번째(book_rent insert)
	 */
	public void bookReservation(List<Object> param) {
		bdao.bookReservation(param);
	}
	
// 대출 예약 대출
	
	/** 만약에 전 순번의 대출 예약자가 예약일+7일 지났다면 예약 가능하므로 리스트 출력
	 * @param MEM_NO
	 * @return ((현재 도서관에서)) 대출 가능한 목록 / null일 경우 빌릴수 있는 예약도서가 없음
	 *  꼭 알림창에 현재 도서관에서 대출 가능한 예약 도서라고 위에 붙일 것!!!
	 */
	public List<Map<String,Object>> bookRefPossList(List<Object> param){
		return bdao.bookRefPossList(param);
	}
	
	/** 예약도서 대출 메소드 
	 * @param BOOK_NO, MEM_NO
	 * @return 반납일을 띄우기 위해 오늘 빌린 책의 정보를 출력
	 */
	public Map<String,Object> bookRefRent(List<Object> param){
		return bdao.bookRefRent(param);
	}
	
	/**	대출예약 기간 지난 리스트
	 * @param MEM_NO
	 * @return 대출 예약 기간이 지나 취소된 리스트 (다른 도서관 포함)
	 * 확인 후 꼭 refTimeOverUpdate 수행
	 */
	public List<Map<String,Object>> refTimeOver(List<Object> param){
		return bdao.refTimeOver(param);
	}
	
	/**	대출예약 기간 지난 리스트를 BOOK_REF_CHK=4번으로 수정
	 * @param MEM_NO
	 * 대출 예약 취소 UPDATE
	 */
	public void refTimeOverUpdate(List<Object> param) {
		bdao.refTimeOverUpdate(param);
	}
	
	/**	대출예약 '대출' 페이지 닫은 후 하는 업데이트 닫기전에
	 * 대출하지않은 도서는 대출예약이 취소됩니다 알림창으로 꼭 물어볼 것
	 * @param MEM_NO, LIB_NO
	 * 현재 보고있는 도서관의 대출되는 예약도서들의 번호를 book_ref_chk=3으로 변경
	 */
	public void bookRefEnd(List<Object> param) {
		bdao.bookRefEnd(param);
	}
	
	/** 대출 예약 리스트
	 * @param MEM_NO
	 * @return 회원의 대출 예약 리스트
	 */
	public List<Map<String, Object>> bookRefList(List<Object> param) {
		return bdao.bookRefList(param);
	}
	
	/** 대출 예약 취소시 사용할 것
	 * @param BOOK_NO, MEM_NO
	 */
	public void bookRefCancel(List<Object> param) {
		bdao.bookRefCancel(param);
	}
	
	
// 대출 연장
	/** 연장 리스트
	 * @param MEM_NO
	 * @return 연장 가능한 책 목록 리스트
	 * 연장 조건 : 반납일까지 7일 남았을 것, 
	 * 연장 메뉴 들어가기 전에 연체여부 (memberRentChk를 하고 만약 false일 시 사용 불가능 전 화면)
	 * 
	 */
	public List<Map<String,Object>> bookDelayList(List<Object> param){
		return bdao.bookDelayList(param);
	}
	
	/** 전체 연장
	 * @param MEM_NO
	 */
	public void bookDelayAll(List<Object> param) {
		bdao.bookDelayAll(param);
	}
	/** 부분 연장
	 * @param BOOK_NO, MEM_NO
	 */
	public void bookDelay(List<Object> param) {
		bdao.bookDelay(param);
	}
	
// 반납
	/**
	 * @param LIB_NO, MEM_NO
	 * @return 반납할 빌린 책 목록 리스트
	 */
	public List<Map<String, Object>> bookReturnList(List<Object> param) {
		return bdao.bookReturnList(param);
	}
	
	/** 전체 반납
	 * @param LIB_NO, MEM_NO
	 */
	public void bookReturnAll(List<Object> param) {
		bdao.bookReturnAll(param);
	}
	/** 부분 반납
	 * @param LIB_NO, BOOK_NO, MEM_NO
	 */
	public void bookReturn(List<Object> param) {
		bdao.bookReturn(param);
	}
	
	/** 전체 반납 전 수행되어야하는 작업
	 * @param MEM_NO
	 * 연체 되니 상태에서 모든 반납한 날짜를 더한 겂을 avadate에 추가
	 * memberOverdue를 이용해 반납가능일자를 출력
	 */
	public void memberOverdueUpdateAll(List<Object> param) {
		bdao.memberOverdueUpdateAll(param);
	}
	
	/** 부분 반납 전 수행되어야하는 작업
	 * @param BOOK_NO, MEM_NO
	 * 연체 된 상태에서 부분 반납시 avadate에 추가
	 * memberOverdue를 이용해 반납가능일자를 출력
	 */
	public void memberOverdueUpdate(List<Object> param) {
		bdao.memberOverdueUpdate(param);
	}
// 책 리스트
	/**
	 * @param bookNo
	 * @return 책의 대출 상태
	 */
	public String bookRentState(String bookNo) {
		Map<String, Object> map = bdao.bookRentYN(bookNo);
		if(map!=null) return "대출가능";
		Map<String,Object> refMap = bdao.bookRefState(bookNo);
		if(refMap!=null) return "대출예약";
		return "대출불가";
	}
	
	/**
	 * @param LIB_NO
	 * @return 한 도서관에서의 전체 순위 10위
	 */
	public List<Map<String,Object>> bookTopList(List<Object> param){
		return bdao.bookTopList(param);
	}
	
	/**
	 * @return 전체 도서관에서의 전체 순위
	 * 
	 */
	public List<Map<String,Object>> bookTopAllList(){
		return bdao.bookTopAllList();
	}
	
	/**O
	 * @param LIB_NO, ROWNUM
	 * @return 한 도서관 전체 리스트
	 */
	public List<Map<String,Object>> bookList(List<Object> param){
		return bdao.bookList(param);
	}
	
	/**O
	 * @param LIB_NO
	 * @return 한 도서관의 전체 도서 리스트 갯수
	 */
	public int bookListCount(List<Object> param){
		Map<String,Object> map = bdao.bookListCount(param);
		int count = ((BigDecimal) map.get("COUNT")).intValue();
		return count;
	}

	/**O
	 * @param ROWNUM
	 * @return 도서관 전체리스트
	 */
	public List<Map<String,Object>> bookAllList(List<Object> param){
		return bdao.bookAllList(param);
	}
	
	/**O
	 * @return 전체 도서관의 전체 도서 갯수
	 */
	public int bookAllListCount(){
		Map<String,Object> map = bdao.bookAllListCount();
		int count = ((BigDecimal) map.get("COUNT")).intValue();
		return count;
	}
	
	public List<Map<String,Object>> cateName(){
		return bdao.cateName();
	}
	
	/**
	 * @param LIB_NO, CATE_NO, ROWNUM
	 * @return 한 도서관의 분류별 책 리스트
	 */
	public List<Map<String,Object>> bookCateList(List<Object> param){
		return bdao.bookCateList(param);
	}
	
	/**
	 * @param LIB_NO, CATE_NO
	 * @return 한 도서관의 분류별 전체 갯수
	 */
	public int bookCateListCount(List<Object> param){
		Map<String,Object> map = bdao.bookCateListCount(param);
		int count = ((BigDecimal) map.get("COUNT")).intValue();
		return count;
	}
	
	/**
	 * @param CATE_NO, ROWNUM
	 * @return 전체 도서관의 분류별 책 리스트
	 */
	public List<Map<String,Object>> bookCateAllList(List<Object> param){
		return bdao.bookCateAllList(param);
	}
	
	/**
	 * @param CATE_NO
	 * @return 전체 도서관의 분류별 전체 갯수
	 */
	public int bookCateAllListCount(List<Object> param){
		Map<String,Object> map = bdao.bookCateAllListCount(param);
		int count = ((BigDecimal) map.get("COUNT")).intValue();
		return count;
	}
	
	/**
	 * @param LIB_NO
	 * @param 1 = 제목, 2 = 작가, 3 = 출판사 검색
	 * @param ROWNUM
	 * @return 한 도서관의 검색결과
	 */
	public List<Map<String,Object>> bookSearchList(List<Object> param, int sel){
		return bdao.bookSearchList(param, sel);
	}
	
	/**
	 * @param LIB_NO
	 * @param 1 = 제목, 2 = 작가, 3 = 출판사 검색
	 * @return 한 도서관의 검색결과 전체 갯수
	 */
	public int bookSearchListCount(List<Object> param, int sel){
		Map<String,Object> map = bdao.bookSearchListCount(param, sel);
		int count = ((BigDecimal) map.get("COUNT")).intValue();
		return count;
	}
	/**
	 * @param 1 = 제목, 2 = 작가, 3 = 출판사 검색
	 * @param ROWNUM
	 * @return 전체 도서관의 도서 검색결과
	 */
	public List<Map<String,Object>> bookSearchAllList(List<Object> param, int sel){
		return bdao.bookSearchAllList(param, sel);
	}
	
	/**
	 * @param 1 = 제목, 2 = 작가, 3 = 출판사 검색
	 * @return 전체 도서관의 검색결과 전체 갯수
	 */
	public int bookSearchAllListCount(List<Object> param, int sel){
		Map<String,Object> map = bdao.bookSearchAllListCount(param, sel);
		int count = ((BigDecimal) map.get("COUNT")).intValue();
		return count;
	}
	
	
}
