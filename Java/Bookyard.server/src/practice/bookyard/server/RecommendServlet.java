package practice.bookyard.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import practice.bookyard.contracts.OperationResult;
import practice.bookyard.contracts.beans.BookRecommendations;
import practice.bookyard.contracts.beans.User;
import practice.bookyard.server.util.Database;

@WebServlet("/recommend")
public class RecommendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public RecommendServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String msg = "HTTP GET method not supported.";
		
        try {
			response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		User user = (User)request.getAttribute("User");
		
		if (user == null)
		{
			OperationResult<BookRecommendations> result = 
					new OperationResult<BookRecommendations>(false, "Internal server error.", null);
			ObjectMapper mapper = new ObjectMapper();
			String resultString = mapper.writeValueAsString(result);
			response.getWriter().append(resultString);
			return;
		}
		
		BookRecommendations recommendations = Database.getBookRecommendations(user);
		
		OperationResult<BookRecommendations> result = 
				new OperationResult<BookRecommendations>(true, null, recommendations);
		ObjectMapper mapper = new ObjectMapper();
		String resultString = mapper.writeValueAsString(result);
		response.getWriter().append(resultString);
		return;
	}

}
