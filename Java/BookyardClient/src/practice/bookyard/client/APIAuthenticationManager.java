package practice.bookyard.client;

import java.util.HashMap;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import practice.bookyard.contracts.Constants;
import practice.bookyard.contracts.IAuthenticationManager;
import practice.bookyard.contracts.OperationResult;

public class APIAuthenticationManager implements IAuthenticationManager<String> {

	@Override
	public OperationResult<String> authenticateUser(String userName, String password, 
			String appId, String appSecret) {
		
		try {
			HashMap<String, Object> claims = new HashMap<String, Object>();
			claims.put("iss", appId);
			claims.put("sub", "LoginRequest");
			claims.put("userName", userName);
			claims.put("password", password);
			
			// make a jwt out of the username and password
			String jwt = Jwts.builder()
					.setClaims(claims)
					.signWith(SignatureAlgorithm.HS256, appSecret)
					.compact();
					
			// make a POST web request sending the jwt in the request body
			String loginUrl = Constants.getLoginUrl();
			String body = "appId=" + appId + "&token=" + jwt;
			String responseString = new WebRequest().Post(loginUrl, body);
			
			System.out.println(responseString);
			
			// deserialize the response into an OperationResult<String>
			ObjectMapper mapper = new ObjectMapper();
			JavaType type = mapper.getTypeFactory().constructParametricType(OperationResult.class, String.class);
			OperationResult<String> result = mapper.readValue(responseString, type);
			
			// return that to the caller
			return result;
		}
		catch(Exception ex) {
			return new OperationResult<String>(false, ex.getMessage(), null);
		}
		
	}

}
