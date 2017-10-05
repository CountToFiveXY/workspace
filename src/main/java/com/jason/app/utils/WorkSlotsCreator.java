package com.jason.app.utils;

import com.jason.app.objects.Person;
import com.jason.app.time.WorkSlot;
import com.jason.app.time.WorkSlotContainer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  这个类的建立主要是为了方便Handler处理数据，毕竟处理数据和建立数据，我感觉还是分开比较好。
 *  静态的属性也能保证数据从建立后就可以完整的放在那边给其他类调用
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
        int ROW = workSheet.length;
        int COLUMN = workSheet[0].length;
        if (ROW != 13 || COLUMN != 15 || !workSheet[0][0].equals("X")) {
            System.out.println("FileHandler传入的表格数据有误");
            return;
        }

        for (int j = 1; j < COLUMN ; j = j + 2) {
            String dateInCalender = Tools.correctDate(workSheet[0][j]);
            String dateOfWeek = workSheet[1][j];
            System.out.println("---->扫描至 "+dateInCalender+" 并且创建当日的Container");
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
                //Handle Date Line Case and empty line Case
                if (personName.equals("X") || personName.isEmpty() || personName == null ) {
                    continue;
                }
                for(int k = j; k < j + 2; k ++){
                    String workSlotString = workSheet[i][k];
                    if (workSlotString.equals("X") || workSlotString.isEmpty() || workSlotString == null) {
                        continue;
                    }
                    WorkSlot workSlot = new WorkSlot(workSlotString);
                    workSlot.assignTo(person);
                    workSlotsForThisDate.add(workSlot);
                    WorkSlotsSum++;
                    System.out.println("班次 "+workSlotString+" is fetched for "+personName+", 当前班次个数为:"+ workSlotsForThisDate.size());
                }
            }
            WorkSlotContainer workSlotContainer = new WorkSlotContainer(dateInCalender, dateOfWeek, workSlotsForThisDate);
            if (workSlotsForThisDate.size() == 0) {
            	System.out.println("该日为节假日，没有任何班次.\n");
            }else{
                System.out.println("---->"+dateInCalender+" ("+dateOfWeek+")的Container已创建, 包含班次个数为:"+workSlotsForThisDate.size()+"-----\n");
            }
            if (!AllWorkSlotContainersList.contains(workSlotContainer)){
            	AllWorkSlotContainersList.add(workSlotContainer);
            }           
        }    
    }
}
