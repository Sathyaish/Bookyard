package bookyard.server;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

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

import bookyard.contracts.OperationResult;
import bookyard.contracts.Constants;
import bookyard.server.util.*;
import bookyard.contracts.beans.*;

open class LoginServlet : HttpServlet() {

    override fun doGet(request : HttpServletRequest, response : HttpServletResponse) {
        val msg: String = "HTTP GET method not supported.";

        try {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
        } catch (e: IOException) {
            e.printStackTrace();
        }
    }

    override fun doPost(request : HttpServletRequest, response : HttpServletResponse) {
        this.doPostInternal(request, response);
    }

    private fun doPostInternal(request: HttpServletRequest, response: HttpServletResponse) {
        try
        {
            var appId : String? = request.getParameter("appId");

            if(appId == null || appId.length == 0)
            {
                val result : OperationResult<String?> = OperationResult<String?>(false, "Bad Request. Missing appId.", null);

                val mapper : ObjectMapper = ObjectMapper();
                val resultString : String? = mapper.writeValueAsString(result);
                response.getWriter().append(resultString);
                return;
            }

            // "Auth0 is awesome!";
            val appSecret : String? = getApplicationSecret(appId);

            if (appSecret == null || appSecret.length == 0)
            {
                val result : OperationResult<String> = OperationResult<String>(false, "Server error: appSecret not set.", null);

                val mapper : ObjectMapper = ObjectMapper();
                val resultString : String = mapper.writeValueAsString(result);
                response.getWriter().append(resultString);
                return;
            }

            // parse the JWT in the request body
            val loginRequestJWT : String = request.getParameter("token");
            val jwsClaims : Jws<Claims>? = Jwts.parser()
                    .setSigningKey(appSecret)
                    .parseClaimsJws(loginRequestJWT);

            if (jwsClaims == null)
            {
                val result : OperationResult<String> = OperationResult<String>(false, "Invalid request: Bad request format.", null);

                val mapper : ObjectMapper = ObjectMapper();
                val resultString : String  = mapper.writeValueAsString(result);
                response.getWriter().append(resultString);
                return;
            }
            else
            {
                val body : Claims = jwsClaims.getBody();

                for((k, v) in body)
                {
                    println(k + ": " + v);
                }

                if (!body.get("sub").toString().contentEquals(Constants().JWT_SUBJECT_LOGIN_REQUEST))
                {
                    val result : OperationResult<String> = OperationResult<String>(false, "Bad request format. Invalid subject.", null);

                    val mapper : ObjectMapper = ObjectMapper();
                    val resultString : String = mapper.writeValueAsString(result);
                    response.getWriter().append(resultString);
                    return;
                }

                val userName : String = body.get("userName").toString();
                val password : String = body.get("password").toString();
                val operationResultOfUser : OperationResult<User> = DatabaseAuthenticationManager().authenticateUser(userName, password, appId, appSecret);

                if (operationResultOfUser.successful == false) {
                    val result : OperationResult<String> = OperationResult<String>(false, operationResultOfUser.errorMessage, null);
                    val mapper : ObjectMapper = ObjectMapper();
                    val resultString : String = mapper.writeValueAsString(result);
                    response.getWriter().append(resultString);
                    return;
                }

                val user : User? = operationResultOfUser.data;
                if (user == null) {
                    val result : OperationResult<String> = OperationResult<String>(false, "Invalid login", null);
                    val mapper : ObjectMapper = ObjectMapper();
                    val resultString : String = mapper.writeValueAsString(result);
                    response.getWriter().append(resultString);
                    return;
                }

                val claims : HashMap<String, Any?> =  HashMap<String, Any?>();
                claims.put("iss", "Bookyard Server");
                claims.put("sub", "AccessToken");
                claims.put("userId", user.id);
                claims.put("userName", user.userName);
                claims.put("fullName", user.fullName);
                claims.put("email", user.email);
                claims.put("appId", user.appId);
                claims.put("applicationTableId", user.applicationTableId);
                claims.put("generatedTimestamp", Date().time);

                val expiryDate : Date = DateUtils.addHours(Date(), 1);

                // make a jwt out of the username and password
                val accessToken : String = Jwts.builder()
                        .setClaims(claims)
                        .setExpiration(expiryDate)
                        .signWith(SignatureAlgorithm.HS256, appSecret)
                        .compact();

                // Save the token in the database
                val saved : Boolean = saveOrUpdateAccessToken(
                        user.id, user.userName!!,
                        user.applicationTableId,
                        appId,
                        accessToken,
                        expiryDate);

                if (!saved)
                {
                    val result : OperationResult<String> = OperationResult<String>(false, "Internal server error", null);
                    val mapper : ObjectMapper = ObjectMapper();
                    val resultString : String = mapper.writeValueAsString(result);
                    response.getWriter().append(resultString);
                    return;
                }

                val result : OperationResult<String> = OperationResult<String>(true, null, accessToken);
                val mapper : ObjectMapper = ObjectMapper();
                val resultString : String = mapper.writeValueAsString(result);
                response.getWriter().append(resultString);
                return;
            }
        }
        catch(ex : Exception)
        {
            ex.printStackTrace();

            val result : OperationResult<String> = OperationResult<String>(false, ex.message, null);
            val mapper : ObjectMapper = ObjectMapper();
            val resultString : String = mapper.writeValueAsString(result);
            response.getWriter().append(resultString);
        }
    }
}