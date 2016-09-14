package practice.bookyard.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import practice.bookyard.contracts.Constants;
import practice.bookyard.contracts.OperationResult;
import practice.bookyard.contracts.beans.Book;
import practice.bookyard.contracts.beans.BookRecommendations;
import practice.bookyard.contracts.beans.User;

public class BookRecommendationsFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private String accessToken = null;
	private User user = null;
	
	private JLabel lblHeader;
	private BookRecommendationsContentPanel pnlContent;
	private JPanel pnlStatus;
	private JLabel lblStatus;
	
	public BookRecommendationsFrame(String accessToken) { 
		
		User user = this.getUserFromAccessToken(accessToken);
		
		this.setAccessToken(accessToken);
		this.setUser(user);
		
		this.setTitle(String.format("Loading %s's book recommendations...", user.getFullName()));
		
		BookRecommendations recommendations = this.getBookRecommendations(accessToken);
		
		this.displayBookRecommendations(recommendations);
		
		this.setTitle(String.format("%s's (%s) book recommendations", user.getFullName(), user.getEmail()));
	}

	private void displayBookRecommendations(BookRecommendations recommendations) {
		
		if (recommendations == null) {
			this.displayErrorMessage();
		}
		else {
			if (recommendations.getBooks().size() == 0) {
				this.displayNoMatchingRecommendationsMessage();
			}
			else {
				this.displayBookRecommendationsWithData(recommendations);
			}
		}
	}

	private void displayBookRecommendationsWithData(BookRecommendations recommendations) {
		
		this.setLayout(new BorderLayout());
		
		lblHeader = this.getHeaderLabel(recommendations);
		pnlContent = this.getContentPanel(recommendations);
		pnlStatus = this.getStatusPanel();
		
		this.add(lblHeader, BorderLayout.NORTH);
		this.add(pnlContent, BorderLayout.CENTER);
		this.add(pnlStatus, BorderLayout.SOUTH);
		
		lblHeader.setBorder(new EmptyBorder(15, 10, 7, 10));
	}

	private JPanel getStatusPanel() {
		
		JPanel pnlStatus = new JPanel();
		pnlStatus.setBorder(new BevelBorder(BevelBorder.LOWERED));
		pnlStatus.setSize(this.getWidth(), 50);
		pnlStatus.setLayout(new BorderLayout());
		
		this.lblStatus = new JLabel("Status");
		pnlStatus.add(lblStatus, BorderLayout.SOUTH);
		
		return pnlStatus;
	}

	private BookRecommendationsContentPanel getContentPanel(BookRecommendations recommendations) {
		
		BookRecommendationsContentPanel pnlContent = new BookRecommendationsContentPanel(this.lblStatus);
		pnlContent.setSize((int)(this.getWidth() * 0.85), pnlContent.getHeight());
		
		for(Book book : recommendations.getBooks()) {
			
			pnlContent.AddBook(book);
		}
		
		return pnlContent;
	}

	

	private JLabel getHeaderLabel(BookRecommendations recommendations) {
		
		JLabel lblHeader = new JLabel();
		
		String headerText = String.format("We found the following %d books matching your likes: ", recommendations.getBooks().size());
		lblHeader.setText(headerText);
		lblHeader.setFont(new Font("Verdana", Font.PLAIN, 14));
		
		return lblHeader;
	}

	private void displayNoMatchingRecommendationsMessage() {
		this.setLayout(new BorderLayout());
		JLabel lblMessage = new JLabel();
		lblMessage.setFont(new Font("Verdana", Font.BOLD, 24));
		lblMessage.setForeground(Color.green);
		lblMessage.setText("There are no books matching your likes. Please try again later.");
		this.add(lblMessage, BorderLayout.CENTER);
	}

	private void displayErrorMessage() {
		this.setLayout(new BorderLayout());
		
		JLabel lblMessage = new JLabel();
		lblMessage.setFont(new Font("Verdana", Font.BOLD, 24));
		lblMessage.setForeground(Color.red);
		lblMessage.setText("There was an error retrieving your book recommendations. Please try again later.");
		this.add(lblMessage, BorderLayout.CENTER);
	}

	private BookRecommendations getBookRecommendations(String accessToken) {
		
		try
		{
			String recommendationsUrl = Constants.getRecommendationsUrl();
			
			String authorizationHeaderKey = "Authorization";
			String authorizationHeaderValue = String.format("Bearer %s", accessToken);
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put(authorizationHeaderKey, authorizationHeaderValue);
			
			String body = "appId=" + this.getUser().getAppId();
			
			String responseString = new WebRequest().Post(recommendationsUrl, body, headers);
			
			System.out.println(responseString);
			
			// deserialize the response into an OperationResult<BookRecommendations>
			ObjectMapper mapper = new ObjectMapper();
			JavaType type = mapper.getTypeFactory().constructParametricType(OperationResult.class, BookRecommendations.class);
			OperationResult<BookRecommendations> result = mapper.readValue(responseString, type);
			
			if  (result.getSuccessful())
			{
				return result.getData();
			}
			else
			{
				System.out.println(result.getErrorMessage());
				return null;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	private User getUserFromAccessToken(String accessToken) {
		
		try
		{
			String appSecret = "Auth0 is awesome!";
			
			Jws<Claims> jwsClaims = Jwts.parser()
					.setSigningKey(appSecret)
					.parseClaimsJws(accessToken);
			
			Claims body = jwsClaims.getBody();
			
			for(Map.Entry<String, Object> entry : body.entrySet())
			{
				System.out.println(entry.getKey() + ": " + entry.getValue());
			}
			
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

	public String getAccessToken() {
		return accessToken;
	}

	protected void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public User getUser() {
		return user;
	}

	protected void setUser(User user) {
		this.user = user;
	}
}
