package jdbc.dao;

import java.sql.Connection;
import java.util.List;

import jdbc.container.Container;
import jdbc.dto.User;
import jdbc.util.DBUtil;
import jdbc.util.SecSql;

public class UserDao {
	@SuppressWarnings("unused")
	private List<User> users;
	private Connection connection;
	private SecSql sql;

	public UserDao(Connection connection) {
		this.connection = connection;
		this.users = Container.loginusers;
	}

	public void signup(String id, String pw, String nickname) {
		sql = new SecSql();
		sql = SecSql.from("INSERT INTO user");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", id = ?", id);
		sql.append(", pw = ?", pw);
		sql.append(", nickname = ?", nickname);
		sql.append(", logined = FALSE;");
		DBUtil.insert(connection, sql);
	}

	public boolean login(String id, String pw) {
		boolean success = false;
		sql = new SecSql();
		boolean existsid = DBUtil.selectRowBooleanValue(connection,
				SecSql.from("SELECT 1 FROM user WHERE").append("id = ? AND pw = ?;", id, pw));

		if (existsid) {
			sql = new SecSql();
			sql = SecSql.from("UPDATE user");
			sql.append(" SET logined = TRUE");
			sql.append(" , updateDate = NOW()");
			sql.append(" WHERE id = ?;", id);
			DBUtil.update(connection, sql);
			success = true;
		}
		return success;
	}

	public boolean logined(String id) {
		sql = new SecSql();
		boolean loginedValue = DBUtil.selectRowBooleanValue(connection,
				SecSql.from("SELECT logined FROM user WHERE").append("id = ?;", id));

		return loginedValue;
	}

	public String getname(String id) {
		sql = new SecSql();
		sql = SecSql.from("SELECT nickname FROM user WHERE");
		sql.append("id = ?;", id);
		String name = DBUtil.selectRowStringValue(connection, sql);
		return name;
	}

	public void logout(String id) {
		sql = new SecSql();
		sql = SecSql.from("UPDATE user");
		sql.append(" SET logined = FALSE");
		sql.append(" WHERE id = ?;", id);
		DBUtil.update(connection, sql);
	}

	public void deleteuser(String id, String pw) {
		if (checkid(id, pw)) {
			SecSql sql = new SecSql();
			sql.append("DELETE FROM `user` WHERE id = ? AND pw = ?");
			sql.append(" AND EXISTS(SELECT 1 FROM `user` WHERE id = ?)");
			sql.append(" ?", id, pw);
			logout(id);
			DBUtil.delete(connection, sql);
		}
	}

	public void edituser(String id, String newname, String pw, String newpw) {
		if (checkid(id, pw)) {
			SecSql sql = new SecSql();
			sql.append("UPDATE `user`");
			sql.append("SET nickname = ?", newname);
			sql.append(", pw = ?", newpw);
			sql.append(" WHERE id = ?");
			sql.append(" AND EXISTS(SELECT 1 FROM `user` WHERE id = ?)");
			sql.append(" ?", id);
			DBUtil.update(connection, sql);
		}
	}

	public boolean checkid(String id, String... pw) {
		if (pw.length == 0) {
			sql = new SecSql();
			boolean existsid = DBUtil.selectRowBooleanValue(connection,
					SecSql.from("SELECT 1 FROM user").append("WHERE id = ?", id));
			return existsid;
		} else {
			sql = new SecSql();
			boolean existsid = DBUtil.selectRowBooleanValue(connection,
					SecSql.from("SELECT 1 FROM user WHERE").append("id = ? AND pw = ?;", id, pw[0]));
			return existsid;
		}
	}
}
