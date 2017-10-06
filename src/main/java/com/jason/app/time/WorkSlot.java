package com.jason.app.time;

import java.util.Date;

import com.jason.app.objects.Person;
import com.jason.app.utils.Tools;

import lombok.Getter;
import lombok.Setter;

/*
** 班次类，没啥好说的
*/
@Getter
@Setter
public class WorkSlot {
    private String fromTime;
	private String toTime;
	public Person assignee;
	public String Date;
	
	public WorkSlot(String timeString){
		String[] time = Tools.convertStringToArray(timeString);
		this.fromTime = time[0];
		this.toTime = time[1];
		this.assignee = null;
		this.Date = null;
	}
	
	public int getWorkTime(){
		int workTime = Tools.convertStringToInteger(toTime) - Tools.convertStringToInteger(fromTime);
		return workTime > 0 ? workTime : workTime + 12;
	}
	
	public void assignTo(Person person){
		assignee = person;
	}
}
