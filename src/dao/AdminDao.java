package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class AdminDao {
	private static AdminDao instance;

	private AdminDao() {

	}

	public static AdminDao getInstance() {
		if (instance == null) {
			instance = new AdminDao();
		}
		return instance;
	}

	JDBCUtil jdbc = JDBCUtil.getInstance();

	// 전체 책 검색 폐기된 것도 조회가능
	/**
	 * 세션에 sel input값 pageNo pageEnd저장하고 나갈때 삭제할것 
	 * @param sel 1 : 전체 조회						> ROWNUM	
	 * @param sel 2 : 책 폐기/사용가능한 것들만 조회			> ROWNUM	'사용가능' / '폐기'
	 * @param sel 3 : 도서관 선택						> ROWNUM	libraryList
	 * @param sel 4 : 분류별 선택						> ROWNUM	cate
	 * @param sel 5 : 이름 검색						> ROWNUM	name
	 * @param sel 6 : 출판사 검색						> ROWNUM	pub
	 * @param sel 7 : 발행년도 검색						> ROWNUM	year
	 * 
	 */
	public List<Map<String, Object>> bookAdminList(List<Object> param, int sel, String input){
		String sql = "SELECT *\r\n" + 
				"FROM (\r\n" + 
				"SELECT ROWNUM AS RN,A.BOOK_NO, A.BOOK_NAME, A.BOOK_AUTHOR, A.BOOK_PUB, A.BOOK_PUB_YEAR, B.CATE_NAME, C.LIB_NAME\r\n" + 
				"FROM BOOK A, BOOK_CATEGORY B, LIBRARY C\r\n" + 
				"WHERE A.CATE_NO=B.CATE_NO\r\n" + 
				"AND A.LIB_NO=C.LIB_NO\r\n";
			if(sel==2) {
				sql+=" AND A.BOOK_REMARK="+input;
			}
			if(sel==3) {
			sql+="   AND A.LIB_NO="+input;
			}
			if(sel==4) {
				sql+=" AND A.CATE_NO="+input;
			}
			if(sel==5) {
				sql+=" AND A.BOOK_NAME LIKE '%"+input+"%' ";
			}
			if(sel==6) {
				sql+=" AND A.BOOK_PUB LIKE '%"+input+"%' ";
			}
			if(sel==7) {
				sql+=" AND A.BOOK_PUB_DATE LIKE '%"+input+"%' ";
			}
				sql+="ORDER BY A.LIB_NO, A.CATE_NO, A.BOOK_NO)\r\n" + 
				"WHERE RN>=? AND RN<=?";
				return jdbc.selectList(sql, param);
	} 

	// 대출된 책 조회/예약된 책 조회/연체된 책조회
	/**
	 * @param ROWNUM		세션에 page랑 sel저장
	 * @param sel = 1 대출 가능	
	 * @param sel = 2 대출 예약 
	 * @param sel = 3 대출 불가 (예약한 회원정보도 함께 출력할 것 / 연락처 필수)
	 * @return String state = bookService.bookRentState(bookNo);으로  상태표현
	 */
	public List<Map<String, Object>> bookAdminStateList(List<Object> param, int sel){
		String sql = "SELECT *\r\n" + 
				"FROM(SELECT ROWNUM AS RN, A.BOOK_NO, A.BOOK_NAME, A.BOOK_AUTHOR, A.BOOK_PUB, A.BOOK_PUB_YEAR, \r\n" + 
				"       B.CATE_NAME, C.LIB_NAME, D.RENT_DATE, D.RETURN_DATE, F.MEM_NAME, F.MEM_TELNO\r\n" + 
				"FROM BOOK A\r\n" + 
				"INNER JOIN BOOK_CATEGORY B ON A.CATE_NO = B.CATE_NO\r\n" + 
				"INNER JOIN LIBRARY C ON A.LIB_NO = C.LIB_NO\r\n" + 
				"LEFT JOIN BOOK_RENT D ON A.BOOK_NO = D.BOOK_NO\r\n" + 
				"LEFT JOIN BOOK_REF E ON D.BOOK_REF_NO = E.BOOK_REF_NO\r\n" + 
				"LEFT JOIN MEMBER F ON D.MEM_NO = F.MEM_NO\r\n";
				if(sel==3) {
				sql+="WHERE D.RENT_DATE IS NOT NULL\r\n" + 
				"AND D.RETURN_YN = 'N'\r\n";
				}
				if(sel==2) {
				sql+="WHERE D.BOOK_REF_CHK=1           \r\n";
				}
				if(sel==1) {
				sql+="WHERE A.BOOK_NO NOT IN(\r\n" + 
				"    SELECT A.BOOK_NO\r\n" + 
				"    FROM BOOK A\r\n" + 
				"    INNER JOIN BOOK_CATEGORY B ON A.CATE_NO = B.CATE_NO\r\n" + 
				"    INNER JOIN LIBRARY C ON A.LIB_NO = C.LIB_NO\r\n" + 
				"    LEFT JOIN BOOK_RENT D ON A.BOOK_NO = D.BOOK_NO\r\n" + 
				"    LEFT JOIN BOOK_REF E ON D.BOOK_REF_NO = E.BOOK_REF_NO\r\n" + 
				"    LEFT JOIN MEMBER F ON D.MEM_NO = F.MEM_NO\r\n" + 
				"    WHERE D.RENT_DATE IS NOT NULL     --  대출불가\r\n" + 
				"    AND D.RETURN_YN = 'N')\r\n" + 
				"AND A.BOOK_NO NOT IN (\r\n" + 
				"    SELECT A.BOOK_NO\r\n" + 
				"    FROM BOOK A\r\n" + 
				"    INNER JOIN BOOK_CATEGORY B ON A.CATE_NO = B.CATE_NO\r\n" + 
				"    INNER JOIN LIBRARY C ON A.LIB_NO = C.LIB_NO\r\n" + 
				"    LEFT JOIN BOOK_RENT D ON A.BOOK_NO = D.BOOK_NO\r\n" + 
				"    LEFT JOIN BOOK_REF E ON D.BOOK_REF_NO = E.BOOK_REF_NO\r\n" + 
				"    LEFT JOIN MEMBER F ON D.MEM_NO = F.MEM_NO\r\n" + 
				"    WHERE D.BOOK_REF_CHK=1 \r\n" + 
				")\r\n";
				}
				sql+=")WHERE RN>=? AND RN<=?";
			return jdbc.selectList(sql, param);
	}
	

	// 이관 폐기 (도서관변경, 이관/폐기) (도서관 변경은 대출가능시에만 이동가능)
	/**
	 * @param sel = 1(도서이관)	> LIB_NO, BOOK_NO 
	 * @param sel = 2(폐기)		> BOOK_NO                       
	 * @param sel = 3(사용가능)	> BOOK_NO
	 */
	public void bookUpdate(List<Object> param, int sel) {
		String sql = "UPDATE BOOK\r\n";
		if (sel == 1) {
			sql += "SET LIB_NO=?\r\n";
		}
		if (sel == 2) {
			sql += "SET BOOK_REMARK='폐기'\r\n";
		}
		if (sel == 3) {
			sql += "SET BOOK_REMARK='사용가능'\r\n";
		}
		sql += "WHERE BOOK_NO = ?";
		jdbc.update(sql, param);
	}

	// 책 추가

	/*
	 * 카테고리 리스트를 뽑아서 누르기 List<Map<String, Object>> cate = bookService.cateName();
	 * printCateName(cate);
	 * 
	 * librarylist() > LibraryController 에서 뽑아서 직접 선택하게 할 것
	 * 
	 */
	/**
	 * 모든 정보를 추가한 후 확인절차를 마친 뒤 저장하기 (while 문 break)
	 * 책 내용은 이후 수정할 수 없습니다
	 * 정확히 작성하고 저장해주세요
	 * 
	 * @param BOOK_NAME, BOOK_AUTHOR, BOOK_PUB, BOOK_PUB_YEAR, LIB_NO
	 * @param cateNo
	 */
	public void bookInsert(List<Object> param, int cateNo) {
		String sql = "INSERT INTO BOOK (BOOK_NO, BOOK_NAME. BOOK_AUTHOR, BOOK_PUB, BOOK_PUB_YEAR, CATE_NO, LIB_NO)\r\n"
				+ "SELECT MAX(BOOK_NO)+1,?, ?, ?, ?, " + cateNo + ", ?\r\n" + "FROM BOOK\r\n" + "WHERE CATE_NO="
				+ cateNo;
		jdbc.update(sql, param);
	}
}
