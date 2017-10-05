package com.jason.app.time;
import com.jason.app.objects.Person;
import com.jason.app.utils.FileHandler;
import com.jason.app.utils.WorkSlotsCreator;

import java.util.HashMap;
import java.util.HashSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WorkSlotsHandler {
    List<WorkSlotContainer> workSlotContainersList;
    List<WorkSlotContainer> ContainersBetweenTwoDays;
    HashMap<String, Person> personMap;
    HashMap<String, Double> salaryMap = new HashMap<>();
    HashSet<Person> fullTimePerson;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();
    
    public void init(WorkSlotsCreator workSlotsCreator, FileHandler fileHandler, String fromDate, String toDate) throws ParseException {
    	workSlotContainersList = workSlotsCreator.getAllWorkSlotContainersList();
		personMap = workSlotsCreator.getPersonMap();
		salaryMap = fileHandler.getSalaryMap();
		System.out.println("\n将参数传递至Handler...\n");
		ContainersBetweenTwoDays = getAllWorkContainerBetweenTwoDays(fromDate, toDate);
    }
    
    public void findResult(String personName) throws ParseException {   	
    	List<WorkSlot> workSlotsForThiPerson = findWorkSlotsForThisPerson(personName, ContainersBetweenTwoDays);
    	calculateSalary(workSlotsForThiPerson);
    }
    
    public List<WorkSlotContainer> getAllWorkContainerBetweenTwoDays (String fromDateString, String toDateString) {
    	List<WorkSlotContainer> allWorkContainersBetweenTwoDays= new ArrayList<>();
    	Date fromDate = null;
    	Date toDate = null;
		try {
			fromDate = dateFormat.parse(fromDateString);
			toDate = dateFormat.parse(toDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("[Error]: 程序崩溃，因为输入日期格式有毒，请重启程序并输入正确的参数\n");
		}
    	
    	Date date = fromDate;
    	while(!date.after(toDate)) {
    		String dateString = dateFormat.format(date);
    		//Fetching Container For Day
    		for (WorkSlotContainer workSlotContainer :  workSlotContainersList) {
    			if (workSlotContainer.getDateOfCalender().equals(dateString)) {
    				allWorkContainersBetweenTwoDays.add(workSlotContainer);
				}
    		}
    		calendar.setTime(date);
    		calendar.add(Calendar.DATE, 1);
    		date = calendar.getTime();
    	}
    	System.out.println("[Complete]: Containers扫描完毕, 共找到"+allWorkContainersBetweenTwoDays.size()+"个Containers.\n");
    	return allWorkContainersBetweenTwoDays;
    }
    
    public List<WorkSlot> findWorkSlotsForThisPerson(String name, List<WorkSlotContainer> containerList) {
    	if (personMap.isEmpty()) {
    		System.out.println("[Error]: map is not established");
    		return null;
    	}
    	if (name == null || !personMap.keySet().contains(name)) {
    		System.out.println("[Error]:"+ name +"这个人我不认识，请确认名字无误并重试!\n");
    		return null;
    	}
    	System.out.println("开始查询"+name+"的班次:");
    	Person person = personMap.get(name);
    	List<WorkSlot> workSlots = new ArrayList<>();
    	for (WorkSlotContainer container : containerList) {
    		String date = container.getDateOfCalender();
    		String week = container.getDateOfWork();
    		List<WorkSlot> workSlotList = container.getWorkSlotList();
    		int count = 0;
    		for (WorkSlot workSlot : workSlotList) {
    			if (workSlot.getAssignee().isSame(person)) {
    				count ++;
    				workSlot.setDate(date+"("+week+")");
    				workSlots.add(workSlot);
    			}
    		}
    		if (count != 0){
    			System.out.println("在"+ date +"("+ week +")发现"+count+"个班次.");
    		}
    	}
        int num = workSlots.size();
    	System.out.println("[Complete]: "+person.getName()+"这段时间内的班次总数为"+num);
    	return workSlots;
    }
    
    public double calculateSalary (List<WorkSlot> workSlotsList) { 	
    	if (workSlotsList == null) {
    		System.out.println("程序已终止，请重试\n");
    		return 0.0;
    	}
    	if (workSlotsList.isEmpty()) {
    		System.out.println("\n这个人这段时间内没有上班.");
    		return 0.0;
    	}
    	System.out.println("\n开始计算总时长:");
    	Person person = workSlotsList.get(0).getAssignee();
    	double salary = findSalaryForThisPerson(person.getName());
    	int hours = 0;
    	for (WorkSlot workSlot : workSlotsList) {
    		String date = workSlot.getDate();
    		String from = workSlot.getFromTime();
    		String to = workSlot.getToTime();
    		int workhour = workSlot.getWorkTime();
    		hours += workhour;
    		System.out.format("%s, 班时: %s-%s, +%d小时, 总时长: %d.\n", date,from,to,workhour,hours); 		  		
    	}
    	double sum = (double) salary*hours;
    	System.out.format("该时段总工资为: \n%.2f X %d = %.2f刀.\n\n",salary,hours,sum);
    	return sum;
    }
    public double findSalaryForThisPerson (String Name) {
		if (!salaryMap.containsKey(Name)) {
			System.out.println("[Error]: Can't find salary for "+Name);
		}
		return salaryMap.get(Name);		
	}
}

