package print;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.BookService;

public class Print extends Notice {
	BookService bookService = BookService.getInstance();
	public static final String END = "\u001B[0m";
	public static final String YELLOW = "\u001B[33m";
	public static final String GREEN = "\u001B[32m";

	public void printVar() {
		System.out.println(
				"─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
	}

	public void printBookIndex() {
		System.out.println("도서관이름\t\t도서 상태\t도서 번호\t분류\t제목\t\t\t\t작가\t\t\t출판사\t\t출판년도");
	}

	public void printBookList(Map<String, Object> map) {
		String bookNo = (String) map.get("BOOK_NO");
		String state = bookService.bookRentState(bookNo);
		String title = (String) map.get("BOOK_NAME");
		String author = (String) map.get("BOOK_AUTHOR");
		String pub = (String) map.get("BOOK_PUB");
		System.out.print(map.get("LIB_NAME")+"\t");
		if(((String) map.get("LIB_NAME")).length()<=5) {System.out.print("\t");}
		System.out.print(state + "\t" + map.get("BOOK_NO") + "\t" + map.get("CATE_NAME") + "\t");
		if (title.length() >= 24) {
			System.out.print(title.substring(0, 23) + "..");
		} else {
			System.out.print(title);
			for (int j = title.length() / 7; j < 24 / 7; j++) {
				System.out.print("\t");
				if(title.length()>=13&&j<2) {
					break;
				}
			}
			if(title.contains(":")&&title.length()<15) {
				System.out.print("\t");
			}
		}
		System.out.print("\t");
		if (author.length() >= 15) {
			System.out.print(author.substring(0, 14) + "..\t");
		} else {
			System.out.print(author);
			for (int j = author.length() / 7; j < 15 / 7; j++) {
				System.out.print("\t");
			}


		}

		if (pub.length() >= 10) {
			System.out.print("\t" + pub.substring(0, 9) + "..\t");
		} else {
			System.out.print("\t" + pub);
			for (int j = pub.length() / 5; j < 10 / 5; j++) {
				System.out.print("\t");
			}
			if(pub.length()>=7&&pub.matches("^[a-zA-Z]*$")) {
				System.out.print("\t");
			}
			

		}
		if(pub.length()==5) {
			System.out.print("\t");
		}
		System.out.println(map.get("BOOK_PUB_YEAR"));
	}

	public Map<Integer, Integer> printLibraryList(List<Map<String,Object>> list) {
		// 
		Map<Integer, Integer> num = new HashMap<Integer, Integer>();
		int count = 0;
		for (Map<String, Object> map : list) {
			System.out.print(map.get("LIB_NO"));
			if(((BigDecimal) map.get("LIB_NO")).intValue()<10) System.out.print(" ");
			System.out.print(" : "+map.get("LIB_NAME") + "\t");
			if(((String) map.get("LIB_NAME")).length()<=7) {System.out.print("\t");}
			count++;
			if(count%4==0&&count!=20) System.out.println();
			num.put(((BigDecimal)map.get("LIB_NO")).intValue(), 1);
		}
		System.out.println();
		return num;
	}
	
	public Map<Integer, Integer> printLocalList(List<Map<String,Object>> list){
		Map<Integer, Integer> num = new HashMap<Integer, Integer>();
		for (Map<String, Object> map : list) {
			System.out.print(map.get("LOC_NO") + " : " + map.get("LOC_NAME") + "  ");
			num.put(((BigDecimal)map.get("LOC_NO")).intValue() , 1);
		}
		System.out.println();
		return num;
	}
}
