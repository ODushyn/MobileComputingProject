package com.kpi.scheduler.synchronizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class HTMLRetriever extends AsyncTask<String, Void, String> {
	 
    @Override
    protected String doInBackground(String... strings) {
        StringBuffer buffer = new StringBuffer();
        try {
            Log.d("JSwa", "Connecting to ["+strings[0]+"]");
            Document doc  = Jsoup.connect(strings[0]).get();
            Log.d("JSwa", "Connected to ["+strings[0]+"]");
            // Get document (HTML page) title
            String title = doc.title();
            Log.d("JSwA", "Title ["+title+"]");
            buffer.append("Title: " + title + "\r\n");

            // Get meta info
            Elements metaElems = doc.select("meta");
            buffer.append("META DATA\r\n");
            for (Element metaElem : metaElems) {
                String name = metaElem.attr("name");
                String content = metaElem.attr("content");
                buffer.append("name ["+name+"] - content ["+content+"] \r\n");
            }

            Elements topicList = doc.select("h2.topic");
            buffer.append("Topic list\r\n");
            for (Element topic : topicList) {
                String data = topic.text();

                buffer.append("Data ["+data+"] \r\n");
            }
            send();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }

        return buffer.toString();
    }



    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        MainActivity.tempText.setText(s);
    }
    
    private void send(){
        //get message from message box
        String  msg = "ÊÌ-31ì";
         
        //check whether the msg empty or not
        if(msg.length()>0) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.fpm.ntu-kpi.kiev.ua/scheduler/group.do");
             
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                   nameValuePairs.add(new BasicNameValuePair("id", "gr_262"));
                   httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                   HttpResponse response = httpclient.execute(httppost);
                   Log.d("Http Response:", response.toString());
                   HttpEntity entity = response.getEntity(); 
                   InputStream is = entity.getContent(); // Create an InputStream with the response
                   BufferedReader reader = new BufferedReader(new InputStreamReader(is, "windows-1251"));
                   StringBuilder sb = new StringBuilder();
                   String line = null;
                   while ((line = reader.readLine()) != null) // Read line by line
                       sb.append(line + "\n");

                   String resString = sb.toString(); // Result is here
                   Log.d("Http Response:", resString.toString());
                   System.out.println(resString.toString());
                   is.close(); // Close the stream
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //display message if text field is empty
            //Toast.makeText(getBaseContext(),"All fields are required",Toast.LENGTH_SHORT).show();
        }
    }	
}
