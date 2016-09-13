package bookyard.server;

import java.io.IOException;

import javax.servlet.Filter;
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
import bookyard.contracts.Constants;
import bookyard.contracts.beans.User;
import bookyard.server.util.*;


public class AuthorizationFilter : Filter {

    override public fun init(filterConfig : FilterConfig) {
    }

    override public fun doFilter(request : ServletRequest, response : ServletResponse, chain : FilterChain) {

        val req : HttpServletRequest = request as HttpServletRequest;

        val path : String = req.getServletPath();

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
            val bearerComponent : String = req.getHeader("Authorization");
            val bearerArray : List<String> = bearerComponent.split(" ");
            val accessToken : String = bearerArray[1];

            System.out.println(bearerComponent);
            System.out.println(accessToken);

            // access token can be decrypted using the appId's appSecret
            val appId : String? = req.getParameter("appId");
            if (appId == null || appId.length == 0)
            {
                req.setAttribute("User", null);
                chain.doFilter(request, response);

                val resp : HttpServletResponse = response as HttpServletResponse;
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing appId");
                return;
            }

            val appSecret : String? = getApplicationSecret(appId);

            if (appSecret == null || appSecret.length == 0)
            {
                req.setAttribute("User", null);
                chain.doFilter(request, response);

                val resp : HttpServletResponse = response as HttpServletResponse;
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid appId.");
                return;
            }

            val user : User? = this.getUserFromAccessToken(accessToken, appSecret);

            if (user == null)
            {
                req.setAttribute("User", null);
                chain.doFilter(request, response);

                val resp : HttpServletResponse = response as HttpServletResponse;
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid access token.");
                return;
            }

            // that a row exists against the userId obtained from the
            // access token in the access token table and that the token hasn't expired.
            val valid : Boolean = validateAccessToken(user.id, appId, accessToken, appSecret);

            if (!valid)
            {
                req.setAttribute("User", null);
                chain.doFilter(request, response);

                val resp : HttpServletResponse = response as HttpServletResponse;
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Expired access token.");
                return;
            }

            req.setAttribute("User", user);
            chain.doFilter(request, response);
        }
    }

    override public fun destroy() {
    }

    private fun getUserFromAccessToken(accessToken : String, appSecret : String) : User? {

        try
        {
            val jwsClaims : Jws<Claims> = Jwts.parser()
                    .setSigningKey(appSecret)
                    .parseClaimsJws(accessToken);

            val body : Claims = jwsClaims.getBody();

            for((k, v) in body)
            {
                println("$k: $v");
            }

            /*for(Map.Entry<String, Object> entry : body.entrySet())
            {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }*/

            val user : User = User();
            user.id = body.get("userId") as Int;
            user.userName = body.get("userName").toString();
            user.fullName = body.get("fullName").toString();
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