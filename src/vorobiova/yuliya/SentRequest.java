package vorobiova.yuliya;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SentRequest {
	String request;
	String url;
	public SentRequest(String request1, String url1) {
		this.request=request1;
		this.url=url1;
	}
	
	public String Sent() {
		String response = null;
		try {
    		URL svr = new URL(url);
    		HttpURLConnection con = (HttpURLConnection) svr.openConnection();
    		con.setRequestMethod("POST");
    		con.setDoOutput(true);
    		con.setDoInput(true);
    		con.connect();
    		
    		OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
    		writer.write(this.request);
    		writer.close();
    		if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
    			InputStream is = con.getInputStream();
    			InputStreamReader reader = new InputStreamReader(is);
    			StringBuffer data = new StringBuffer();
    			int c;
    			while ((c = reader.read()) != -1)
    				data.append((char) c);   		
    			response = data.toString();
    		} else {
    			response = "Error. Server does not response";
    		}
		} catch (MalformedURLException e) {
			response = "Error. Wrong url: " + e.getMessage();
		} catch (IOException e) {
			response = "Error. IOException: " + e.getMessage();
    	} catch (Exception e) {	
    		response = "Error. " + e.getMessage();
    	}
    	
    	return response;
    }
}

