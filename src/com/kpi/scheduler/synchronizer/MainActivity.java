package com.kpi.scheduler.synchronizer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	protected static final String URL = "http://www.fpm.ntu-kpi.kiev.ua/scheduler/group.do";
	protected static final String URL2 = "http://rozklad.kpi.ua/Schedules/ViewSchedule.aspx?g=d5992d1b-4dcf-41e5-8517-5516c4f8868d";
	public static Button syncButton;
	public static EditText tempText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		syncButton = (Button) findViewById(R.id.syncButton);
		tempText = (EditText) findViewById(R.id.outputText);
		
		syncButton.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {		        
		    	  String siteUrl = URL;
		    	  HTMLRetriever retriever = new HTMLRetriever();
		          retriever.execute(new String[]{siteUrl});		       
		      }  
		});
		
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
