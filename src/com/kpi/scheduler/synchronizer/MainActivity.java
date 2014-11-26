package com.kpi.scheduler.synchronizer;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.kpi.scheduler.R;
import com.kpi.scheduler.activity.LessonActivity;
import com.kpi.scheduler.model.Lesson;
import com.kpi.scheduler.tools.JsonParser;
import com.kpi.scheduler.tools.JsonRetriever;
import com.kpi.scheduler.tools.LessonTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
		
	public static Button syncButton;
	public static EditText groupNo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		syncButton = (Button) findViewById(R.id.syncButton);
		groupNo = (EditText) findViewById(R.id.groupNo);
		
		//Mock data
//		final Lesson lesson = new Lesson("Test", "¿Û‰. 74-15", 5, 3, 1);
//		final Lesson[] lessons = new Lesson[1];
//		lessons[0] = lesson;

		syncButton.setOnClickListener(new View.OnClickListener() {
		      @Override
		      public void onClick(View v) {		        
		    	  try {
			    	  JsonRetriever retriever = new JsonRetriever();
			    	  
			    	  retriever.execute(new String[]{groupNo.getText().toString()}).get();
			    	  
			          String jsonString = retriever.getJsonResult();
			          List<Lesson> lessList = JsonParser.parseScheduleInJson(jsonString);
			          final Lesson[] lessons = LessonTools.convertListToArray(lessList);
			          
			    	  Intent intent = new Intent(getBaseContext(), LessonActivity.class);
			    	  intent.putExtra("lessons", lessons);
			    	  intent.putExtra("semester", 1);
			    	  intent.putExtra("week", 1);		    	  
			    	  startActivity(intent);
			    	  
		    	  } catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}	
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
	
	//TODO: 1. Choosing android account
	//	2. Current week determine
	// 3. Change week to only 1 or 2. Coud not be 0 - occure every week (due to rozklad.kpi)
	
}
