package jdbc.controller;

import java.sql.Connection;
import java.util.Scanner;

import jdbc.container.Container;
import jdbc.dto.User;
import jdbc.service.UserService;

public class Usermanager extends Controller {
	private UserService userService;

	public Usermanager(Scanner sc, Connection connection) {
		this.sc = sc;
		this.connection = connection;
		this.userService = new UserService(connection);
	}

	@Override
	public void doing(String cmd) {
		switch (cmd) {
		case "login":
			login();
			break;
		case "signup":
			signup();
			break;
		case "exit":
			System.out.println("프로그램을 종료합니다.");
			sc.close();
			System.exit(0);
			return;
		default:
			System.out.println("알 수 없는 명령어입니다.");
			break;
		}
	}

	private void login() {
		System.out.print("아이디 : ");
		id = sc.nextLine().trim();
		System.out.print("패스워드 : ");
		pw = sc.nextLine().trim();
		boolean loginSuccess = userService.login(id, pw);
		if (loginSuccess) {
			User user = new User(id, true);
			loggedInUser = user;
			Container.loginusers.add(user);
			System.out.println("로그인 되었습니다.");
			while (user.getLogined() && loggedInUser == user) {
				System.out.println(
						"\n사용 가능한 명령어 : \nwrite, list, modify, detail, search, delete,\n logout, withdraw, edit");
				System.out.print("명령어) ");
				Articlecontroller articlecontroller = new Articlecontroller(sc, connection, user);
				cmd = sc.nextLine().trim();
				articlecontroller.doing(cmd);
			}
		} else {
			System.out.println("아이디 또는 패스워드가 잘못되었습니다.");
		}
	}

	private void signup() {
		System.out.print("회원 아이디 : ");
		id = sc.nextLine().trim();
		if (id.isEmpty()) {
			System.out.println("아이디를 입력해주세요.");
		} else {
			exists = userService.checkid(id);
			if (exists) {
				System.out.println("이미 존재하는 아이디입니다.");
			}
		}
		System.out.print("패스워드 : ");
		pw = sc.nextLine().trim();
		System.out.print("패스워드 확인 : ");
		String newpw = sc.nextLine().trim();

		if (!pw.equals(newpw)) {
			System.out.println("패스워드가 일치하지 않습니다.");
		} else if (pw.isEmpty()) {
			System.out.println("패스워드를 입력해주세요.");
		} else {
			System.out.print("사용할 이름을 입력해주세요 : ");
			String nickname = sc.nextLine().trim();
			userService.signup(id, pw, nickname);
			System.out.println("사용자가 생성되었습니다.");
		}
	}
}