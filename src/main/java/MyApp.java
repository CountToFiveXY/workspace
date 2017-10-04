import com.jason.app.time.WorkSlotsHandler;
import com.jason.app.utils.FileHandler;
import com.jason.app.utils.WorkSlotsCreator;
import java.text.ParseException;
import java.util.Scanner;

public class MyApp {

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		/**
		 * 从输出里提取参数
		 */		
		Scanner input = new Scanner(System.in);
		System.out.println("请输入开始日期:");
		String fromDate = input.nextLine();	
		System.out.println("开始日期设定: "+fromDate);
		String output_Name = "SalaryInfo.txt";	
		System.out.println("请输入结束日期：");
		String toDate = input.nextLine();
		System.out.println("结束日期设定: "+toDate);
		FileHandler fileHandler = new FileHandler();
		WorkSlotsCreator workSlotsCreator = new WorkSlotsCreator();
		WorkSlotsHandler workSlotsHandler = new WorkSlotsHandler();
		
		//phase 1: input worksheet and work on it.
		fileHandler.setUpSalaryMap();
		System.out.println("\n(Action COMPLETE)Salary Map display Complete-----\n");
		System.out.println("2.Starting converting .CSV File To Array\n");
		String[][] table1 = FileHandler.convertInputCSVFileToArray("1.csv");
		String[][] table2 = FileHandler.convertInputCSVFileToArray("2.csv");
		String[][] table3 = FileHandler.convertInputCSVFileToArray("3.csv");
			
		//phase 2: create WorkData For sheets.
		workSlotsCreator.createWorkSheetData(table1);
		System.out.println("\n(Action COMPLETE)Data Table Display Complete-----\n");
		workSlotsCreator.createWorkSheetData(table2);
		workSlotsCreator.createWorkSheetData(table3);
		System.out.println("(Action COMPLETE)Date Container Create Complete, total " + workSlotsCreator.getAllWorkSlotContainersList().size() +" days fetched, total number of WorkSlots is " +workSlotsCreator.getWorkSlotsSum()+"-----\n");
        
		//phase 3: use the worked data, get parameter from input and fetch target data.
		System.out.format("4.Starting fetching WorkSlots between %s and %s.\n",fromDate,toDate);
		System.out.println("请输入查询人的名字:");
		String personName = input.nextLine();
		workSlotsHandler.init(workSlotsCreator, fileHandler); 
		workSlotsHandler.findResult(fromDate, toDate, personName);
	}
}
