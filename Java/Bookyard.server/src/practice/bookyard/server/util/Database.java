package practice.bookyard.server.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import practice.bookyard.contracts.Constants;
import practice.bookyard.contracts.beans.Book;
import practice.bookyard.contracts.beans.BookRecommendations;
import practice.bookyard.contracts.beans.User;

public class Database {
	
	public static Connection getConnection() {
		String url = "jdbc:sqlserver://localhost:1433;instance=ADMIN-VAIO\\SQLEXPRESS;databaseName=Bookyard;integratedSecurity=true;";
		Connection connection = null;
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			connection = DriverManager.getConnection(url);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return connection;
	}
	
	public static String getApplicationSecret(String appId) {
		
		String applicationSecret = null;
		
		try
		{
			Connection connection = Database.getConnection();
			String sql = "SELECT ApplicationSecret FROM Application WHERE ApplicationId = ?;";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, appId);
			ResultSet resultSet = statement.executeQuery();
			
			if (resultSet.next()) {
				applicationSecret = resultSet.getString("ApplicationSecret");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return applicationSecret;
	}

	public static boolean saveOrUpdateAccessToken(
			int userId, String userName, int applicationTableId, String appId,
			String accessToken, Date expiryDate) {
		
		try
		{
			Connection connection = Database.getConnection();
			String sql = "SELECT * FROM AccessToken WHERE UserId = ? AND UserName = ? AND ApplicationTableId = ? AND ApplicationId = ?;";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1,  userId);
			statement.setString(2, userName);
			statement.setInt(3, applicationTableId);
			statement.setString(4, appId);
			
			ResultSet resultSet = statement.executeQuery();
			boolean exists = resultSet.next();
			statement.close();
			if (!connection.isClosed()) connection.close();
			
			if (exists)
			{
				// if an access token exists for the userId, userName, applicationTableId, appId
				// update its accessToken and expiryDate
				connection = Database.getConnection();
				sql = "UPDATE AccessToken SET AccessToken.AccessToken = ?, AccessToken.ExpiryDate = ? WHERE AccessToken.UserId = ?;";
				statement = connection.prepareStatement(sql);
				statement.setString(1,  accessToken);
				
				java.sql.Timestamp expiryTimestamp = new java.sql.Timestamp(expiryDate.getTime());
				System.out.println("javaUtilExpiryDate: " + expiryDate.toString());
				System.out.println("javaSqlExpiryDate: " + expiryTimestamp.toString());
				statement.setTimestamp(2, expiryTimestamp);
				
				statement.setInt(3, userId);
				statement.execute();
				statement.close();
				if (!connection.isClosed()) connection.close();
				return true;
			}
			else
			{
				// otherwise insert a new one there
				connection = Database.getConnection();
				sql = "INSERT INTO AccessToken VALUES(?, ?, ?, ?, ?, ?);";
				statement = connection.prepareStatement(sql);
				
				statement.setInt(1,  userId);
				statement.setString(2, userName);
				statement.setInt(3, applicationTableId);
				statement.setString(4, appId);
				statement.setString(5, accessToken);
				
				java.sql.Timestamp expiryTimestamp = new java.sql.Timestamp(expiryDate.getTime());
				System.out.println("javaUtilExpiryDate: " + expiryDate.toString());
				System.out.println("javaSqlExpiryDate: " + expiryTimestamp.toString());
				statement.setTimestamp(6, expiryTimestamp);
				
				statement.execute();
				statement.close();
				if (!connection.isClosed()) connection.close();
				return true;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}

	public static boolean validateAccessToken(int userId, String appId, String accessToken, String appSecret) {
		
		try
		{
			Jws<Claims> jwsClaims = Jwts.parser()
					.setSigningKey(appSecret)
					.parseClaimsJws(accessToken);
			
			Claims body = jwsClaims.getBody();
			
			// if it is not of type AccessToken, throw
			if (!body.get("sub").toString().contentEquals(Constants.JWT_SUBJECT_ACCESS_TOKEN))
			{
				throw new IllegalArgumentException("Does not represent a valid access token.", null);
			}
			
			// if it is not of type AccessToken, throw
			if (!body.get("iss").toString().contentEquals("Bookyard Server"))
			{
				throw new IllegalArgumentException("Invalid token source.", null);
			}
									
			Connection connection = Database.getConnection();
			String sql = "SELECT * FROM AccessToken WHERE UserId = ? AND ApplicationId = ? AND AccessToken = ?;";
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setInt(1,  userId);
			statement.setString(2, appId);
			statement.setString(3, accessToken);
			
			ResultSet resultSet = statement.executeQuery();
			
			if (resultSet == null)
			{
				return false;
			}
			
			if (resultSet.next())
			{
				java.sql.Timestamp expiryTimestamp = resultSet.getTimestamp("ExpiryDate");
				
				Date threshold = DateUtils.addSeconds(new Date(), 1);
				
				return expiryTimestamp.after(threshold);
			}
			
			return false;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}

	public static BookRecommendations getBookRecommendations(User user) {
		
		try
		{
			Connection connection = Database.getConnection();
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("SELECT DISTINCT b.* FROM Book b JOIN ");
			buffer.append(" (SELECT Name FROM [User] u JOIN UserLike ul ON u.Id = ul.UserId JOIN Likeable l ON l.Id = ul.LikeableId) n ");
			buffer.append(" ON b.Name LIKE '%' + n.Name + '%' OR b.[Description] LIKE '%' + n.Name + '%'");
			String sql = buffer.toString();
			
			Statement statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery(sql);
			
			BookRecommendations recommendations = new BookRecommendations(user.getId(), user.getUserName());
			
			while(resultSet.next()) {
				
				Book book = new Book();
				
				book.setId(resultSet.getInt("Id"));
				book.setName(resultSet.getString("Name"));
				book.setAuthor(resultSet.getString("Author"));
				book.setDescription(resultSet.getString("Description"));
				book.setAmazonUrl(resultSet.getString("AmazonUrl"));
				
				recommendations.addBook(book);
			}
			
			statement.close();
			if (!connection.isClosed()) connection.close();
			
			return recommendations;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
}
