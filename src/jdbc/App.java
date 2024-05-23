package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import jdbc.dto.Article;
import jdbc.dto.User;
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
			List<User> loginuser = new ArrayList<>();
			try {
				connection = DriverManager.getConnection(URL, USER, PASSWORD);
				SecSql sql;

				String name = null;
				String id = null;
				String pw = null;
				String num = null;
				
				System.out.println("메뉴 : \n1.login \n2.signup \n3.exit");
				System.out.print("명령어) ");
				String cmd1 = sc.nextLine().trim().trim();
				switch (cmd1) {
				case "login":
					System.out.print("아이디 : ");
					id = sc.nextLine().trim();
					System.out.print("패스워드 : ");
					pw = sc.nextLine().trim();
					sql = new SecSql();
					boolean existsid = DBUtil.selectRowBooleanValue(connection,
							SecSql.from("SELECT 1 FROM user WHERE").append("id = ? AND pw = ?;", id, pw));
					if (!existsid) {
						System.out.println("해당하는 사용자가 존재하지 않습니다.");
						break;
					}
					sql = new SecSql();
					sql = SecSql.from("SELECT nickname FROM user WHERE");
					sql.append("id = ?;", id);
					name = DBUtil.selectRowStringValue(connection, sql);
					User user = new User(id, name, true);
					loginuser.add(user);

					sql = new SecSql();
					sql = SecSql.from("UPDATE user");
					sql.append(" SET logined = TRUE");
					sql.append(" , updateDate = NOW()");
					sql.append(" WHERE id = ?;", id);
					DBUtil.update(connection, sql);
					System.out.println("로그인 되었습니다.");

					while (user.getLogined()) {
						System.out.println("\n사용 가능한 명령어 : \nwrite, list, modify, detail, delete, logout, exit");
						System.out.print("명령어) ");
						String title = null;
						String body = null;
						
						String cmd2 = sc.nextLine().trim().trim();
						switch (cmd2) {
						case "logout":
							id = user.getId();
							sql = new SecSql();
							sql = SecSql.from("UPDATE user");
							sql.append(" SET logined = FALSE");
							sql.append(" WHERE id = ?;", id);
							DBUtil.update(connection, sql);
							user.setLogined(false);
							System.out.println("로그아웃 되었습니다.");
							break;
						case "write":
							System.out.print("제목 : ");
							title = sc.nextLine().trim();
							System.out.print("내용 : ");
							body = sc.nextLine().trim();

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
							String selnum = sc.nextLine().trim();
							boolean exists = DBUtil.selectRowBooleanValue(connection,
									SecSql.from("SELECT 1 FROM article WHERE").append("num = ?", selnum));

							if (!exists) {
								System.out.println("해당되는 번호의 글이 존재하지 않습니다.");
								break;
							}

							System.out.print("수정 제목 : ");
							title = sc.nextLine().trim();
							System.out.print("수정 내용 : ");
							body = sc.nextLine().trim();

							sql = SecSql.from("UPDATE article");
							sql.append(" SET title = ?", title);
							sql.append(", `body` = ?", body);
							sql.append(", updateDate = NOW()");
							sql.append(" WHERE num = ?", selnum);

							DBUtil.update(connection, sql);
							System.out.println(selnum + "번 게시글이 수정되었습니다.");
							break;
						case "detail":
							System.out.print("확인할 게시글의 번호를 입력해주세요 : ");
							num = sc.nextLine().trim();
							sql = new SecSql();
							boolean exists2 = DBUtil.selectRowBooleanValue(connection,
									SecSql.from("SELECT 1 FROM article WHERE").append("num = ?;", num));

							if (!exists2) {
								System.out.println("해당되는 번호의 글이 존재하지 않습니다.");
								break;
							}
							sql = new SecSql();
							sql.append("SELECT * FROM article WHERE num = ?", num);
							Map<String, Object> articleMap = DBUtil.selectRow(connection, sql);
							Article article = new Article(articleMap);

							System.out.println("== 게시물 상세 정보 ==");
							System.out.println("번호   |     제목      |     내용      |       작성일       ");
							System.out.printf("번호 : %-6d \n제목 : %-15s \n내용 : %s \n작성일 : %20s\n", article.num,
									article.title, article.body, article.regDate);
							break;
						case "delete":
							System.out.print("삭제할 게시글의 번호를 입력해주세요 : ");
							num = sc.nextLine().trim();
							boolean exists1 = DBUtil.selectRowBooleanValue(connection,
									SecSql.from("SELECT 1 FROM article WHERE").append("num = ?", num));
							if (!exists1) {
								System.out.println("해당되는 번호의 글이 존재하지 않습니다.");
								break;
							}
							sql = new SecSql();
							sql.append("DELETE FROM article WHERE num = ?;", num);
							DBUtil.delete(connection, sql);
							break;
						case "exit":
							System.out.println("프로그램을 종료합니다.");
							sc.close();
							return;
						default:
							System.out.println("알 수 없는 명령어입니다.");
							break;
						}
					}
					break;
				case "signup":
					System.out.print("회원 아이디 : ");
					id = sc.nextLine().trim();
					if (id.isEmpty()) {
						System.out.println("아이디를 입력해주세요.");
						break;
					} else {
						sql = new SecSql();
						boolean existid = DBUtil.selectRowBooleanValue(connection,
								SecSql.from("SELECT 1 FROM user WHERE").append("id = ?;", id));
						if (!existid) {
							System.out.println("이미 존재하는 아이디입니다.");
							break;
						}
					}
					System.out.print("패스워드 : ");
					pw = sc.nextLine().trim();
					System.out.print("패스워드 확인 : ");
					String newpw = sc.nextLine().trim();

					if (!pw.equals(newpw)) {
						System.out.println("패스워드가 일치하지 않습니다.");
						break;
					} else {
						System.out.print("사용할 이름을 입력해주세요 : ");
						String nickname = sc.nextLine().trim();
						sql = new SecSql();
						sql = SecSql.from("INSERT INTO user");
						sql.append("SET regDate = NOW()");
						sql.append(", updateDate = NOW()");
						sql.append(", id = ?", id);
						sql.append(", pw = ?", pw);
						sql.append(", nickname = ?", nickname);
						sql.append(", logined = FALSE;");

						int usernum = DBUtil.insert(connection, sql);
						System.out.println(usernum + "번 사용자가 생성되었습니다.");
					}
					break;
				case "exit":
					System.out.println("프로그램을 종료합니다.");
					sc.close();
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
