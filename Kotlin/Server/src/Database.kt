package bookyard.server.util;

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
import bookyard.contracts.Constants;
import bookyard.contracts.beans.Book;
import bookyard.contracts.beans.BookRecommendations;
import bookyard.contracts.beans.User;


public fun getConnection() : Connection? {
    val url : String? = "jdbc:sqlserver://localhost:1433;instance=ADMIN-VAIO\\SQLEXPRESS;databaseName=Bookyard;integratedSecurity=true;";

    var connection : Connection? = null;

    try
    {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        connection = DriverManager.getConnection(url);
    }
    catch(e : SQLException)
    {
        e.printStackTrace();
    }
    catch(e : ClassNotFoundException)
    {
        e.printStackTrace();
    }

    return connection;
}

public fun getApplicationSecret(appId : String?) : String? {

    var applicationSecret : String? = null;

    try
    {
        val connection : Connection = getConnection()!!;
        val sql : String = "SELECT ApplicationSecret FROM Application WHERE ApplicationId = ?;";

        val statement : PreparedStatement = connection.prepareStatement(sql);
        statement.setString(1, appId);
        val resultSet : ResultSet = statement.executeQuery();

        if (resultSet.next()) {
            applicationSecret = resultSet.getString("ApplicationSecret");
        }
    }
    catch(ex : Exception)
    {
        ex.printStackTrace();
    }

    return applicationSecret;
}

private fun accessTokenExists(userId : Int,
                              userName : String,
                              applicationTableId : Int,
                              appId : String) : Boolean {

    val connection : Connection = getConnection()!!;

    val sql : String = "SELECT * FROM AccessToken WHERE UserId = ? AND UserName = ? AND ApplicationTableId = ? AND ApplicationId = ?;";
    val statement : PreparedStatement = connection.prepareStatement(sql);
    statement.setInt(1,  userId);
    statement.setString(2, userName);
    statement.setInt(3, applicationTableId);
    statement.setString(4, appId);

    val resultSet : ResultSet = statement.executeQuery();
    val exists : Boolean = resultSet.next();

    statement.close();
    if (!connection.isClosed()) connection.close();

    return exists;
}

private fun updateAccessToken(accessToken : String,
                              expiryDate : Date,
                              userId : Int) : Boolean {

    try
    {
        val connection : Connection = getConnection()!!;
        val sql : String = "UPDATE AccessToken SET AccessToken.AccessToken = ?, AccessToken.ExpiryDate = ? WHERE AccessToken.UserId = ?;";
        val statement : PreparedStatement = connection.prepareStatement(sql);
        statement.setString(1,  accessToken);

        val expiryTimestamp : java.sql.Timestamp = java.sql.Timestamp(expiryDate.getTime());
        System.out.println("javaUtilExpiryDate: " + expiryDate.toString());
        System.out.println("javaSqlExpiryDate: " + expiryTimestamp.toString());
        statement.setTimestamp(2, expiryTimestamp);

        statement.setInt(3, userId);
        statement.execute();
        statement.close();
        if (!connection.isClosed()) connection.close();

        return true;
    }
    catch(ex : Exception)
    {
        ex.printStackTrace();
        return false;
    }
}

private fun insertNewAccessToken(userId : Int,
                                 userName : String,
                                 applicationTableId : Int,
                                 appId : String,
                                 accessToken : String,
                                 expiryDate : Date) : Boolean {
    try
    {
        val connection : Connection = getConnection()!!;
        val sql : String = "INSERT INTO AccessToken VALUES(?, ?, ?, ?, ?, ?);";
        val statement : PreparedStatement = connection.prepareStatement(sql);

        statement.setInt(1,  userId);
        statement.setString(2, userName);
        statement.setInt(3, applicationTableId);
        statement.setString(4, appId);
        statement.setString(5, accessToken);

        val expiryTimestamp : java.sql.Timestamp = java.sql.Timestamp(expiryDate.getTime());
        System.out.println("javaUtilExpiryDate: " + expiryDate.toString());
        System.out.println("javaSqlExpiryDate: " + expiryTimestamp.toString());
        statement.setTimestamp(6, expiryTimestamp);

        statement.execute();
        statement.close();
        if (!connection.isClosed()) connection.close();

        return true;
    }
    catch(ex : Exception)
    {
        ex.printStackTrace();
        return false;
    }
}

