package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import jdbc.dto.Article;
import jdbc.util.DBUtil;
import jdbc.util.SecSql;

public class App {
	private final String URL = "jdbc:mysql://localhost:3306/jdbc_article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";
	private final String USER = "root";
	private final String PASSWORD = "";

	public void run() {
		Scanner sc = new Scanner(System.in);
		Connection connection = null;

		System.out.println("== 프로그램 시작 ==");
		while (true) {
			try {
				connection = DriverManager.getConnection(URL, USER, PASSWORD);
				SecSql sql;

				System.out.println("사용 가능한 명령어 : write, list, modify, exit");
				System.out.print("명령어) ");
				String cmd = sc.nextLine().trim();
				switch (cmd) {
				case "write":
					System.out.print("제목 : ");
					String title = sc.nextLine();
					System.out.print("내용 : ");
					String body = sc.nextLine();

					sql = new SecSql();
					sql = SecSql.from("INSERT INTO article");
					sql.append("SET regDate = NOW()");
					sql.append(", updateDate = NOW()");
					sql.append(", title = ?", title);
					sql.append(", `body` = ?", body);

					int articlenum = DBUtil.insert(connection, sql);
					System.out.println(articlenum + "번 게시글이 작성되었습니다.");

					break;
				case "list":
					List<Article> articles = new ArrayList<>();
					sql = new SecSql();
					sql.append("SELECT * FROM article");
					sql.append("ORDER BY num DESC");
					List<Map<String, Object>> articleListMap = DBUtil.selectRows(connection, sql);
					for (Map<String, Object> articleMap : articleListMap) {
						articles.add(new Article(articleMap));
					}

					if (articles.size() == 0) {
						System.out.println("게시물이 존재하지 않습니다");
						continue;
					}

					System.out.println("== 게시물 목록 ==");
					System.out.println("번호   |     제목      |       작성일       ");

					for (Article article : articles) {
						System.out.printf("%-6d | %-15s | %20s\n", article.num, article.title, article.regDate);
					}
					break;
				case "modify":
					System.out.print("변경할 글의 번호를 입력해주세요 : ");
					String selnum = sc.nextLine();

					boolean exists = DBUtil.selectRowBooleanValue(connection,
							SecSql.from("SELECT 1 FROM article WHERE").append("num = ?", selnum));

					if (!exists) {
						System.out.println("해당되는 번호의 글이 존재하지 않습니다.");
						break;
					}

					System.out.print("수정 제목 : ");
					String motitle = sc.nextLine();
					System.out.print("수정 내용 : ");
					String mobody = sc.nextLine();

					sql = SecSql.from("UPDATE article");
					sql.append(" SET title = ?", motitle);
					sql.append(", `body` = ?", mobody);
					sql.append(", updateDate = NOW()");
					sql.append(" WHERE num = ?", selnum);

					DBUtil.update(connection, sql);

					System.out.println(selnum + "번 게시글이 수정되었습니다.");

					break;
				case "exit":
					System.out.println("프로그램을 종료합니다.");
					sc.close();
					return;
				default:
					System.out.println("알 수 없는 명령어입니다.");
					break;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}