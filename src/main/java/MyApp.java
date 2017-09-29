import com.jason.app.*;
import com.jason.app.utils.FileHandler;

public class MyApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/**
		 * 从输出里提取参数
		 */
		String input_Name = args[0];
		String output_Name = "SalaryInfo.txt";
		String employeeName = args[1];
		String fromDate = args[2];
		String toData = args[3];
		
		FileHandler.setUpSalaryMap();
		String[][] table = FileHandler.convertInputCSVFileToArray(input_Name);
		

	}

}
