package jdbc.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import jdbc.container.Container;
import jdbc.dto.Article;
import jdbc.util.DBUtil;
import jdbc.util.SecSql;

public class ArticleDao {
	@SuppressWarnings("unused")
	private List<Article> articles;
	private Connection connection;
	private SecSql sql;

	public ArticleDao(Connection connection) {
		this.connection = connection;
		this.articles = Container.articles;
	}

	public void write(String title, String body, String writer) {
		SecSql sql = new SecSql();
		sql.append("INSERT INTO article");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", title = ?", title);
		sql.append(", `body` = ?", body);
		sql.append(", writer = ?", writer);

		DBUtil.insert(connection, sql);
	}

	public List<Map<String, Object>> list() {
		sql = new SecSql();
		sql.append("SELECT * FROM article");
		sql.append("ORDER BY num DESC");
		return DBUtil.selectRows(connection, sql);
	}

	public boolean checkid(String num, String writer) {
		boolean success = false;
		boolean existsid = DBUtil.selectRowBooleanValue(connection,
				SecSql.from("SELECT 1 FROM article WHERE").append("num = ? and writer = ?", num, writer));
		if (existsid) {
			success = true;
		}
		return success;
	}

	public boolean checknum(String num) {
		boolean success = false;
		boolean existsid = DBUtil.selectRowBooleanValue(connection,
				SecSql.from("SELECT 1 FROM article WHERE").append("num = ?", num));
		if (existsid) {
			success = true;
		}
		return success;
	}

	public void modify(String title, String body, String num) {
		sql = SecSql.from("UPDATE article");
		sql.append(" SET title = ?", title);
		sql.append(", `body` = ?", body);
		sql.append(", updateDate = NOW()");
		sql.append(" WHERE num = ?", num);

		DBUtil.update(connection, sql);
	}

	public Map<String, Object> detail(String num) {
		sql = new SecSql();
		sql.append("SELECT * FROM article WHERE num = ?", num);
		return DBUtil.selectRow(connection, sql);
	}

	public void delete(String num) {
		sql = new SecSql();
		sql.append("DELETE FROM article WHERE num = ?;", num);
		DBUtil.delete(connection, sql);
	}
}
