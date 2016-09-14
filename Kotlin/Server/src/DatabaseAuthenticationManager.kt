package bookyard.server.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import bookyard.contracts.beans.User;
import bookyard.contracts.IAuthenticationManager;
import bookyard.contracts.OperationResult;

public class DatabaseAuthenticationManager :  IAuthenticationManager<User> {

    override public fun authenticateUser(userName : String?,
                                         password : String?,
                                         appId : String?,
                                         appSecret : String?) : OperationResult<User> {

        // if the user exists with that user name and that password and has a membership of
        // that appication (issuer == appId), then
        // return a user object with the username, userid, full name,
        // application name, email, appId, appTableId, issuer, expires
        // otherwise, return invlalid login.

        try
        {
            if (userName == null || userName.length == 0)
            {
                return OperationResult<User>(false, "Bad request. Missing userName.", null);
            }

            if (password == null || password.length == 0)
            {
                return OperationResult<User>(false, "Invalid login.", null);
            }

            val passwordHash : String = PasswordHash().Compute(password);
            System.out.println(passwordHash);

            val buffer : StringBuffer = StringBuffer();
            buffer.append("SELECT u.id, u.UserName, u.FullName, u.Email, a.Id AS ApplicationTableId, a.ApplicationId ");
            buffer.append(" FROM [User] u JOIN [Membership] m ON u.Id = m.UserId ");
            buffer.append(" JOIN [Application] a ON m.ApplicationTableId = a.Id ");
            buffer.append(" WHERE u.UserName = ? AND u.PasswordHash = ? AND m.ApplicationId = ?;");
            val sql : String = buffer.toString();

            val connection : Connection? = getConnection();

            if (connection == null)
            {
                println("Could not obtain a valid database connection. Connection is null.");
                return OperationResult<User>(false, "Internal server error.", null);
            }

            val statement : PreparedStatement = connection.prepareStatement(sql);
            statement.setString(1, userName);
            statement.setString(2, passwordHash);
            statement.setString(3, appId);
            val resultSet : ResultSet = statement.executeQuery();

            var user : User?;

            if (resultSet.next())
            {
                user = User();

                user.id = resultSet.getInt("Id");
                user.userName  = resultSet.getString("UserName");
                user.fullName = resultSet.getString("FullName");
                user.email = resultSet.getString("Email");
                user.appId = resultSet.getString("ApplicationId");
                user.applicationTableId = resultSet.getInt("ApplicationTableId");

                return OperationResult<User>(true, null, user);
            }
            else
            {
                return OperationResult<User>(false, "Invalid login.", null);
            }
        }
        catch(ex : Exception)
        {
            ex.printStackTrace();
            return OperationResult<User>(false, ex.message, null);
        }
    }
}