package jdbc.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import jdbc.container.Container;
import jdbc.dto.Article;
import jdbc.dto.User;
import jdbc.service.ArticleService;
import jdbc.service.UserService;

public class Articlecontroller extends Controller {
	@SuppressWarnings("unused")
	private List<Article> articles;
	private ArticleService articleService;
	@SuppressWarnings("unused")
	private List<User> users;
	private UserService userService;
	private User loggedInUser;

	public Articlecontroller(Scanner sc, Connection connection, User loggedInUser) {
		this.sc = sc;
		this.articles = Container.articles;
		this.articleService = new ArticleService(connection);
		this.users = Container.loginusers;
		this.userService = new UserService(connection);
		this.loggedInUser = loggedInUser;
	}

	public void doing(String cmd) {
		String title = null;
		String body = null;
		String writer = loggedInUser.getId();
		switch (cmd) {
		case "logout":
			userService.logout(id);
			loggedInUser = null;
			System.out.println("로그아웃 되었습니다.");
			break;
		case "write":
			System.out.print("제목 : ");
			title = sc.nextLine().trim();
			System.out.print("내용 : ");
			body = sc.nextLine().trim();

			articleService.write(title, body, writer);
			System.out.println("게시글이 작성되었습니다.");
			break;
		case "list":
			articleService.list();
			List<Map<String, Object>> articleListMap = articleService.list();
			for (Map<String, Object> articleMap : articleListMap) {
				Container.articles.add(new Article(articleMap));
			}
			if (Container.articles.size() == 0) {
				System.out.println("게시물이 존재하지 않습니다");
				break;
			} else {
				System.out.println("== 게시물 목록 ==");
				System.out.println("번호   |     제목      |       작성일       ");
				for (Article article : Container.articles) {
					System.out.printf("%-6d | %-15s | %20s\n", article.num, article.title, article.regDate);
				}
				Container.articles.clear();
			}
			break;
		case "modify":
			System.out.print("변경할 글의 번호를 입력해주세요 : ");
			num = sc.nextLine().trim();
			exists = articleService.checkid(num, writer);
			if (!exists) {
				System.out.println("해당되는 번호의 글이 존재하지 않거나 글에 대한 권한이 없습니다.");
				break;
			}
			System.out.print("수정 제목 : ");
			title = sc.nextLine().trim();
			System.out.print("수정 내용 : ");
			body = sc.nextLine().trim();
			articleService.modify(title, body, num);
			System.out.println(num + "번 게시글이 수정되었습니다.");
			break;
		case "detail":
			System.out.print("확인할 게시글의 번호를 입력해주세요 : ");
			num = sc.nextLine().trim();
			Map<String, Object> articleMap = articleService.detail(num);
			if (articleMap != null) {
				Article article = new Article(articleMap);
				System.out.println("== 게시물 상세 정보 ==");
				System.out.println("번호   |     제목      |     내용      |       작성일       ");
				System.out.printf("번호 : %-6d \n제목 : %-15s \n내용 : %s \n작성일 : %20s\n", article.num, article.title,
						article.body, article.regDate);
			} else {
				System.out.println("해당 글이 존재하지 않습니다.");
			}
			break;
		case "delete":
			System.out.print("삭제할 게시글의 번호를 입력해주세요 : ");
			num = sc.nextLine().trim();
			exists = articleService.checkid(num, writer);
			if (!exists) {
				System.out.println("해당 글이 존재하지 않거나 해당 글에 대한 권한이 존재하지 않습니다.");
				break;
			} else {
				articleService.delete(num);
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
	}
}