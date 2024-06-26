package jdbc.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class Article {
	public int num;
	public LocalDateTime regDate;
	public LocalDateTime updateDate;
	public String title;
	public String body;
	public String writer;
	public int viewcount;
	
	public Article(Map<String, Object> articleMap) {
		this.num = (int) articleMap.get("num");
		this.regDate = (LocalDateTime) articleMap.get("regDate");
		this.updateDate = (LocalDateTime) articleMap.get("updateDate");
		this.title = (String) articleMap.get("title");
		this.body = (String) articleMap.get("body");
		this.writer = (String) articleMap.get("writer");
		this.viewcount = (int) articleMap.get("viewcount");
	}
}