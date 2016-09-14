package bookyard.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import bookyard.contracts.OperationResult;
import bookyard.contracts.beans.BookRecommendations;
import bookyard.contracts.beans.User;
import bookyard.server.util.*;

@WebServlet("/recommend")
public class RecommendServlet : HttpServlet() {

    override protected fun doGet(request : HttpServletRequest, response : HttpServletResponse) {

        val msg : String = "HTTP GET method not supported.";

        try {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
        } catch (e : IOException) {
            e.printStackTrace();
        }
    }

    override protected fun doPost(request : HttpServletRequest, response : HttpServletResponse) {

        val user : User? = request.getAttribute("User") as User;

        if (user == null)
        {
            val result : OperationResult<BookRecommendations> =
                    OperationResult<BookRecommendations>(false, "Internal server error.", null);

            val mapper : ObjectMapper = ObjectMapper();
            val resultString : String = mapper.writeValueAsString(result);
            response.getWriter().append(resultString);
            return;
        }

        val recommendations : BookRecommendations? = getBookRecommendations(user);

        if (recommendations == null)
        {
            println("Failed to retrieve user's book recommendations from the database.");

            val result : OperationResult<BookRecommendations> = OperationResult<BookRecommendations>(false, "Internal Server Error", null);

            val mapper : ObjectMapper = ObjectMapper();
            val resultString : String = mapper.writeValueAsString(result);
            response.getWriter().append(resultString);
            return;
        }

        val result : OperationResult<BookRecommendations> = OperationResult<BookRecommendations>(true, null, recommendations);

        val mapper : ObjectMapper = ObjectMapper();
        val resultString : String = mapper.writeValueAsString(result);
        response.getWriter().append(resultString);
        return;
    }
}