package jdbc.dto;

public class User {
	private String id;
	private boolean logined;
	private String name;

	public User(String id, String name, boolean logined) {
		this.id = id;
		this.name = name;
		this.logined = logined;
	}

	public String getId() {
		return id;
	}

	public boolean getLogined() {
		return logined;
	}

	public void setLogined(boolean logined) {
		this.logined = logined;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}