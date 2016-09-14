package practice.bookyard.server;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import practice.bookyard.contracts.Constants;
import practice.bookyard.contracts.OperationResult;
import practice.bookyard.contracts.beans.User;
import practice.bookyard.server.util.Database;
import practice.bookyard.server.util.DatabaseAuthenticationManager;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public LoginServlet() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		
		String msg = "HTTP GET method not supported.";
		
        try {
			response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPostInternal(request, response);
	}
	
	private void doPostInternal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try
		{
			String appId = request.getParameter("appId");
			
			if(appId == null || appId.length() == 0)
			{
				OperationResult<String> result = new OperationResult<String>(false, "Bad Request. Missing appId.", null);
				ObjectMapper mapper = new ObjectMapper();
				String resultString = mapper.writeValueAsString(result);
				response.getWriter().append(resultString);
				return;
			}
			
			// "Auth0 is awesome!";
			String appSecret = Database.getApplicationSecret(appId);
			
			if (appSecret == null || appSecret.length() == 0)
			{
				OperationResult<String> result = new OperationResult<String>(false, "Server error: appSecret not set.", null);
				ObjectMapper mapper = new ObjectMapper();
				String resultString = mapper.writeValueAsString(result);
				response.getWriter().append(resultString);
				return;
			}
			
			// parse the JWT in the request body
			String loginRequestJWT = request.getParameter("token");
			Jws<Claims> jwsClaims = Jwts.parser()
					.setSigningKey(appSecret)
					.parseClaimsJws(loginRequestJWT);
			
			if (jwsClaims == null)
			{
				OperationResult<String> result = new OperationResult<String>(false, "Invalid request: Bad request format.", null);
				ObjectMapper mapper = new ObjectMapper();
				String resultString = mapper.writeValueAsString(result);
				response.getWriter().append(resultString);
				return;
			}
			else
			{
				Claims body = jwsClaims.getBody();
				
				for(Map.Entry<String, Object> entry : body.entrySet())
				{
					System.out.println(entry.getKey() + ": " + entry.getValue());
				}
				
				// if it is not of type LoginRequest, send failure, Bad Request: Invalid request subject.
				if (!body.get("sub").toString().contentEquals(Constants.JWT_SUBJECT_LOGIN_REQUEST))
				{
					OperationResult<String> result = new OperationResult<String>(false, "Bad request format. Invalid subject.", null);
					ObjectMapper mapper = new ObjectMapper();
					String resultString = mapper.writeValueAsString(result);
					response.getWriter().append(resultString);
					return;
				}
				
				String userName = body.get("userName").toString();
				String password = body.get("password").toString();
				OperationResult<User> operationResultOfUser = new DatabaseAuthenticationManager().authenticateUser(userName, password, appId, appSecret);
				
				if (operationResultOfUser.getSuccessful() == false) {
					OperationResult<String> result = new OperationResult<String>(false, operationResultOfUser.getErrorMessage(), null);
					ObjectMapper mapper = new ObjectMapper();
					String resultString = mapper.writeValueAsString(result);
					response.getWriter().append(resultString);
					return;
				}
				
				User user = operationResultOfUser.getData();
				if (user == null) {
					OperationResult<String> result = new OperationResult<String>(false, "Invalid login", null);
					ObjectMapper mapper = new ObjectMapper();
					String resultString = mapper.writeValueAsString(result);
					response.getWriter().append(resultString);
					return;
				}
				
				HashMap<String, Object> claims = new HashMap<String, Object>();
				claims.put("iss", "Bookyard Server");
				claims.put("sub", "AccessToken");
				claims.put("userId", user.getId());
				claims.put("userName", user.getUserName());
				claims.put("fullName", user.getFullName());
				claims.put("email", user.getEmail());
				claims.put("appId", user.getAppId());
				claims.put("applicationTableId", user.getApplicationTableId());
				claims.put("generatedTimestamp", new Date().getTime());
				
				Date expiryDate = DateUtils.addHours(new Date(), 1);
				
				// make a jwt out of the username and password
				String accessToken = Jwts.builder()
						.setClaims(claims)
						.setExpiration(expiryDate)
						.signWith(SignatureAlgorithm.HS256, appSecret)
						.compact();
				
				// Save the token in the database
				boolean saved = Database.saveOrUpdateAccessToken(user.getId(), user.getUserName(), user.getApplicationTableId(), appId, accessToken, expiryDate);
				
				if (!saved)
				{
					OperationResult<String> result = new OperationResult<String>(false, "Internal server error", null);
					ObjectMapper mapper = new ObjectMapper();
					String resultString = mapper.writeValueAsString(result);
					response.getWriter().append(resultString);
					return;
				}
				
				OperationResult<String> result = new OperationResult<String>(true, null, accessToken);
				ObjectMapper mapper = new ObjectMapper();
				String resultString = mapper.writeValueAsString(result);
				response.getWriter().append(resultString);
				return;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			
			OperationResult<String> result = new OperationResult<String>(false, ex.getMessage(), null);
			ObjectMapper mapper = new ObjectMapper();
			String resultString = mapper.writeValueAsString(result);
			response.getWriter().append(resultString);
		}
	}
}
