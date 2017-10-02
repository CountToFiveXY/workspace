package com.jason.app.time;
import com.jason.app.objects.Person;
import com.jason.app.utils.WorkSlotsCreator;

import java.util.HashMap;
import java.util.HashSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.Setter;

public class WorkSlotsHandler {
    @Setter List<WorkSlotContainer> workSlotContainersList;
    @Setter HashMap<String, Person> personMap;
    HashSet<Person> fullTimePerson;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();
    
    public void init(WorkSlotsCreator workSlotsCreator) {
    	workSlotContainersList = workSlotsCreator.getAllWorkSlotContainersList();
		personMap = workSlotsCreator.getPersonMap();
		System.out.println("Passing attributes To Handler......\n");
    }
    
    public void findResult(String fromDate, String toDate, String personName) throws ParseException {
    	List<WorkSlotContainer> workSlotsContainerList = getAllWorkContainerBetweenTwoDays(fromDate, toDate);
    	List<WorkSlot> workSlotsForThiPerson = findWorkSlotsForThisPerson(personName, workSlotsContainerList);
    	calculateSalary(workSlotsForThiPerson);
    }
    
    public List<WorkSlotContainer> getAllWorkContainerBetweenTwoDays (String fromDateString, String toDateString) throws ParseException {
    	System.out.format("4.Starting fetching WorkSlots between %s and %s.\n",fromDateString,toDateString);
    	List<WorkSlotContainer> allWorkContainersBetweenTwoDays= new ArrayList<>();
    	Date fromDate = dateFormat.parse(fromDateString);
    	Date toDate = dateFormat.parse(toDateString);
    	Date date = fromDate;
    	while(!date.after(toDate)) {
    		int count = 0;
    		String dateString = dateFormat.format(date);
    		//Fetching Container For Day
    		for (WorkSlotContainer workSlotContainer :  workSlotContainersList) {
    			if (workSlotContainer.getDateOfCalender().equals(dateString)) {
    				count ++;
    				allWorkContainersBetweenTwoDays.add(workSlotContainer);
				}
    		}
    		calendar.setTime(date);
    		calendar.add(Calendar.DATE, 1);
    		date = calendar.getTime();
    	}
    	System.out.format("\n***** WorkContainers Fetching Complete, total %d containers fetched*****\n",allWorkContainersBetweenTwoDays.size());
    	return allWorkContainersBetweenTwoDays;
    }
    
    public List<WorkSlot> findWorkSlotsForThisPerson(String name, List<WorkSlotContainer> containerList) {
    	if (personMap.isEmpty()) {
    		System.out.println("map is not established");
    	}
    	if (name == null || !personMap.keySet().contains(name)) {
    		System.out.println("I dont know this guy!");
    	}
    	System.out.println("\nStarting fetching workSlots for person: "+name);
    	Person person = personMap.get(name);
    	List<WorkSlot> workSlots = new ArrayList<>();
    	for (WorkSlotContainer container : containerList) {
    		String date = container.getDateOfCalender();
    		String week = container.getDateOfWork();
    		List<WorkSlot> workSlotList = container.getWorkSlotList();
    		int count = 0;
    		for(WorkSlot workSlot : workSlotList) {
    			if (workSlot.getAssignee().isSame(person)) {
    				count ++;
    				workSlot.setDate(date+"("+week+")");
    				workSlots.add(workSlot);
    			}
    		}
    		System.out.println("found "+count+" workslot for person in "+ date +"("+ week +")");
    	}
        int num = workSlots.size();
    	System.out.println("(Action COMPLETE) All WorkSlots found for "+person.getName()+", the total number is "+num);
    	return workSlots;
    }
    
    public double calculateSalary (List<WorkSlot> workSlotsList) { 	
    	if (workSlotsList.isEmpty()) {
    		System.out.println("Something Wrong fecthing workSLots for this one, please check");
    		return 0.0;
    	}
    	System.out.println("\n5.Starting Calculating Salary for this person");
    	Person person = workSlotsList.get(0).getAssignee();
    	double salary = person.getSalary();
    	int hours = 0;
    	for (WorkSlot workSlot : workSlotsList) {
    		String date = workSlot.getDate();
    		String from = workSlot.getFromTime();
    		String to = workSlot.getToTime();
    		int workhour = workSlot.getWorkTime();
    		hours += workhour;
    		System.out.format("In %s, working from %s to %s, total work hour is %d.\n", date,from,to,hours); 		  		
    	}
    	double sum = (double) salary*hours;
    	System.out.format("The Total salary is: %.2f X %d = %.3f.",salary,hours,sum);
    	return sum;
    }
}

