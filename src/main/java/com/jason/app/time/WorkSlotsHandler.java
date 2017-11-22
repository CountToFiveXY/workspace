package com.jason.app.time;
import com.jason.app.objects.Person;
import com.jason.app.utils.FileHandler;
import com.jason.app.utils.WorkSlotsCreator;

import java.util.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/*
** 这个类可以认为是班次的处理器，先把班次的容器筛选集中，再从容器里根据条件获取班次，处理后以统一格式呈现给用户
*/
public class WorkSlotsHandler {

    List<WorkSlotContainer> workSlotContainersList;
    List<WorkSlotContainer> ContainersBetweenTwoDays;
    HashMap<String, Person> personMap;
    HashMap<String, Double> salaryMap = new HashMap<>();
    //先建着，估计以后有用//
    HashSet<Person> fullTimePerson;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();
    
    public void init(WorkSlotsCreator workSlotsCreator, FileHandler fileHandler, String fromDate, String toDate) throws ParseException {
    	workSlotContainersList = workSlotsCreator.getAllWorkSlotContainersList();
		personMap = workSlotsCreator.getPersonMap();
		salaryMap = fileHandler.getSalaryMap();
		ContainersBetweenTwoDays = getAllWorkContainerBetweenTwoDays(fromDate, toDate);
    }
    
    public String findResult(String personName) throws ParseException {
    	List<WorkSlot> workSlotsForThiPerson = findWorkSlotsForThisPerson(personName, ContainersBetweenTwoDays);
    	return calculateSalary(workSlotsForThiPerson);
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
			System.out.println("[Error]: 输入日期格式有毒，请输入正确的日期😅\n");
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
    		System.out.println("[Error]: Person map is not established");
    		return null;
    	}
    	if (name == null || !personMap.keySet().contains(name)) {
    		System.out.println("[Error]:" + name + "这个人不在查询的班表里，请确认名字无误或更换班表!🌚\n");
    		return null;
    	}
    	System.out.println("-->开始查询" + name + "的班次:");
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
    	System.out.println("[Complete]: "+person.getName()+"这段时间内的班次总数为"+num+"\n");
    	return workSlots;
    }
    
    public String calculateSalary (List<WorkSlot> workSlotsList) {
		StringBuilder logs = new StringBuilder();
    	if (workSlotsList == null) {
    		logs.append("程序已终止，请重试.🤤@========================");
    		return logs.toString();
    	}
    	if (workSlotsList.isEmpty()) {
			logs.append("根据输入的表格，这个人这段时间内傻啦吧唧的没有上班.😴@========================");
    		return logs.toString();
    	}
    	//开始录入工资LOG
    	logs.append("开始计算总时长: @");
    	Person person = workSlotsList.get(0).getAssignee();
    	double salary = findSalaryForThisPerson(person.getName());
    	if (salary == 0.0) {
			return "似乎没找到这个人的工资😟, 请检查终端信息.@========================";
		}
		double hours = 0.0;
		for (WorkSlot workSlot : workSlotsList) {
			String date = workSlot.getDate();
			String from = workSlot.getFromTime();
			String to = workSlot.getToTime();
			double workHour = workSlot.getWorkTime();
			double pre = hours;
			hours += workHour;
			logs.append(String.format("%s, 班时: %s-%s, 总时长: %.2f + %.2f = %.2f小时.@", date,from,to,pre,workHour,hours));
		}
		double sum = salary * hours;
		logs.append(String.format("该时段总工资为: \n%.2f($/h) X %.2f(小时) = %.2f刀.@",salary,hours,sum));
		//彩蛋
		if (sum > 500.00)
			logs.append("啥都不说了土豪带我飞呀~😍");
		else if (sum > 330.00)
			logs.append("嗯不错你上班时间及格了.👏");
		else
			logs.append("你个穷逼，还不赶紧干活挣钱.🌚");
		logs.append("@========================");
    	return logs.toString();
    }

    public double findSalaryForThisPerson (String Name) {
    	System.out.println("-->查询"+ Name +"的工资记录");
		if (!salaryMap.containsKey(Name)) {
			System.out.println("\n[Error]: 无法找到"+Name+"的工资记录，请更新Salary.txt😮");
			return 0.0;
		}
		System.out.println("[Done!]:已找到此人的工资记录，请去工资日志下查看🌞.\n");
		return salaryMap.get(Name);		
	}
}

