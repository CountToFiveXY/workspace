import com.jason.app.time.WorkSlot;
import com.jason.app.time.WorkSlotsHandler;
import com.jason.app.utils.FileHandler;
import com.jason.app.utils.Tools;
import com.jason.app.utils.WorkSlotsCreator;

import javax.tools.Tool;

public class MyApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/**
		 * 从输出里提取参数
		 */
//		String input_Name = args[0];
//		String output_Name = "SalaryInfo.txt";
//		String fromDate = args[1];
//		String toData = args[2];
		WorkSlotsCreator workSlotsCreator = new WorkSlotsCreator();
		
		FileHandler.setUpSalaryMap();
		System.out.println("\n*****Notice:Salary Map display Complete-----\n");
		String[][] table = FileHandler.convertInputCSVFileToArray("test1.csv");
		System.out.println("\n*****Data Table Display Complete-----\n");
		workSlotsCreator.createWorkSheetData(table);
		System.out.println("\n****Date Container Create Complete, total " + workSlotsCreator.getAllWorkSlotContainersList().size() +" days fetched, total number of WorkSlots is " +workSlotsCreator.getWorkSlotsSum()+"-----\n");


	}
}
