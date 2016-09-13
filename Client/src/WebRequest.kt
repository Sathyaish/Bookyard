package bookyard.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection
import kotlin.collections.MutableMap;

public class WebRequest {

    public fun Post(url : String?, body : String?, headers : Map<String, String>? = null) : String?
    {
        return this.executePost(url,  body, headers);
    }

    private fun executePost(targetURL : String?, urlParameters : String?, headers : Map<String, String>? = null) : String? {

        var connection : HttpURLConnection? = null;

        try {
            //Create connection
            val url : URL = URL(targetURL);

            if (url.protocol.equals("https")) {
                HttpsURLConnection.setDefaultHostnameVerifier({ hostname, session -> true })
                connection = url.openConnection() as HttpsURLConnection;
            }
            else
            {
                connection = url.openConnection() as HttpURLConnection;
            }

            val contentLength : Int = if (urlParameters == null) 0 else urlParameters.toByteArray().size;

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", Integer.toString(contentLength));
            connection.setRequestProperty("Content-Language", "en-US");

            if (headers != null)
            {
                for ((k, v) in headers)
                {
                    connection.setRequestProperty(k, v);
                }
            }

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            if (urlParameters != null) {
                val wr : DataOutputStream = DataOutputStream (connection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.close();
            }

            //Get Response
            val inputStream : InputStream = connection.getInputStream();
            val rd : BufferedReader = BufferedReader(InputStreamReader(inputStream));
            val response : StringBuffer = StringBuffer();
            var line : String? = rd.readLine();

            while (line != null)
            {
                response.append(line);
                response.append('\r');

                line = rd.readLine();
            }

            rd.close();
            return response.toString();

        }
        catch (e : Exception)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }
    }
}