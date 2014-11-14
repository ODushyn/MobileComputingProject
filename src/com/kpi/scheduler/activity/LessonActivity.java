package com.kpi.scheduler.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.CalendarContract.Events;

import com.kpi.scheduler.R;
import com.kpi.scheduler.model.Lesson;

public class LessonActivity extends Activity {
	
	private int year;
	private int month;
	private Map<Integer, LessonTime> lessonsTime;
	private TimeZone tz;
	
	public void save(final Parcelable[] lessons){
		//((List<Lesson>)getIntent().getExtras().getSerializable("lessons")).toArray();
		new AsyncTask<Parcelable, Void, String>(){

			@Override
			protected String doInBackground(Parcelable... lessons) {
				//TODO change second parameter
				for(Parcelable pl : lessons){
					Lesson lesson = (Lesson)pl;
					saveLesson(lesson, 6);
				}
				return "URAA";
			}
			
		    @Override
		    protected void onPostExecute(String s) {
		        super.onPostExecute(s);
		    }
			//(Lesson[]) lessons.toArray()
		}.execute(lessons);	
	}
	

	//TODO change calId
	private void saveLesson(Lesson lesson, long calID){
		
		for(Entry<Integer, LessonTime> entry : lessonsTime.entrySet()){
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		
		//TODO: add repetition for lessons - use field week! 		
		Calendar beginTime = Calendar.getInstance();
		int beginH = lessonsTime.get(lesson.getNumber()).start_h;
		int beginM = lessonsTime.get(lesson.getNumber()).start_m;
		beginTime.set(year, month, lesson.getDay(), beginH, beginM);
		long startMillis = beginTime.getTimeInMillis();
		
		Calendar endTime = Calendar.getInstance();
		int endH = lessonsTime.get(lesson.getNumber()).finish_h;
		int endM = lessonsTime.get(lesson.getNumber()).finish_m;
		System.out.println(endH + " " + endM);
		System.out.println(lesson.getDay());
		System.out.println(year + " " + month);
		endTime.set(year, month, lesson.getDay(), endH, endM);
		long endMillis = endTime.getTimeInMillis();

		ContentResolver cr = getContentResolver();
		ContentValues values = new ContentValues();
		values.put(Events.DTSTART, startMillis);
		values.put(Events.DTEND, endMillis);
		values.put(Events.TITLE, lesson.getName());
		values.put(Events.DESCRIPTION, lesson.getPlace());
		values.put(Events.CALENDAR_ID, calID);
		values.put(Events.EVENT_TIMEZONE, tz.getID());
		cr.insert(Events.CONTENT_URI, values);

		
	}
	
	class LessonTime{
		int start_h;
		int start_m;
		int finish_h;
		int finish_m;
		
		public LessonTime(int start_h, int start_m, int finish_h, int finish_m) {
			this.start_h = start_h;
			this.start_m = start_m;
			this.finish_h = finish_h;
			this.finish_m = finish_m;
		}

		@Override
		public String toString() {
			return "LessonTime [start_h=" + start_h + ", start_m=" + start_m
					+ ", finish_h=" + finish_h + ", finish_m=" + finish_m + "]";
		}
		
	}
	
	private Map<Integer, LessonTime> lessonsTime(int lessonTimeResourceId) {
		
		Map<Integer, LessonTime> lessTimeMap = new HashMap<Integer, LessonTime>();
		
		List<LessonTime> lTimeArray = parseLessonTime(lessonTimeResourceId);	    
	    int lNum = 0;
	    for (LessonTime lt : lTimeArray) {
	        lessTimeMap.put(lNum, lt);
	        lNum++;
	    }
	    
	    return lessTimeMap;
	}
	
	private List<LessonTime> parseLessonTime(int lessonTimeResourceId){
		
		List<LessonTime> lessTimeArray = new ArrayList<LessonTime>();
		String[] lessTime = getResources().getStringArray(lessonTimeResourceId);
	    for (String time : lessTime) {
	    	String[] timePeriod = time.split(" ", 2);
	    	String beginTime = timePeriod[0]; 
	    	String finishTime = timePeriod[1];
	        int start_h = Integer.valueOf(beginTime.split(":", 2)[0]);
			int start_m = Integer.valueOf(beginTime.split(":", 2)[1]);
			int finish_h = Integer.valueOf(finishTime.split(":", 2)[0]);
			int finish_m = Integer.valueOf(finishTime.split(":", 2)[1]);
			lessTimeArray.add(new LessonTime(start_h, start_m, finish_h, finish_m));
	    }	    
	    return lessTimeArray;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		//initialization
		this.year = Calendar.getInstance().get(Calendar.YEAR);
		this.month = Calendar.getInstance().get(Calendar.MONTH);
		this.lessonsTime = lessonsTime(R.array.lessons_time);
		this.tz = TimeZone.getDefault();
		System.out.println(tz.getDisplayName());
		//saving
		save(getIntent().getParcelableArrayExtra("lessons"));
	}
	
}
