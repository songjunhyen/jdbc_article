package jdbc.dto;

public class Article {
	private int num;
	private String title;
	private String body;

	public Article(int num, String title, String body) {
		this.num = num;
		this.title = title;
		this.body = body;
	}

	public int getNum() {
		return num;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setBody(String body) {
		this.body = body;
	}
}