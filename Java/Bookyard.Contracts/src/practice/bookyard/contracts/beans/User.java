package practice.bookyard.contracts.beans;

public class User {
	
	// Id
	// userName
	// fullName
	// email
	// appId
	// applicationTableId
	
	private int id;
	private String userName;
	private String fullName;
	private String email;
	private String appId;
	private int applicationTableId;
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getFullName() {
		return this.fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getAppId() {
		return this.appId;
	}
	
	public void setAppId(String appId) {
		this.appId = appId;
	}

	public int getApplicationTableId() {
		return applicationTableId;
	}

	public void setApplicationTableId(int applicationTableId) {
		this.applicationTableId = applicationTableId;
	}
}