package util;

public enum View {
	MAIN,					// 기본화면
	MAIN_MEMBER,			// 도서관선택 X 로그인O 메인화면
	MAIN_LIBRARY,			// 도서관선택 O 로그인X 메인화면
	MAIN_ALL,				// 도서관 선택O 로그인O 메인화면
	
	MEMBER,					// 회원 interface	> 회원정보수정, 회원탈퇴, 책메뉴 호출
	LOGIN,					// 로그인
	LOGOUT,					// 로그아웃ㄴ
	SIGN,					// 회원가입
	FOUND,					// 아이디 비밀번호 찾기
	MYPAGE,
	IDFOUND,
	PWFOUND,
	

	
	BOOK,					// 책  interface > 순위보기, 검색, 대출(로그인 체크), 연장, 반납(로그인 체크&대여상태 체크)
	BOOK_LIST,
	BOOK_CATE_LIST,
	BOOK_SEARCH_LIST,
	
	BOOK_OVERDUE_CHK,
	BOOK_RENT,// 대출
	// 연장
	// 반납
	BOOK_RESERVATION,

	LIBRARY,// 도서관 선택
	LIBRARY_LIST,
	LIBRARY_LOCAL,
	LIBRARY_SEARCH,
	
	ADMIN,					// 관리자	interface > 연체정보, 회원정보, 책추가, 관리자 추가
	
	PDS
	
	
	
}
