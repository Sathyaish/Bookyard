package practice.bookyard.server.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import practice.bookyard.contracts.IAuthenticationManager;
import practice.bookyard.contracts.OperationResult;
import practice.bookyard.contracts.beans.User;

public class DatabaseAuthenticationManager implements IAuthenticationManager<User> {

	@Override
	public OperationResult<User> authenticateUser(String userName, String password, String appId, String appSecret) {
		
		// if the user exists with that user name and that password and has a membership of
		// that appication (issuer == appId), then
		// return a user object with the username, userid, full name, 
		// application name, email, appId, appTableId, issuer, expires
		// otherwise, return invlalid login.
		
		try
		{
			if (userName == null || userName.length() == 0)
			{
				return new OperationResult<User>(false, "Bad request. Missing userName.", null);
			}
			
			if (password == null || password.length() == 0)
			{
				return new OperationResult<User>(false, "Invalid login.", null);
			}
			
			String passwordHash = PasswordHash.Compute(password);
			System.out.println(passwordHash);
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT u.id, u.UserName, u.FullName, u.Email, a.Id AS ApplicationTableId, a.ApplicationId ");
			buffer.append(" FROM [User] u JOIN [Membership] m ON u.Id = m.UserId ");
			buffer.append(" JOIN [Application] a ON m.ApplicationTableId = a.Id ");
			buffer.append(" WHERE u.UserName = ? AND u.PasswordHash = ? AND m.ApplicationId = ?;");
			String sql = buffer.toString();
			
			Connection connection = Database.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, userName);
			statement.setString(2, passwordHash);
			statement.setString(3, appId);
			ResultSet resultSet = statement.executeQuery();
			
			User user = null;
			
			if (resultSet.next())
			{
				user = new User();
				user.setId(resultSet.getInt("Id"));
				user.setUserName(resultSet.getString("UserName"));
				user.setFullName(resultSet.getString("FullName"));
				user.setEmail(resultSet.getString("Email"));
				user.setAppId(resultSet.getString("ApplicationId"));
				user.setApplicationTableId(resultSet.getInt("ApplicationTableId"));
				
				return new OperationResult<User>(true, null, user);
			}
			else
			{
				return new OperationResult<User>(false, "Invalid login.", null);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return new OperationResult<User>(false, ex.getMessage(), null);
		}
	}
}
