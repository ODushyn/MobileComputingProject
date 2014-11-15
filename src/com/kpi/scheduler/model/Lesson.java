package com.kpi.scheduler.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Lesson implements Parcelable{
	
	/**
	 *  lesson name
	 */
	private String name;
	
	/**
	 * address of auditory
	 */
	private String place;
	
	/**
	 * lesson number in day lessons order (1,2,...,6) 
	 */
	private int number;
	
	/**
	 * day number (1-Sunday, 2-Monday, ..., 7. Saturday)
	 */
	private int day;
	
	/**
	 * week number (1 or 2, 0 if lesson appear every day)
	 */
	private int weekOccur;
	
	public Lesson(Parcel source){
		this.name = source.readString();
		this.place = source.readString();
		this.number = source.readInt();
		this.day = source.readInt();
		this.weekOccur = source.readInt();
	}
	
	public Lesson(String name, String place, int number, int day, int week) {
		this.name = name;
		this.place = place;
		this.number = number;
		this.day = day;
		this.weekOccur = week;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getWeekOccur() {
		return weekOccur;
	}

	public void setWeekOccur(int week) {
		this.weekOccur = week;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeString(this.place);
		dest.writeInt(this.number);
		dest.writeInt(this.day);
		dest.writeInt(this.weekOccur);
	}
	
	public final static Parcelable.Creator<Lesson> CREATOR = new Parcelable.Creator<Lesson>() { 
       
		@Override 
        public Lesson createFromParcel(Parcel source) { 
            return new Lesson(source); 
        } 

        @Override 
        public Lesson[] newArray(int size) { 
            return new Lesson[size]; 
        } 
    }; 
	

}
