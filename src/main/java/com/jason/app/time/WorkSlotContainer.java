package com.jason.app.time;
import java.util.List;

import lombok.Getter;

/**
 * 这个类是用来装日期和对应的所有班次的
 * 其实HashMap 也挺好用，个人喜好问题。
 */
@Getter
public class WorkSlotContainer {
	private String dateOfCalender;
	private String dateOfWork;
    private List<WorkSlot> workSlotList;

    public WorkSlotContainer(String dateOfCalender, String dateOfWork, List<WorkSlot> workSlotList ) {
        this.dateOfCalender = dateOfCalender;
        this.dateOfWork = dateOfWork;
        this.workSlotList = workSlotList;
    }
}
