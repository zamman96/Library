package util;

import java.util.Scanner;

public class ScanUtil   {
	// 스캐너를 손쉽게 사용할 수 있는 static 메서드를 가지고있음
	static Scanner sc = new Scanner(System.in);
	
	public static int menu() {
		return nextInt("메뉴 : ");
	}
	
	public static String nextLine(String print) {
		System.out.print(print);
		return nextLine();
	}
	
	private static String nextLine() {
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
				System.out.println("잘못 입력!!");
			}
		}
	}
	
}
