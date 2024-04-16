package print;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.BookService;

public class Print extends Notice {
	BookService bookService = BookService.getInstance();


	public void title() {
		System.out.println(YELLOW);
		System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀	");
		System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠁⠀⠀⠐⠄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀	");	
		System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠌⡑⠀⠀⠀⠀⠀⠀⠐⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
		System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡔⢁⠊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠠⠀⠐⠂⢉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
		System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠌⡠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⢀⠀⠀⠀⠀⡀⠄⠂⠁⠀⠀⠀⠀⠀⡌⢀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
		System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡰⠁⠀⠢⠄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡄⢀⠀⠊⠀⠀⠀⠀⠀⠀⠀⠀⡰⠀⠀⡆⡱⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
		System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠜⠀⠀⠀⠀⠀⠈⠢⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡰⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⠁⠀⢸⢰⠡⡌⣱⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
		System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢈⠑⠒⠤⢄⡀⠀⠀⠡⠘⢢⠀⠀⠀⠀⠀⠀⠀⠀⣸⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠃⠀⢀⡏⠆⡳⢠⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
		System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⠂⠀⠀⠀⠀⠈⢉⠒⢥⡈⠄⠣⡀⠀⠀⠀⠀⠀⣼⠃⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠰⠀⠀⡼⢀⢃⡗⢼⢧⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
		System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡰⢁⣀⣀⣄⣂⣀⡁⢂⠠⢀⠘⠢⣅⠱⡀⠀⠀⠀⡼⠁⠀⠀⣀⠤⠒⠂⠘⠈⠁⠀⠀⠀⠀⢰⠁⠌⡘⢤⣻⢯⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀");
		System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡴⠋⠒⡐⣂⣒⣘⣢⡝⣭⣒⡆⢦⣀⠈⠲⣡⠀⠀⡴⠃⡤⠊⢉⣀⣐⠤⠤⠆⠴⠠⠆⠴⠤⢄⡘⢀⠂⢡⡞⣧⢿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
		System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠞⠡⡉⠍⠱⠀⠦⠰⢄⠶⣠⢆⡬⡑⢊⠗⢦⡑⡇⢰⡥⢫⠔⢋⡉⡄⢂⠢⡐⣌⣐⡒⣌⢢⠐⠤⠘⠢⢌⡰⣼⠿⡍⠀⠀⠀⠀⠀⠀⠀⠀");
		System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣶⣯⣤⣧⣴⠾⡶⠿⠾⢿⢻⡟⣷⡾⣶⣟⣦⣮⣄⣥⣧⣏⡬⣑⣪⡵⣶⣚⣯⢿⣭⣭⣭⢿⣭⣟⡿⣽⣭⢷⣦⣥⢿⡻⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀");
		System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢩⣛⠚⠛⠛⠓⠉⠙⠙⠛⠛⠿⠾⣽⢯⣟⡾⣝⣻⢳⢯⣛⡶⣭⢷⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
		System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠙⠋⠛⠽⠫⢷⢻⣞⠇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
		System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢹⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
		System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
		System.out.println("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀"+END);
		String art = "`7MMF'      `7MMF'`7MM\"\"\"Yp, `7MM\"\"\"Mq.        db      `7MM\"\"\"Mq.`YMM'   `MM'\n" +
                "  MM          MM    MM    Yb   MM   `MM.      ;MM:       MM   `MM. VMA   ,V  \n" +
                "  MM          MM    MM    dP   MM   ,M9      ,V^MM.      MM   ,M9   VMA ,V   \n" +
                "  MM          MM    MM\"\"\"bg.   MMmmdM9      ,M  `MM      MMmmdM9     VMMP    \n" +
                "  MM      ,   MM    MM    `Y   MM  YM.      AbmmmqMA     MM  YM.      MM     \n" +
                "  MM     ,M   MM    MM    ,9   MM   `Mb.   A'     VML    MM   `Mb.    MM     \n" +
                ".JMMmmmmMMM .JMML..JMMmmmd9  .JMML. .JMM..AMA.   .AMMA..JMML. .JMM. .JMML.   \n";
   System.out.println(art);
	}

