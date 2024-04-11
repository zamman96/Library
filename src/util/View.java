package util;

public enum View {
	MAIN,					// 기본화면
	MEMBER,					// 회원 interface	> 회원정보수정, 회원탈퇴, 책메뉴 호출
	LOGIN,					// 로그인
	SIGN,					// 회원가입
	MYPAGE,
	
	BOOKSTATE,				// 책 대출/반납/연장 interface
	
	BOOK,					// 책  interface > 순위보기, 검색, 대출(로그인 체크), 연장, 반납(로그인 체크&대여상태 체크)
	// 대출
	// 연장
	// 반납
	SEARCH,					// 검색
	RANK,					// 순위보기, 카테고리별 순위보기		
	
	SEATREF,
	
	ADMIN					// 관리자	interface > 연체정보, 회원정보, 책추가, 관리자 추가
	
	
	
	
}
