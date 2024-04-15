package print;

public class Notice {
	public static final String END = "\u001B[0m";
	public static final String YELLOW = "\u001B[33m";
	public static final String GREEN = "\u001B[32m";
	public static final String RED = "\u001B[31m";

	public void noticeLibrarySel(){
		System.out.println("\t\t"+RED+"┏━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("\t\t┃\t  * 알림 * \t ┃");
		System.out.println("\t\t┃\t\t\t ┃");
		System.out.println("\t\t┃    "+END+"도서관을 지정해야합니다"+RED+"\t\t ┃");
		System.out.println("\t\t┃"+END+" 도서관 선택 페이지로 이동합니다"+RED+"\t┃");
		System.out.println("\t\t┗━━━━━━━━━━━━━━━━━━━━━━━━┛"+END);
	}
	
	public void noticeMemberSel() {
		System.out.println("\t\t"+RED+"┏━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("\t\t┃\t  * 알림 * \t ┃");
		System.out.println("\t\t┃\t\t\t ┃");
		System.out.println("\t\t┃    "+END+"회원만 가능합니다"+RED+"\t\t ┃");
		System.out.println("\t\t┃   "+END+"로그인 페이지로 이동합니다"+RED+"\t\t ┃");
		System.out.println("\t\t┗━━━━━━━━━━━━━━━━━━━━━━━━┛"+END);
	}

	public void noticeLibraryNoSel() {
		System.out.println("\t\t"+RED+"┏━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("\t\t┃\t  * 알림 * \t ┃");
		System.out.println("\t\t┃\t\t\t ┃");
		System.out.println("\t\t┃    "+END+"도서관을 지정하지 않아"+RED+"\t\t ┃");
		System.out.println("\t\t┃    "+END+"도서 대출이 불가능합니다"+RED+"\t\t ┃");
		System.out.println("\t\t┗━━━━━━━━━━━━━━━━━━━━━━━━┛"+END);
	}
}
