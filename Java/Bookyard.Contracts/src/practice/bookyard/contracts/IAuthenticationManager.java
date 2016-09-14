package practice.bookyard.contracts;

public interface IAuthenticationManager<T> {
	OperationResult<T> authenticateUser(String userName, String password, 
			String appId, String appSecret);
}
