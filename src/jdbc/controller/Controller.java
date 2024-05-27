package jdbc.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import jdbc.dto.User;
import jdbc.util.SecSql;

public abstract class Controller {
	protected Scanner sc;
	protected String cmd;
	protected static User loggedInUser;
	protected Connection connection = null;
	protected SecSql sql;

	protected String name = null;
	protected String id = null;
	protected String pw = null;
	protected String num = null;
	protected boolean exists = false;

	public abstract void doing(String cmd);

	public static boolean isLogined() {
		return loggedInUser != null;
	}

	protected String getUserByLoginId(String loginId, List<User> loggedInUsers) {
		for (User user : loggedInUsers) {
			if (user.getId().equals(loginId)) {
				return user.getId();
			}
		}
		return null;
	}
}
