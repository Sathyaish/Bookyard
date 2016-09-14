package bookyard.client;

import java.util.HashMap;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import bookyard.contracts.Constants;
import bookyard.contracts.IAuthenticationManager;
import bookyard.contracts.OperationResult;
import com.fasterxml.jackson.core.type.TypeReference;

public class APIAuthenticationManager : IAuthenticationManager<String> {

    override public fun authenticateUser(userName : String?, password : String?,
                                         appId : String?, appSecret : String?) : OperationResult<String> {

        try {
            val claims : HashMap<String, Any?> = HashMap<String, Any?>();

            claims.put("iss", appId);
            claims.put("sub", "LoginRequest");
            claims.put("userName", userName);
            claims.put("password", password);

            // make a jwt out of the username and password
            val jwt : String = Jwts.builder()
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.HS256, appSecret)
                    .compact();

            // make a POST web request sending the jwt in the request body
            val loginUrl : String? = Constants().loginUrl;
            val body : String? = "appId=" + appId + "&token=" + jwt;
            val responseString : String? = WebRequest().Post(loginUrl, body);

            System.out.println(responseString);

            // deserialize the response into an OperationResult<String>
            val mapper : ObjectMapper = ObjectMapper();
            // val type : JavaType = mapper.getTypeFactory().constructParametricType(OperationResult::class, String::class);
            val result : OperationResult<String> = mapper.readValue<OperationResult<String>>(responseString,
                    object: TypeReference<OperationResult<String>>() { })

            // return that to the caller
            return result;
        }
        catch(ex : Exception) {
            return OperationResult<String>(false, ex.message, null);
        }
    }
}