import com.jason.app.time.WorkSlotsHandler;
import com.jason.app.utils.FileHandler;
import com.jason.app.utils.WorkSlotsCreator;
import java.text.ParseException;

public class MyApp {

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		/**
		 * 从输出里提取参数
		 */
//		String input_Name = args[0];
//		String output_Name = "SalaryInfo.txt";
		String fromDate = "2017-7-4";
		String toDate = "2017-7-9";
		WorkSlotsCreator workSlotsCreator = new WorkSlotsCreator();
		WorkSlotsHandler workSlotsHandler = new WorkSlotsHandler();
		
		//phase 1: input worksheet and work on it.
		FileHandler.setUpSalaryMap();
		System.out.println("\n(Action COMPLETE)Salary Map display Complete-----\n");
		String[][] table = FileHandler.convertInputCSVFileToArray("test1.csv");
		System.out.println("\n(Action COMPLETE)Data Table Display Complete-----\n");
		workSlotsCreator.createWorkSheetData(table);
		System.out.println("(Action COMPLETE)Date Container Create Complete, total " + workSlotsCreator.getAllWorkSlotContainersList().size() +" days fetched, total number of WorkSlots is " +workSlotsCreator.getWorkSlotsSum()+"-----\n");
        
		//phase 2: use the worked data, get parameter from input and fetch target data.
		workSlotsHandler.init(workSlotsCreator); 
		workSlotsHandler.findResult(fromDate, toDate, "文倩");
	}
}
