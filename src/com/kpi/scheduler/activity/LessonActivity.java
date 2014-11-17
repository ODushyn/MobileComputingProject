package com.kpi.scheduler.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.kpi.scheduler.tools.Constants;

public class LessonActivity extends Activity {
	
	private int year;
	private int month;
	private int week; // 1st or 2nd
	private String until;
	private Map<Integer, LessonTime> lessonsTime;
	private TimeZone tz;
	
	public void save(final Parcelable[] lessons){
		//((List<Lesson>)getIntent().getExtras().getSerializable("lessons")).toArray();
		new AsyncTask<Parcelable, Void, String>(){

			@Override
			protected String doInBackground(Parcelable... lessons) {
				//TODO change second parameter - we shall provide to user chance to choose a calander account
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
	

	private void saveLesson(Lesson lesson, long calID){
		
		for(Entry<Integer, LessonTime> entry : lessonsTime.entrySet()){
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		
		int lessonStartDay =  startDay(lesson.getDay(), lesson.getWeekOccur());
		System.out.println("start day: " + lessonStartDay);
		
		Calendar beginTime = Calendar.getInstance();
		int beginH = lessonsTime.get(lesson.getNumber()).start_h;
		int beginM = lessonsTime.get(lesson.getNumber()).start_m;
		beginTime.set(year, month, lessonStartDay, beginH, beginM);
		long startMillis = beginTime.getTimeInMillis();
		
		Calendar endTime = Calendar.getInstance();
		int endH = lessonsTime.get(lesson.getNumber()).finish_h;
		int endM = lessonsTime.get(lesson.getNumber()).finish_m;
		System.out.println(endH + " " + endM);
		System.out.println(lesson.getDay());
		System.out.println(year + " " + month);
		endTime.set(year, month, lessonStartDay, endH, endM);
		long endMillis = endTime.getTimeInMillis();

		int interval = countLessonInterval(lesson);
		
		ContentResolver cr = getContentResolver();
		ContentValues values = new ContentValues();
		values.put(Events.DTSTART, startMillis);
		values.put(Events.DTEND, endMillis);
		values.put(Events.TITLE, lesson.getName());
		values.put(Events.DESCRIPTION, lesson.getPlace());
		values.put(Events.CALENDAR_ID, calID);
		values.put(Events.EVENT_TIMEZONE, tz.getID());
		values.put(Events.RRULE, "FREQ=WEEKLY;INTERVAL="+interval+";UNTIL="+this.until+";WKST=SU");
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
	    int lNum = 1;
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
	
	private int startDay(int dayInWeek, int occurWeek){
		Calendar cal = Calendar.getInstance();

		if(occurWeek == Constants.OCCUR_EVERY_WEEK){
			cal.set(Calendar.DAY_OF_WEEK, dayInWeek);
			return cal.get(Calendar.DAY_OF_MONTH);
		}
		if(occurWeek == this.week){
			cal.set(Calendar.DAY_OF_WEEK, dayInWeek);
			System.out.println(cal.get(Calendar.DAY_OF_MONTH));
			return cal.get(Calendar.DAY_OF_MONTH);
		}
		cal.add(Calendar.DATE,7);
		cal.set(Calendar.DAY_OF_WEEK, dayInWeek);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	private int countLessonInterval(Lesson lesson){
		if(lesson.getWeekOccur() == Constants.OCCUR_EVERY_WEEK){
			return 1;
		}
		
		return 2;
	}
	
	private String semesterEnd(){
		 // 1 - 1st semester, 2 - 2nd semester
		if(getIntent().getIntExtra("semester", 1) == Constants.FIRST_SEMESTER){
			return String.valueOf(this.year+1) + getString(R.string.first_semester_end) + "T000000Z";
		}
		return String.valueOf(this.year) + getString(R.string.second_semester_end) + "T000000Z";
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		//initialization
		this.year = Calendar.getInstance().get(Calendar.YEAR);
		this.month = Calendar.getInstance().get(Calendar.MONTH);
		this.week = getIntent().getIntExtra("week", -1);
		this.lessonsTime = lessonsTime(R.array.lessons_time);
		this.tz = TimeZone.getDefault();
		this.until = semesterEnd();
		System.out.println(until);
		System.out.println(tz.getDisplayName());
		//saving
		save(getIntent().getParcelableArrayExtra("lessons"));
	}
	
}
