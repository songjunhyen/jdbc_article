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

	public boolean checkidnum(String num, String writer) {
		return articleDao.checkArticle(num, writer);
	}

	public void modify(String title, String body, String num) {
		articleDao.modify(title, body, num);
	}

	public Map<String, Object> detail(String num) {
		if (!articleDao.checkArticle(num)) {
			return null;
		}
		return articleDao.detail(num);
	}

	public void delete(String num, String writer) {
		articleDao.delete(num, writer);
	}

	public List<Map<String, Object>> search(String keyword) {
		return articleDao.search(keyword);
	}

}
