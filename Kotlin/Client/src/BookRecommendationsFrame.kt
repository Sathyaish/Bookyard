package bookyard.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import bookyard.contracts.Constants;
import bookyard.contracts.OperationResult;
import bookyard.contracts.beans.Book;
import bookyard.contracts.beans.BookRecommendations;
import bookyard.contracts.beans.User;

public class BookRecommendationsFrame(var accessToken : String?) : JFrame() {

    var user : User? = null;
    var lblHeader : JLabel? = null;
    var pnlContent : BookRecommendationsContentPanel? = null;
    var pnlStatus : JPanel? = null;
    var lblStatus : JLabel? = null;

    init {

        user = this.getUserFromAccessToken(accessToken);

        this.setTitle("Loading ${user!!.fullName}'s book recommendations...");

        val recommendations : BookRecommendations? = this.getBookRecommendations(accessToken);

        this.displayBookRecommendations(recommendations);

        this.setTitle("${user!!.fullName}'s (${user!!.email}) book recommendations");
    }

    private fun displayBookRecommendations(recommendations : BookRecommendations?) {

        if (recommendations == null) {
            this.displayErrorMessage();
        }
        else {
            if (recommendations.books.size == 0) {
                this.displayNoMatchingRecommendationsMessage();
            }
            else {
                this.displayBookRecommendationsWithData(recommendations);
            }
        }
    }

    private fun displayBookRecommendationsWithData(recommendations : BookRecommendations) {

        this.setLayout(BorderLayout());

        lblHeader = this.getHeaderLabel(recommendations);
        pnlStatus = this.getStatusPanel();
        pnlContent = this.getContentPanel(recommendations);

        this.add(lblHeader, BorderLayout.NORTH);
        this.add(pnlContent, BorderLayout.CENTER);
        this.add(pnlStatus, BorderLayout.SOUTH);

        lblHeader!!.setBorder(EmptyBorder(15, 10, 7, 10));
    }

    private fun getStatusPanel() : JPanel {

        val pnlStatus : JPanel = JPanel();
        pnlStatus.setBorder(BevelBorder(BevelBorder.LOWERED));
        pnlStatus.setSize(this.getWidth(), 50);
        pnlStatus.setLayout(BorderLayout());

        this.lblStatus = JLabel("Status");
        pnlStatus.add(lblStatus, BorderLayout.SOUTH);

        return pnlStatus;
    }

    private fun getContentPanel(recommendations : BookRecommendations) : BookRecommendationsContentPanel {

        pnlContent = BookRecommendationsContentPanel(this.lblStatus!!);
        pnlContent!!.setSize((this.getWidth() * 0.85).toInt(), pnlContent!!.getHeight());

        for(book in recommendations.books) {

            pnlContent!!.AddBook(book!!);
        }

        return pnlContent!!;
    }



    private fun getHeaderLabel(recommendations : BookRecommendations) : JLabel {

        val lblHeader : JLabel = JLabel();
        val headerText : String = "We found the following ${recommendations.books.size} books matching your likes: ";
        lblHeader.setText(headerText);
        lblHeader.setFont(Font("Verdana", Font.PLAIN, 14));

        return lblHeader;
    }

    private fun displayNoMatchingRecommendationsMessage() {

        this.setLayout(BorderLayout());

        val lblMessage : JLabel = JLabel();
        lblMessage.setFont(Font("Verdana", Font.BOLD, 24));
        lblMessage.setForeground(Color.green);
        lblMessage.setText("There are no books matching your likes. Please try again later.");
        this.add(lblMessage, BorderLayout.CENTER);
    }

    private fun displayErrorMessage() {

        this.setLayout(BorderLayout());

        val lblMessage : JLabel = JLabel();
        lblMessage.setFont(Font("Verdana", Font.BOLD, 24));
        lblMessage.setForeground(Color.red);
        lblMessage.setText("There was an error retrieving your book recommendations. Please try again later.");
        this.add(lblMessage, BorderLayout.CENTER);
    }

    private fun getBookRecommendations(accessToken : String?) : BookRecommendations? {

        try
        {
            val recommendationsUrl : String = Constants().recommendationsUrl;

            val authorizationHeaderKey : String = "Authorization";
            val authorizationHeaderValue : String = "Bearer ${accessToken}";

            val headers : MutableMap<String, String> = HashMap<String, String>();
            headers.put(authorizationHeaderKey, authorizationHeaderValue);

            val body : String = "appId=${this.user!!.appId!!}";

            val responseString : String? = WebRequest().Post(recommendationsUrl, body, headers);

            System.out.println(responseString);

            // deserialize the response into an OperationResult<BookRecommendations>
            val mapper : ObjectMapper = ObjectMapper();
            val result : OperationResult<BookRecommendations> = mapper.readValue<OperationResult<BookRecommendations>>(responseString,
                    object : TypeReference<OperationResult<BookRecommendations>>() { });

            if  (result.successful)
            {
                return result.data;
            }
            else
            {
                System.out.println(result.errorMessage);
                return null;
            }
        }
        catch(ex : Exception)
        {
            ex.printStackTrace();
            return null;
        }
    }

    private fun getUserFromAccessToken(accessToken : String?) : User? {

        try
        {
            val appSecret : String = "Auth0 is awesome!";

            val jwsClaims : Jws<Claims> = Jwts.parser()
                    .setSigningKey(appSecret)
                    .parseClaimsJws(accessToken);

            val body : Claims = jwsClaims.getBody();

            for((k, v) in body) {
                println("$k : $v");
            }

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

            val user : User = User();
            user.id =  body.get("userId") as Int;
            user.userName =  body.get("userName").toString();
            user.fullName =  body.get("fullName").toString();
            user.email = body.get("email").toString();
            user.appId = body.get("appId").toString();
            user.applicationTableId = body.get("applicationTableId") as Int;

            return user;
        }
        catch(ex : Exception)
        {
            ex.printStackTrace();
            return null;
        }
    }
}