package jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import jdbc.dto.Article;

public class Main {
	public static void main(String[] args) {
		int lastnum = 1;
		List<Article> articles = new ArrayList<>();
		Scanner sc = new Scanner(System.in);
		System.out.println("== 프로그램 시작 ==");
		while (true) {
			System.out.println("사용 가능한 명령어 : write, list, exit");
			System.out.print("명령어) ");
			String cmd = sc.nextLine();
			switch (cmd) {
			case "write":
				System.out.print("제목 : ");
				String title = sc.nextLine();
				System.out.print("내용 : ");
				String body = sc.nextLine();
				Article article = new Article(lastnum, title, body);
				System.out.println(article.getNum() + "번 게시글이 작성되었습니다.");
				lastnum++;
				break;
			case "list":
				if (articles.isEmpty()) {
					System.out.println("게시글이 없습니다.");
				} else {
					System.out.println("  번호 | 제목");
					for (Article ar : articles) {
						System.out.printf("%5d | %-10s\n", ar.getNum(), ar.getTitle());
					}
				}
				break;
			case "exit":
				System.out.println("프로그램을 종료합니다.");
				sc.close();
				return;
			default:
				System.out.println("알 수 없는 명령어입니다.");
				break;
			}
			System.out.println("");
		}
	}
}

