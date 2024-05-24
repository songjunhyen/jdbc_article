package jdbc.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import jdbc.dao.ArticleDao;

public class ArticleService {
	private ArticleDao articleDao;

	public ArticleService(Connection connection) {
		this.articleDao = new ArticleDao(connection);
	}

	public void write(String title, String body, String writer) {
		articleDao.write(title, body, writer);
	}

	public List<Map<String, Object>> list() {
		return articleDao.list();
	}

	public boolean checkid(String num, String writer) {
		return articleDao.checkid(num, writer);
	}

	public void modify(String title, String body, String num) {
		articleDao.modify(title, body, num);
	}

	public Map<String, Object> detail(String num) {
		if (!articleDao.checknum(num)) {
			System.out.println("해당 글이 존재하지 않습니다.");
			return null;
		}
		return articleDao.detail(num);
	}

	public void delete(String num) {
		articleDao.delete(num);
	}

}
