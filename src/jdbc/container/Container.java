package jdbc.container;

import java.util.ArrayList;
import java.util.List;

import jdbc.dto.Article;
import jdbc.dto.User;

public class Container {
	public static List<User> loginusers;
	public static List<Article> articles;

	static {
		loginusers = new ArrayList<>();
		articles = new ArrayList<>();
	}
}
