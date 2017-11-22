package com.jason.app.utils;

import com.jason.app.objects.Person;
import com.jason.app.time.WorkSlot;
import com.jason.app.time.WorkSlotContainer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  这个类的建立是为了方便Handler处理数据
 */
@Getter
public class WorkSlotsCreator {

    List<WorkSlotContainer> AllWorkSlotContainersList = new ArrayList<>();
    int WorkSlotsSum = 0;
    HashMap<String, Person> personMap = new HashMap<>();

    /**
     *  这个Method会把所有表格数据整理成一张日期对应WorkSlots的大表以便提取两个日期之间的所有工作时间。
     */
    public void createWorkSheetData (String[][] workSheet) {
        if (workSheet[0] == null || workSheet == null || workSheet[0][0] == null) {
            return;
        }
        int ROW = workSheet.length;
        int COLUMN = workSheet[0].length;
        if (ROW != 15 || COLUMN != 15 || !workSheet[0][0].equals("X")) {
            System.out.println("FileHandler传入的表格数据有误\n");
            return;
        }

        for (int j = 1; j < COLUMN ; j = j + 2) {
            String dateInCalender = Tools.correctDate(workSheet[0][j]);
            if (!dateInCalender.startsWith("2")) {
                continue;
            }
            String dateOfWeek = workSheet[1][j];
            System.out.println("-->扫描至 "+dateInCalender+" 并且创建该日的Container");
            List<WorkSlot> workSlotsForThisDate = new ArrayList<>();
            //因为前两行都是Date
            for( int i = 2; i < ROW; i ++) {
                String personName = workSheet[i][0];
                if (personName == null || personName.isEmpty()) {
                    System.out.println("扫描结束, 创建Container中...");
                    break;
                }
                Person person = new Person(personName);
                personMap.put(personName, person);
                for(int k = j; k < j + 2; k ++){
                    String workSlotString = workSheet[i][k];
                    if (workSlotString.equals("X") || workSlotString.isEmpty() || workSlotString == null) {
                        continue;
                    }
                    WorkSlot workSlot = new WorkSlot(workSlotString);
                    workSlot.assignTo(person);
                    workSlotsForThisDate.add(workSlot);
                    WorkSlotsSum++;
                    System.out.println("班次 "+workSlotString+" is fetched for "+personName+", 此日的当前班次个数为:"+ workSlotsForThisDate.size());
                }
            }
            WorkSlotContainer workSlotContainer = new WorkSlotContainer(dateInCalender, dateOfWeek, workSlotsForThisDate);
            if (workSlotsForThisDate.size() == 0) {
            	System.out.println("-->"+dateInCalender+"日没有任何班次.\n");
            }else{
                System.out.println("-->"+dateInCalender+"("+dateOfWeek+")的Container已创建, 包含班次个数为:"+workSlotsForThisDate.size()+"-----\n");
            }
            boolean isContained = false;
            for(WorkSlotContainer container: AllWorkSlotContainersList) {
                String date = container.getDateOfCalender();
                if (date.equals(workSlotContainer.getDateOfCalender())) {
                    isContained = true;
                }
            }
            if (!isContained) {
                AllWorkSlotContainersList.add(workSlotContainer);
            }
        }    
    }
}
