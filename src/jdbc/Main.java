package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import jdbc.dto.Article;

public class Main {

	private static final String URL = "jdbc:mysql://localhost:3306/jdbc_article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";
	private static final String USER = "root";
	private static final String PASSWORD = "";

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
				articles.add(article);
				System.out.println(article.getNum() + "번 게시글이 작성되었습니다.");
				lastnum++;

				Connection connection = null;
				PreparedStatement pstmt = null;

				try {
					connection = DriverManager.getConnection(URL, USER, PASSWORD);

					String sql = "INSERT INTO article";
					sql += " SET regDate = NOW()";
					sql += ", updateDate = NOW()";
					sql += ", title = '" + title + "'";
					sql += ", `body` = '" + body + "';";

					pstmt = connection.prepareStatement(sql);
					pstmt.executeUpdate();

				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (pstmt != null) {
						try {
							pstmt.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					if (connection != null) {
						try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
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