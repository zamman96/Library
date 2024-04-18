package print;

import java.util.Map;

public class Notice {
	public static final String END = "\u001B[0m";
	public static final String YELLOW = "\u001B[33m";
	public static final String GREEN = "\u001B[32m";
	public static final String RED = "\u001B[31m";

	public static final String tap = "\t\t\t\t\t\t\t";
	public static final String notice = "\t\t\t\t\t\t\t\t\t";
	public static final String t = "\t\t\t\t\t\t";
	public static final String menuU = "\t\t\t\t\t\t╔═════════════════════════════════════════════════════════════════════════════════════════════════╗";
	public static final String menuD = "\t\t\t\t\t\t╚═════════════════════════════════════════════════════════════════════════════════════════════════╝";
	public static final String var = "\t\t\t\t\t\t\t\t\t━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";

	public void noticeLibrarySel() {
		System.out.println("\t"+RED + var + END);
		System.out.println(notice + "     * 알림 * \t ");
		System.out.println();
		System.out.println(notice + "   도서관을 지정해야합니다");
		System.out.println(notice + "   도서관 선택 페이지로 이동합니다");
		System.out.println("\t"+RED + var + END);
	}

	public void noticeMemberSel() {
		System.out.println("\t"+RED + var + END);
		System.out.println(notice + "     * 알림 * \t ");
		System.out.println();
		System.out.println(notice + "   회원만 가능합니다");
		System.out.println(notice + "   로그인 페이지로 이동합니다");
		System.out.println("\t"+RED + var + END);
	}

	public void noticeLibraryNoSel() {
		System.out.println("\t"+RED + var + END);
		System.out.println(notice + "     * 알림 * \t ");
		System.out.println();
		System.out.println(notice + "   도서관을 지정하지 않아");
		System.out.println(notice + "   도서 대출이 불가능합니다");
		System.out.println("\t"+RED + var + END);
	}

	public void noticeNotSearch() {
		System.out.println("\t"+RED + var + END);
		System.out.println(notice + "     * 알림 * \t ");
		System.out.println();
		System.out.println(notice + "   검색결과가 없습니다");
		System.out.println("\t"+RED + var + END);
	}

	public void noticeNotRes() {
		System.out.println("\t"+RED + var + END);
		System.out.println(notice + "     * 알림 * \t ");
		System.out.println();
		System.out.println(notice + "   대출 예약 내역이 없습니다");
		System.out.println("\t"+RED + var + END);
	}

	public void noticeNotRent() {
		System.out.println("\t"+RED + var + END);
		System.out.println(notice + "     * 알림 * \t ");
		System.out.println();
		System.out.println(notice + "   대출 내역이 없습니다");
		System.out.println("\t"+RED + var + END);
	}

	public void noticeNotNo() {
		System.out.println("\t"+RED + var + END);
		System.out.println(notice + "     * 알림 * \t ");
		System.out.println();
		System.out.println(notice + "   잘못된 입력입니다");
		System.out.println("\t"+RED + var + END);
	}

	public void noticeRentPossble(Map<String, Object> map) {
		System.out.println("\t"+RED + var + END);
		System.out.println(notice + "     * 알림 * \t ");
		System.out.println();
		System.out.println(notice + "   대출이 가능한 책입니다" );
		System.out.println(notice + "   위치 : " + map.get("LIB_NAME"));
		System.out.println(notice + "   해당 도서관을 방문하여 대출하세요");
		System.out.println("\t"+RED + var + END);
	}

	public void noticeResDup() {
		System.out.println("\t"+RED + var + END);
		System.out.println(notice + "     * 알림 * \t ");
		System.out.println();
		System.out.println(notice + "   이미 대출 예약한 책입니다" );
		System.out.println(notice + "   이전 페이지로 돌아갑니다");
		System.out.println("\t"+RED + var + END);
	}

	public void noticeRentDup() {
		System.out.println("\t"+RED + var + END);
		System.out.println(notice + "     * 알림 * \t ");
		System.out.println();
		System.out.println(notice + "   이미 대출한 책입니다" );
		System.out.println(notice + "   대출 예약이 불가능합니다");
		System.out.println(notice + "   대출 예약이 불가능합니다");
		System.out.println(notice + "   이전 페이지로 돌아갑니다");
		System.out.println("\t"+RED + var + END);
	}

	public void noticePageUp() {
		System.out.println("\t"+RED + var + END);
		System.out.println(notice + "     * 알림 * \t ");
		System.out.println();
		System.out.println(notice + "   페이지를 벗어났습니다" );
		System.out.println("\t"+RED + var + END);
	}

	public void noticeNotMember() {
		System.out.println("\t"+RED + var + END);
		System.out.println(notice + "     * 알림 * \t ");
		System.out.println();
		System.out.println(notice + "   없는 회원입니다");
		System.out.println(notice + "   확인하고 다시 시도해주세요" );
		System.out.println("\t"+RED + var + END);
	}

	public void noticeNotRight() {
		System.out.println("\t"+RED + var + END);
		System.out.println(notice + "     * 알림 * \t ");
		System.out.println();
		System.out.println(notice + "   권한이 없습니다");
		System.out.println("\t"+RED + var + END);
	}

	public void noticeFirst() {
		System.out.println("\t"+RED + var + END);
		System.out.println(notice + "     * 알림 * \t ");
		System.out.println();
		System.out.println(notice + "   잘못된 입력입니다");
		System.out.println(notice + "   처음으로 돌아갑니다");
		System.out.println("\t"+RED + var + END);
	}

	public void noticeCancel() {
		System.out.println("\t"+RED + var + END);
		System.out.println(notice + "     * 알림 * \t ");
		System.out.println();
		System.out.println(notice + "   취소되었습니다");
		System.out.println(notice + "   이전으로 돌아갑니다");
		System.out.println("\t"+RED + var + END);
	}

	public void noticeNotHaveBook() {
		System.out.println("\t"+RED + var + END);
		System.out.println(notice + "     * 알림 * \t ");
		System.out.println();
		System.out.println(notice + "   도서관에 도서가 없습니다" );
		System.out.println(notice + "   대출가능한 상태에서 시도해주세요" );
		System.out.println("\t"+RED + var + END);
	}

	public void noticeNoOverdue() {
		System.out.println("\t"+RED + var + END);
		System.out.println(notice + "     * 알림 * \t ");
		System.out.println();
		;
		System.out.println(notice + "   연체 정보가 없습니다" );
		System.out.println("\t"+RED + var + END);
	}

	public void noticeSearch() {
		System.out.println("\t"+RED + var + END);
		System.out.println(notice + "     * 알림 * \t ");
		System.out.println();
		System.out.println(notice + "   검색결과가 없습니다");
		System.out.println("\t"+RED + var + END);
	}
}
