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
import jdbc.util.Util;

public class Articlecontroller extends Controller {
	private ArticleService articleService;
	private UserService userService;

	public Articlecontroller(Scanner sc, Connection connection, User loggedInUser) {
		this.sc = sc;
		this.connection = connection;
		this.articleService = new ArticleService(connection);
		this.userService = new UserService(connection);
		Controller.loggedInUser = loggedInUser;
	}

	@Override
	public void doing(String cmd) {
		String writer = loggedInUser.getId();
		switch (cmd) {
		case "logout":
			userService.logout(writer);
			loggedInUser = null;
			System.out.println("로그아웃 되었습니다.");
			break;
		case "withdraw":
			System.out.printf("회원정보를 삭제하기 위해 확인을 진행하겠습니다.");
			System.out.print("패스워드 : ");
			pw = sc.nextLine().trim();
			userService.deleteuser(writer, pw);
			System.out.printf("회원정보가 삭제되었습니다.");
			loggedInUser = null;
			break;
		case "write":
			write(writer);
			break;
		case "edit":
			edit(writer);
			break;
		case "list":
			list();
			break;
		case "modify":
			modify(writer);
			break;
		case "detail":
			detail();
			break;
		case "delete":
			delete(writer);
			break;
		case "exit":
			System.out.println("프로그램을 종료합니다.");
			userService.logout(writer);
			loggedInUser = null;
			sc.close();
			return;
		default:
			System.out.println("알 수 없는 명령어입니다.");
			break;
		}

	}

	private void write(String writer) {
		System.out.print("제목 : ");
		String title = sc.nextLine().trim();
		System.out.print("내용 : ");
		String body = sc.nextLine().trim();
		articleService.write(title, body, writer);
		System.out.println("게시글이 작성되었습니다.");
	}

	private void edit(String writer) {
		System.out.printf("회원정보를 수정하기 위해 확인을 진행하겠습니다.");
		System.out.print("패스워드 : ");
		pw = sc.nextLine().trim();
		System.out.print("변경 이름 : ");
		String newname = sc.nextLine().trim();
		System.out.print("변경 패스워드 : ");
		String newpw = sc.nextLine().trim();
		userService.edituser(writer, newname, pw, newpw);
		System.out.printf("회원정보를 수정하었습니다.");
	}

	private void list() {
		articleService.list();
		List<Map<String, Object>> articleListMap = articleService.list();
		for (Map<String, Object> articleMap : articleListMap) {
			Container.articles.add(new Article(articleMap));
		}
		if (Container.articles.size() == 0) {
			System.out.println("게시물이 존재하지 않습니다");
		} else {
			System.out.println("== 게시물 목록 ==");
			System.out.println("번호   |     제목      |       작성일       ");
			for (Article article : Container.articles) {
				System.out.printf("%-6d | %-15s | %20s\n", article.num, article.title, Util.datetimeFormat(article.regDate));
			}
			Container.articles.clear();
		}
	}

	private void modify(String writer) {
		while (true) {
			System.out.print("변경할 글의 번호를 입력해주세요 : ");
			num = sc.nextLine().trim();
			exists = articleService.checkidnum(num, writer);
			if (!exists) {
				System.out.println("해당되는 번호의 글이 존재하지 않거나 글에 대한 권한이 없습니다.");
				break;
			}
			System.out.print("수정 제목 : ");
			String title = sc.nextLine().trim();
			System.out.print("수정 내용 : ");
			String body = sc.nextLine().trim();
			articleService.modify(title, body, num);
			System.out.println(num + "번 게시글이 수정되었습니다.");
			break;
		}
	}

	private void detail() {
		System.out.print("확인할 게시글의 번호를 입력해주세요 : ");
		num = sc.nextLine().trim();
		Map<String, Object> articleMap = articleService.detail(num);
		if (articleMap != null) {
			Article article = new Article(articleMap);
			System.out.println("== 게시물 상세 정보 ==");
			System.out.printf("번호 : %-6d \n제목 : %-15s \n작성자 : %-15s \n내용 : %s \n작성일 : %20s\n", article.num,
					article.title, article.writer, article.body, Util.datetimeFormat(article.regDate));
		} else {
			System.out.println("해당 글이 존재하지 않습니다.");
		}
	}

	private void delete(String writer) {
		while (true) {
			System.out.print("삭제할 게시글의 번호를 입력해주세요 : ");
			num = sc.nextLine().trim();
			exists = articleService.checkidnum(num, writer);
			if (!exists) {
				System.out.println("해당 글이 존재하지 않거나 해당 글에 대한 권한이 존재하지 않습니다.");
				break;
			} else {
				articleService.delete(num,writer);
				System.out.print("삭제되었습니다.");
			}
			break;
		}
	}
}
