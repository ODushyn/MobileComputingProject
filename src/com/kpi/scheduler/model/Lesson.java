package com.kpi.scheduler.model;

public class Lesson {
	
	/**
	 *  lesson name
	 */
	private String name;
	
	/**
	 * address of auditory
	 */
	private String place;
	
	/**
	 * lesson number in day lessons order 
	 */
	private int number;
	
	/**
	 * day number (1-Sunday, 2-Monday, ..., 7. Saturday)
	 */
	private int day;
	
	/**
	 * week number (1 or 2)
	 */
	private int week;

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

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}
	

}