public fun saveOrUpdateAccessToken(
        userId : Int,
        userName : String,
        applicationTableId : Int,
        appId : String,
        accessToken : String,
        expiryDate : Date) : Boolean {

    try
    {
        val exists : Boolean = accessTokenExists(userId, userName, applicationTableId, appId);

        if (exists)
        {
            return updateAccessToken(accessToken, expiryDate, userId);
        }
        else
        {
            return insertNewAccessToken(userId,
                    userName,
                    applicationTableId,
                    appId,
                    accessToken,
                    expiryDate);
        }
    }
    catch(ex : Exception)
    {
        ex.printStackTrace();
        return false;
    }
}

public fun validateAccessToken(userId : Int,
                               appId : String,
                               accessToken : String,
                               appSecret : String) : Boolean {

    try
    {
        val jwsClaims : Jws<Claims> = Jwts.parser()
                .setSigningKey(appSecret)
                .parseClaimsJws(accessToken);

        val body : Claims = jwsClaims.getBody();

        // if it is not of type AccessToken, throw
        if (!body.get("sub").toString().contentEquals(Constants().JWT_SUBJECT_ACCESS_TOKEN))
        {
            throw IllegalArgumentException("Does not represent a valid access token.", null);
        }

        // if it is not of type AccessToken, throw
        if (!body.get("iss").toString().contentEquals("Bookyard Server"))
        {
            throw IllegalArgumentException("Invalid token source.", null);
        }

        val connection : Connection = getConnection()!!;
        val sql : String = "SELECT * FROM AccessToken WHERE UserId = ? AND ApplicationId = ? AND AccessToken = ?;";
        val statement : PreparedStatement = connection.prepareStatement(sql);

        statement.setInt(1,  userId);
        statement.setString(2, appId);
        statement.setString(3, accessToken);

        val resultSet : ResultSet? = statement.executeQuery();

        if (resultSet == null)
        {
            return false;
        }

        if (resultSet.next())
        {
            val expiryTimestamp : java.sql.Timestamp = resultSet.getTimestamp("ExpiryDate");

            val threshold : Date = DateUtils.addSeconds(Date(), 1);

            return expiryTimestamp.after(threshold);
        }

        return false;
    }
    catch(ex : Exception)
    {
        ex.printStackTrace();
        return false;
    }
}

public fun getBookRecommendations(user : User) : BookRecommendations? {

    try
    {
        val connection : Connection = getConnection()!!;

        val buffer : StringBuffer = StringBuffer();
        buffer.append("SELECT DISTINCT b.* FROM Book b JOIN ");
        buffer.append(" (SELECT Name FROM [User] u JOIN UserLike ul ON u.Id = ul.UserId JOIN Likeable l ON l.Id = ul.LikeableId) n ");
        buffer.append(" ON b.Name LIKE '%' + n.Name + '%' OR b.[Description] LIKE '%' + n.Name + '%'");
        val sql : String = buffer.toString();

        val statement : Statement = connection.createStatement();

        val resultSet : ResultSet = statement.executeQuery(sql);

        val recommendations : BookRecommendations = BookRecommendations(user.id, user.userName);

        while(resultSet.next()) {

            val book : Book = Book();

            book.id = resultSet.getInt("Id");
            book.name = resultSet.getString("Name");
            book.author = resultSet.getString("Author");
            book.description = resultSet.getString("Description");
            book.amazonUrl = resultSet.getString("AmazonUrl");

            recommendations.addBook(book);
        }

        statement.close();
        if (!connection.isClosed()) connection.close();

        return recommendations;
    }
    catch(ex : Exception)
    {
        ex.printStackTrace();
        return null;
    }
}