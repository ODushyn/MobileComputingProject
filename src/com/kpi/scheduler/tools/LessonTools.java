package com.kpi.scheduler.tools;

import java.util.List;

import com.kpi.scheduler.model.Lesson;

public class LessonTools{

	public static Lesson[] convertListToArray(List<Lesson> list){
		final Lesson[] lessons = new Lesson[list.size()];
		
		for(int i = 0; i < lessons.length; i++){
      	  System.out.println(list.get(i).getName());
      	  lessons[i] = list.get(i);
        }
		
		return lessons;
	}

}
