package jdbc.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {
	public static String datetimeFormat(LocalDateTime datetime) {
		// 원하는 날짜 및 시간 형식 지정
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		// 형식 지정된 문자열로 변환하여 반환
		return datetime.format(formatter);
	}
}
