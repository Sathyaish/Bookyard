package practice.bookyard.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class WebRequest {

	public String Post(String url, String body)
	{
		return this.executePost(url,  body, null);
	}
	
	public String Post(String url, String body, Map<String, String> headers)
	{
		return this.executePost(url,  body, headers);
	}
	
	private String executePost(String targetURL, String urlParameters, Map<String, String> headers) {
		
		HttpURLConnection connection = null;
		// HttpsURLConnection.setDefaultHostnameVerifier ((hostname, session) -> true);
		
		  try {
		    //Create connection
		    URL url = new URL(targetURL);
		    connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("POST");
		    connection.setRequestProperty("Content-Type", 
		        "application/x-www-form-urlencoded");

		    
		    int contentLength = urlParameters == null ? 0 : urlParameters.getBytes().length;
		    
		    connection.setRequestProperty("Content-Length",
		        Integer.toString(contentLength));
		    connection.setRequestProperty("Content-Language", "en-US");
		    
		    if (headers != null) {
		    	for (Map.Entry<String, String> entry : headers.entrySet()) {
		    		connection.setRequestProperty(entry.getKey(), entry.getValue());
		    	}
		    }

		    connection.setUseCaches(false);
		    connection.setDoOutput(true);

		    //Send request
		    if (urlParameters != null) {
		    	DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
		    	wr.writeBytes(urlParameters);
				wr.close();
		    }

		    //Get Response  
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
		    String line;
		    while ((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rd.close();
		    return response.toString();
		  } catch (Exception e) {
		    e.printStackTrace();
		    return null;
		  } finally {
		    if (connection != null) {
		      connection.disconnect();
		    }
		  }
		}
}
