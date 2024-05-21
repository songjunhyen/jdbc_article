package jdbd;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		int lastnum = 1;
		ArrayList<Article> articles = new ArrayList<>();
		Scanner sc = new Scanner(System.in);
		System.out.println("== 프로그램 시작 ==");
		while (true) {
			System.out.println("사용가능 명령어 : write, list");
			System.out.print("명령어) ");
			String cmd = sc.nextLine();
			switch (cmd) {
			case "write":
				System.out.print("제목 : ");
				String title = sc.nextLine();
				System.out.print("내용 : ");
				String body = sc.nextLine();
				System.out.println(lastnum + "번 게시글이 작성되었습니다.");
				articles.add(new Article(lastnum, title, body));
				lastnum++;
				break;
			case "list":
				System.out.println("  번호 | 제목");
				for (Article article : articles) {
					System.out.printf( "%5d | %-10s\n",article.getNum(), article.getTitle());
				}
			default:
				break;
			}
			System.out.println("");
		}
	}
}

class Article {
	private int num;
	private String title;
	private String body;

	public Article(int num, String title, String body) {
		this.num = num;
		this.title = title;
		this.body = body;
	}

	public int getNum() {
		return num;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setBody(String body) {
		this.body = body;
	}
}