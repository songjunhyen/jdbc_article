package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import jdbc.controller.Usermanager;

public class App {
	private final String URL = "jdbc:mysql://localhost:3306/jdbc_article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";
	private final String USER = "root";
	private final String PASSWORD = "";
	
	Scanner sc = new Scanner(System.in);
	
	Connection connection = null;
	public void run() {
	    System.out.println("== 프로그램 시작 ==");
	    while (true) {
	        try {
	            connection = DriverManager.getConnection(URL, USER, PASSWORD);
	            Usermanager usermanager = new Usermanager(sc,connection);
	            System.out.println("메뉴 : \n1.login \n2.signup \n3.exit");
	            System.out.print("명령어) ");
	            String cmd = sc.nextLine().trim().trim();

	            usermanager.doing(cmd);

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
}
