package com.jason.app.time;
import java.util.List;

import lombok.Getter;

//given a date, you can extract all workslots for that day.
@Getter
public class WorkSlotContainer {
	private String date;
    private List<WorkSlot> workSlotList;
    public WorkSlotContainer(String date, List<WorkSlot> workSlotList ) {
        this.date = date;
        this.workSlotList = workSlotList;
    }
}
