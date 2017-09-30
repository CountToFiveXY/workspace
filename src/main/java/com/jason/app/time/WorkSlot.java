package com.jason.app.time;

import com.jason.app.objects.Person;
import com.jason.app.utils.Tools;

import lombok.Getter;
import lombok.Setter; 

@Getter
@Setter
public class WorkSlot {
    private String fromTime;
	private String toTime;
	public Person assignee;
	
	public WorkSlot(String timeString){
		String[] time = Tools.convertStringToArray(timeString);
		this.fromTime = time[0];
		this.toTime = time[1];
		this.assignee = null;
	}
	
	public int getWorkTime(){
		int workTime = Tools.convertStringToInteger(toTime) - Tools.convertStringToInteger(fromTime);
		return workTime > 0 ? workTime : workTime + 12;
	}
	
	public void assignTo(Person person){
		assignee = person;
	}
}
