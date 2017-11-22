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
	
	public double getWorkTime(){
		double fromtime;
		double totime;
		if(fromTime.length() > 2) {
			String[] fromTimeArray = Tools.convertStringToTime(fromTime);
			int hour  = Tools.convertStringToInteger(fromTimeArray[0]);
			double minutes = Tools.convertStringToInteger(fromTimeArray[1])/60.0;
			fromtime = hour + minutes;
		} else {
			fromtime = (double)Tools.convertStringToInteger(fromTime);
		}
		if(toTime.length() > 2) {
			String[] toTimeArray = Tools.convertStringToTime(toTime);
			int hour  = Tools.convertStringToInteger(toTimeArray[0]);
			double minutes = Tools.convertStringToInteger(toTimeArray[1])/60.0;
			totime = hour + minutes;
		} else {
			totime = (double) Tools.convertStringToInteger(toTime);
		}

		double workTime = totime - fromtime;
		//假设一个工作时间段不会少于3小时
		return workTime > 2.5 ? workTime : workTime + 12;
	}
	
	public void assignTo(Person person){
		assignee = person;
	}
}
