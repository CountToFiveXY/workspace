package com.jason.app.time;
import java.util.List;

import lombok.Getter;

/**
 *  这个类是用来装日期和对应的所有WorkSlots的
 *  有人可能要问为什么不用HashMap?
 *  好吧，HashMap 也挺好用。个人喜好，而且Creator里已经有一个Map装人物和WorkSlot的关系了
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
