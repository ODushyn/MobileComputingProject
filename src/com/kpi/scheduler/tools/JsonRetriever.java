package com.kpi.scheduler.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.util.Xml;

public class JsonRetriever extends AsyncTask<String, Void, String> {

	private final String URL = "https://reg.kpi.ua/NP.Dev/WebService.asmx/InteropGetGroupSchedulesAsJson?";
	private String jsonResult;
	
	@Override
	protected String doInBackground(String... strings) {
		jsonResult = "";
		TrustManager[] trustAllCerts = new TrustManager[]{
			    new X509TrustManager() {
			        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			            return null;
			        }
			        public void checkClientTrusted(
			            java.security.cert.X509Certificate[] certs, String authType) {
			        }
			        public void checkServerTrusted(
			            java.security.cert.X509Certificate[] certs, String authType) {
			        }
			    }
			};

			// Install the all-trusting trust manager
			try {
			    SSLContext sc = SSLContext.getInstance("SSL");
			    sc.init(null, trustAllCerts, new java.security.SecureRandom());
			    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			} catch (Exception e) {
			}

			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				URI uri = new URI(URL);
			      HttpPost httpPost = new HttpPost(uri);
			      
			      ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
			      param.add(new BasicNameValuePair("groupName", strings[0]));
			      param.add(new BasicNameValuePair("password","hello_mobile"));
			      httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
			      HttpResponse httpResponse = httpClient.execute(httpPost);
			      HttpEntity httpEntity = httpResponse.getEntity();
			      InputStream is = httpEntity.getContent();
		      
	            
		           XmlPullParser parser = Xml.newPullParser();
		           parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		           parser.setInput(is, HTTP.UTF_8);
		           parser.nextTag();
		           
		           jsonResult = getScheduleFromXml(parser);

		           is.close();
            
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e){
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			}
		
		return jsonResult.toString();
	}

	private String getScheduleFromXml(XmlPullParser parser) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, null, "string");
	    String schedule = "";
	        String name = parser.getName();
	        if (name.equals("string")) {
	        	if (parser.next() == XmlPullParser.TEXT) {
	    	        schedule = parser.getText();	    	        
	    	    }
	        }	
	    return schedule;
	}
	
	@Override
	protected void onPostExecute(String s) {
		super.onPostExecute(s);
	}	
	
	public String getJsonResult(){
		return this.jsonResult;
	}
}
