package practice.bookyard.contracts;

public class Constants {
	
	public static String getLoginUrl() {
		return "http://localhost:8080/Bookyard.server/login";
	}
	
	public static String getSecuredLoginUrl() {
		return "https://localhost:8443/Bookyard.server/login";
	}
	
	public static String getRecommendationsUrl() {
		return "http://localhost:8080/Bookyard.server/recommend";
	}
	
	public static String getSecuredRecommendationsUrl() {
		return "https://localhost:8443/Bookyard.server/recommend";
	}
	
	public static String JWT_SUBJECT_LOGIN_REQUEST = "LoginRequest";
	public static String JWT_SUBJECT_ACCESS_TOKEN = "AccessToken";
}
