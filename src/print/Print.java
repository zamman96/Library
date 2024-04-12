package print;

import java.util.Map;

public class Print {
	public static final String END = "\u001B[0m";
	public static final String YELLOW = "\u001B[33m";
	public static final String GREEN = "\u001B[32m";
	
	public void printVar() {
		System.out.println("─────────────────────────────────────────────────────────────────────────────────────────────────────────────");
	}
	public void printBookIndex() {
		System.out.println("도서 번호\t분류\t제목\t\t\t\t\t작가\t\t발행처");
	}
	
	public void printBookList(Map<String, Object> map) {
				String title= (String) map.get("BOOK_NAME");
				String author= (String) map.get("BOOK_AUTHOR");
				System.out.print(map.get("BOOK_NO")+"\t"+
						map.get("CATE_NAME")+"\t");
				if(title.length()>=24) {
					System.out.print(title.substring(0,23)+"..");
				} else {
					System.out.print(title);	
					for(int j=title.length()/5;j<24/5;j++) {
						System.out.print("\t");
					}
				}
				if(title.length()>9) {System.out.print("\t");}
				if(author.length()>=10) {
					System.out.print("\t"+author.substring(0,9)+"..\t");
				} else {
					System.out.print("\t"+author);	
					for(int j=author.length()/5;j<10/5;j++) {
						System.out.print("\t");
					}
				}
				if(author.length()>7&&author.length()<8||author.length()>3&&author.length()<7) {System.out.print("\t");}
				System.out.println(map.get("BOOK_PUB"));
	}
}