	public void printOverVar() {
		System.out.println(
				"╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
	}

	public void printMiddleVar() {
		System.out.println(
				"╠════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");
	}

	public void printUnderVar() {
		System.out.println(
				"╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
	}
	
	public void printMenuVar() {
		System.out.println("===============================================================================");
	}
	public void printMenuOverVar() {
		System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
	}
	public void printBookIndex() {
		System.out.println("│도서관이름\t\t│도서 상태\t│도서 번호\t│분류\t│제목\t\t\t\t│작가\t\t\t│출판사\t\t│출판년도");
	}

	public void printBookList(Map<String, Object> map) {
		String bookNo = (String) map.get("BOOK_NO");
		String state = bookService.bookRentState(bookNo);
		String title = (String) map.get("BOOK_NAME");
		String author = (String) map.get("BOOK_AUTHOR");
		String pub = (String) map.get("BOOK_PUB");
		System.out.print("│" + map.get("LIB_NAME") + "\t");
		if(((String)map.get("LIB_NAME")).length()<=5) {System.out.print("\t");}
		System.out.print("│");
		if(state.equals("대출가능")) System.out.print(GREEN);
		if(state.equals("대출예약")) System.out.print(YELLOW);
		if(state.equals("대출불가")) System.out.print(RED);
		System.out.print(state+END+ "\t│" + map.get("BOOK_NO") + "\t│" + map.get("CATE_NAME") + "\t");
		if (title.length() >= 24) {
			System.out.print("│"+title.substring(0, 23) + "..");
		} else {
			System.out.print("│"+title);
			for (int j = title.length() / 7; j < 24 / 7; j++) {
				System.out.print("\t");
				if (title.length() >= 13 && j < 2) {
					break;
				}
			}

		}
		System.out.print("\t│");
		if (author.length() >= 15) {
			System.out.print(author.substring(0, 14) + "..  ");
		} else {
			System.out.print(author);
			for (int j = author.length() / 6; j < 15 / 6; j++) {
				System.out.print("\t");
			}

		}

		if (pub.length() >= 10) {
			System.out.print("\t│" + pub.substring(0, 9) + "..\t");
		} else {
			System.out.print("\t│" + pub);
			for (int j = pub.length() / 5; j < 10 / 5; j++) {
				System.out.print("\t");
			}
			if (pub.length() >= 7 && pub.matches("^[a-zA-Z]*$")) {
				System.out.print("\t");
			}

		}
		if (pub.length() == 5) {
			System.out.print("\t");
		}
		System.out.println("│"+map.get("BOOK_PUB_YEAR"));
		System.out.println("──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
	}
	
	public void printCateName(List<Map<String, Object>> cate) {
		printMenuVar();
		for(Map<String, Object> map : cate) {
			System.out.print("\t"+map.get("CATE_NO")+" : "+map.get("CATE_NAME")+" ");
			if(((BigDecimal)map.get("CATE_NO")).intValue()==4) {
				System.out.println();
			}
		}
		System.out.println();
		printMenuVar();
	}
	

	public Map<Integer, Integer> printLibraryList(List<Map<String, Object>> list) {
		Map<Integer, Integer> num = new HashMap<Integer, Integer>();
		int count = 0;
		for (Map<String, Object> map : list) {
			num.put(((BigDecimal) map.get("LIB_NO")).intValue(), ((BigDecimal) map.get("LIB_NO")).intValue());
			System.out.print(map.get("LIB_NO"));
			if (((BigDecimal) map.get("LIB_NO")).intValue() < 10)
				System.out.print(" ");
			System.out.print(" : " + map.get("LIB_NAME") + "\t");
			if (((String) map.get("LIB_NAME")).length() <= 7) {
				System.out.print("\t");
			}
			count++;
			if (count % 4 == 0 && count != 20)
				System.out.println();
		}
		System.out.println();
		return num;
	}

	public Map<Integer, Integer> printLocalList(List<Map<String, Object>> list) {
		Map<Integer, Integer> num = new HashMap<Integer, Integer>();
		for (Map<String, Object> map : list) {
			System.out.print(map.get("LOC_NO") + " : " + map.get("LOC_NAME") + "  ");
			num.put(((BigDecimal) map.get("LOC_NO")).intValue(), ((BigDecimal) map.get("LOC_NO")).intValue());
		}
		System.out.println();
		return num;
	}
}
