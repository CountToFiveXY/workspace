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
	private WorkType workType; 
	public Person assignee;
	
	public WorkSlot(String timeString, WorkType workType){
		this.fromTime = getTime(timeString)[0];
		this.toTime = getTime(timeString)[1];
		this.workType = workType;
		this.assignee = null;		
	}
	
	private String[] getTime(String timeString){
		String[] result = timeString.split("-");
		return result;
	}
	
	public int getWorkTime(){
		int workTime = Tools.convertStringToInteger(toTime) - Tools.convertStringToInteger(fromTime);
		return workTime > 0 ? workTime : workTime + 12;
	}
	
	public void assignTo(Person person){
		assignee = person;
	}
}
