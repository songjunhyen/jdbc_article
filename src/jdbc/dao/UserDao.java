package jdbc.dao;

import java.sql.Connection;
import java.util.List;

import jdbc.container.Container;
import jdbc.dto.User;
import jdbc.util.DBUtil;
import jdbc.util.SecSql;

public class UserDao {
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

	public String getname(String id) {
		sql = new SecSql();
		sql = SecSql.from("SELECT nickname FROM user WHERE");
		sql.append("id = ?;", id);
		String name = DBUtil.selectRowStringValue(connection, sql);
		return name;
	}

	public boolean checkid(String id) {
		boolean success = false;
		sql = new SecSql();
		boolean existsid = DBUtil.selectRowBooleanValue(connection,
				SecSql.from("SELECT 1 FROM user").append("WHERE id = ?", id));
		if (existsid) {
			success = true;
		}
		return success;
	}

	public void logout(String id) {
		sql = new SecSql();
		sql = SecSql.from("UPDATE user");
		sql.append(" SET logined = FALSE");
		sql.append(" WHERE id = ?;", id);
		DBUtil.update(connection, sql);
	}
}
