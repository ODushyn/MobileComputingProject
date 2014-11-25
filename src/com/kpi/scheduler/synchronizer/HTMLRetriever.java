package com.kpi.scheduler.synchronizer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class HTMLRetriever extends AsyncTask<String, Void, String> {

	private final String URL = "https://reg.kpi.ua/NP.Dev/WebService.asmx/InteropGetGroupSchedulesAsJson?";

	@Override
	protected String doInBackground(String... strings) {
		String result = "";
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
			      param.add(new BasicNameValuePair("groupName"," Ã-31Ï"));
			      param.add(new BasicNameValuePair("password","hello_mobile"));
			      httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
			      System.out.println(httpPost.getURI());
			      System.out.println(httpPost.getParams().getParameter("groupName"));
			      System.out.println(httpPost.getParams().getParameter("password"));
			      HttpResponse httpResponse = httpClient.execute(httpPost);
			      HttpEntity httpEntity = httpResponse.getEntity();
			      InputStream is = httpEntity.getContent();
		      
//			      BufferedReader bReader = new BufferedReader(new InputStreamReader(is), 20000);
//		            StringBuilder sBuilder = new StringBuilder();
//
//		            String line = null;
//		            while ((line = bReader.readLine()) != null) {
//		                sBuilder.append(line);
//		            }
//		           
//		            result = sBuilder.toString();
//		            System.out.println(result.toString());
		            
		           XmlPullParser parser = Xml.newPullParser();
		           parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		           parser.setInput(is, HTTP.UTF_8);
		           parser.nextTag();
		           result = getScheduleFromXml(parser);
		           System.out.println(result);
		           is.close();
            
			} catch (IOException e) {
		} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		
		return result.toString();
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
		MainActivity.tempText.setText(s);
	}	

}
