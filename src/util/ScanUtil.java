package util;

import java.util.Scanner;

import print.Print;

public class ScanUtil extends Print   {
	// 스캐너를 손쉽게 사용할 수 있는 static 메서드를 가지고있음
	static Scanner sc = new Scanner(System.in);
	
	public static int menu() {
		return nextInt(tap+"입력    >  ");
	}
	
	public static String menuStr() {
		return nextLine(tap+"입력    >  ");
	}
	
	public static String nextLine(String print) {
		System.out.print(print);
		return nextLine();
	}
	
	public static String bookNo() {
		System.out.print(tap+"도서 번호    > ");
		return BookNo();
	}
	
	public static String nextLine() {
		return sc.nextLine();
	}
	
	
	
	// 안내문구 강제
	// private로 사용할 수 없기 때문에 위의 안내문구를 강제하는 메소드를 사용해야함
	public static int nextInt(String print) {
		System.out.print(print);
		return nextInt();
	}
	
	private static int nextInt() {
		// return 이 될 때까지 반복
		while(true) {
			try {
				int result = Integer.parseInt(sc.nextLine());
				return result;
			}catch (NumberFormatException e) {
				System.out.println("\t"+RED+var+END);
				System.out.println(notice+"숫자만 입력해주세요");
				System.out.println("\t"+RED+var+END);
			}
		}
	}
	private static String BookNo() {
        // return 이 될 때까지 반복
        while(true) {
            try {
                String result = sc.nextLine();
                if (result.length() == 5 && result.matches("\\d+")) {
                    return result;
                } else {
                	System.out.println("\t"+RED+var+END);
                    System.out.println(notice+"다섯 자리 숫자를 입력하세요.");
                    System.out.println("\t"+RED+var+END);
                }
            } catch (NumberFormatException e) {
            	System.out.println("\t"+RED+var+END);
                System.out.println(notice+"잘못 입력!!");
                System.out.println("\t"+RED+var+END);
            }
        }
    }
	
}
