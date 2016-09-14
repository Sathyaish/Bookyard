package practice.bookyard.server;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import practice.bookyard.contracts.beans.User;
import practice.bookyard.server.util.Database;

public class AuthorizationFilter implements javax.servlet.Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		
		String path = req.getServletPath();
		
		if (path.contentEquals("/login"))
		{
			req.setAttribute("User", null);
			chain.doFilter(request, response);
			return;
		}
		else
		{
			if (!req.getMethod().contentEquals("POST"))
			{
				req.setAttribute("User", null);
				chain.doFilter(request, response);
				return;
			}
			
			// Just check for the presence of the accessToken
			String bearerComponent = req.getHeader("Authorization");
			String[] bearerArray = bearerComponent.split(" ");
			String accessToken = bearerArray[1];
			
			System.out.println(bearerComponent);
			System.out.println(accessToken);
			
			// access token can be decrypted using the appId's appSecret
			String appId = req.getParameter("appId");
			if (appId == null || appId.length() == 0)
			{
				req.setAttribute("User", null);
				chain.doFilter(request, response);
				HttpServletResponse resp = (HttpServletResponse) response;
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing appId");
				return;
			}
			
			String appSecret = Database.getApplicationSecret(appId);
			User user = this.getUserFromAccessToken(accessToken, appSecret);
			
			if (user == null)
			{
				req.setAttribute("User", null);
				chain.doFilter(request, response);
				HttpServletResponse resp = (HttpServletResponse) response;
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid access token.");
				return;
			}
			
			// that a row exists against the userId obtained from the
			// access token in the access token table and that the token hasn't expired.
			boolean valid = Database.validateAccessToken(user.getId(), appId, accessToken, appSecret);
			
			if (!valid)
			{
				req.setAttribute("User", null);
				chain.doFilter(request, response);
				HttpServletResponse resp = (HttpServletResponse) response;
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expired access token.");
				return;
			}
			
			req.setAttribute("User", user);
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
	}
	
	private User getUserFromAccessToken(String accessToken, String appSecret) {
		
		try
		{
			Jws<Claims> jwsClaims = Jwts.parser()
					.setSigningKey(appSecret)
					.parseClaimsJws(accessToken);
			
			Claims body = jwsClaims.getBody();
			
			for(Map.Entry<String, Object> entry : body.entrySet())
			{
				System.out.println(entry.getKey() + ": " + entry.getValue());
			}
			
			User user = new User();
			user.setId((int)body.get("userId"));
			user.setUserName(body.get("userName").toString());
			user.setFullName(body.get("fullName").toString());
			user.setEmail(body.get("email").toString());
			user.setAppId(body.get("appId").toString());
			user.setApplicationTableId((int)body.get("applicationTableId"));
			
			return user;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

}
