package com.jason.app.time.tests;

import com.jason.app.time.WorkSlot;
import com.jason.app.utils.Tools;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;

public class WorkSlotTest {

    @Test
    public void testGetTime () {
        String testString = "12-6";
        WorkSlot workSlot = new WorkSlot(testString);
        Assert.assertEquals("6",workSlot.getToTime());
    }
}
