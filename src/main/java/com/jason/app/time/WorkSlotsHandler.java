package com.jason.app.time;
import com.jason.app.objects.Person;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;

public class WorkSlotsHandler {
    List<WorkSlotContainer> workSlotContainersList;
    HashMap<Person, List<WorkSlot>> MapPersonToHisWorkSlots;
    HashSet<Person> fullTimePerson;

    public WorkSlotsHandler () {
        workSlotContainersList = new ArrayList<WorkSlotContainer>();
        this.MapPersonToHisWorkSlots = new HashMap<>();
        this.fullTimePerson = new HashSet<>();
    }

    public void handleWorkSlotContainer (WorkSlotContainer workSlotContainer) {
        workSlotContainersList.add(workSlotContainer);
    }

    public List<WorkSlot> fetchWorkSlotForThisDay (String date){
        List<WorkSlot> targetWorkSlots = new ArrayList<>();
        for(WorkSlotContainer workSlotContainer : workSlotContainersList) {
            if (workSlotContainer.getDate().equals(date)) {
                targetWorkSlots = workSlotContainer.getWorkSlotList();
            }
        }
        return targetWorkSlots;
    }

    public List<WorkSlot> findWorkSlotsForThisPerson(String name, List<WorkSlot> workSlotsList) {
        //need to be done
        return null;
    }

    public void fetchWorkslotsForThisPerson(Person person) {
        if (person == null) {
            System.out.println("I don't know this guy");
        }
        if (fullTimePerson.contains(person)) {
            System.out.println("This Person is full time, you don't need to get hourly pay info");
        }
        //need to be done
        return;
    }
}

