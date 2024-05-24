package jdbc.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import jdbc.dto.User;
import jdbc.util.SecSql;

public abstract class Controller {
	public Scanner sc;
	public String cmd;
	public static User loggedInUser;
	Connection connection = null;

	SecSql sql;
	
	String name = null;
	String id = null;
	String pw = null;
	String num = null;
	boolean exists = false;

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
