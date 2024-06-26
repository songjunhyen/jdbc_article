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

	public void modify(String title, String body, String num) {
		if (checkArticle(num)) {
			sql = new SecSql();
			sql = SecSql.from("UPDATE article");
			sql.append(" SET title = ?", title);
			sql.append(", `body` = ?", body);
			sql.append(", updateDate = NOW()");
			sql.append(" WHERE num = ?", num);

			DBUtil.update(connection, sql);
		}
	}

	public List<Map<String, Object>> search(String keyword) {
		sql = new SecSql();
		sql.append("SELECT * FROM article");
		// 키워드를 공백으로 분할
		String[] keywords = keyword.split("\\s+");
		if (keywords.length > 0) {
			sql.append("WHERE");
			// 각 키워드에 대해 LIKE 조건 추가
			for (int i = 0; i < keywords.length; i++) {
				if (i > 0) {
					sql.append("AND");
				}
				String searchKeyword = "%" + keywords[i] + "%";
				sql.append("(title LIKE ? OR `body` LIKE ?)", searchKeyword, searchKeyword);
			}
		}
		sql.append("ORDER BY num DESC");
		return DBUtil.selectRows(connection, sql);
	}

	public Map<String, Object> detail(String num) {
		increaseViewCount(num);
		SecSql sql = new SecSql();
		sql.append("SELECT * FROM article WHERE num = ?", num);
		sql.append(" AND EXISTS(SELECT 1 FROM article WHERE num = ?)", num);
		return DBUtil.selectRow(connection, sql);
	}

	public void delete(String num, String writer) {
		if (checkArticle(num, writer)) {
			SecSql sql = new SecSql();
			sql.append("DELETE FROM article WHERE num = ?", num);
			sql.append(" AND writer = ?", writer);
			sql.append(" AND EXISTS(SELECT 1 FROM article WHERE num = ?)", num);
			DBUtil.delete(connection, sql);
		}
	}

	public boolean checkArticle(String num, String... writer) {
		if (writer.length == 0) {
			sql = SecSql.from("SELECT 1 FROM article WHERE").append("num = ?", num);
		} else {
			sql = SecSql.from("SELECT 1 FROM article WHERE").append("num = ? AND writer = ?", num, writer[0]);
		}
		return DBUtil.selectRowIntValue(connection, sql) == 1;
	}

	public void increaseViewCount(String num) {
		SecSql sql = new SecSql();
		sql.append("UPDATE article SET viewcount = viewcount + 1 WHERE num = ?", num);
		DBUtil.update(connection, sql);
	}

}
