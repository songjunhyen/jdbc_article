package jdbc.dto;

public class User {
	private String id;
	private boolean logined;

	public User(String id, boolean logined) {
		this.id = id;
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

}