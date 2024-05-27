package jdbc.service;

import java.sql.Connection;

import jdbc.dao.UserDao;

public class UserService {
	private UserDao userDao;

	public UserService(Connection connection) {
		this.userDao = new UserDao(connection);
	}

	public void signup(String id, String pw, String nickname) {
		userDao.signup(id, pw, nickname);
	}

	public boolean login(String id, String pw) {
		return userDao.login(id, pw);
	}
	
	public boolean logined(String id) {
		return userDao.logined(id);
	}

	public boolean checkid(String id) {
		return userDao.checkid(id);
	}

	public void logout(String id) {
		userDao.logout(id);
	}

	public void deleteuser(String writer, String pw) {
		userDao.deleteuser(writer, pw);
	}

	public void edituser(String writer, String newname, String pw, String newpw) {
		userDao.edituser(writer, newname, pw, newpw);
	}
}
