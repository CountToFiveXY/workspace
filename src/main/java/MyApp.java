import com.jason.app.time.WorkSlotsHandler;
import com.jason.app.utils.FileHandler;
import com.jason.app.utils.Tools;
import com.jason.app.utils.WorkSlotsCreator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
**Main类: 创建GUI, 调用各个类的函数
 */
public class MyApp {

	public static void main(String[] args) throws ParseException {
		
        //三个核心类的对象
		final FileHandler fileHandler = new FileHandler();
		final WorkSlotsCreator workSlotsCreator = new WorkSlotsCreator();
		final WorkSlotsHandler workSlotsHandler = new WorkSlotsHandler();

        //开始建立GUI
		JFrame jf = new JFrame(" KungFuTea工资计算器v1.0");
		jf.setLayout(null);

		JPanel jpanel = new JPanel();
		jpanel.setLayout(null);
		jpanel.setBackground(Color.WHITE);
		jpanel.setBounds(20,0,280,220);

		Container c = jf.getContentPane();
		c.add(jpanel,BorderLayout.CENTER);

		//建立Label
		JLabel authorLabel,fromLabel, toLabel, nameLabel;
		authorLabel = new JLabel("       ~~ Powered By Jason ~~");
		fromLabel = new JLabel("开始日期:");
		toLabel = new JLabel("结束日期:");
		nameLabel = new JLabel("员工姓名:");
		authorLabel.setBounds(20,5,200,23);;
		fromLabel.setBounds(10,35,70,30);
		toLabel.setBounds(10,70,70,30);
		nameLabel.setBounds(10,105,70,30);
		authorLabel.setBackground(Color.YELLOW);
		authorLabel.setForeground(Color.BLUE);
		authorLabel.setOpaque(true);
		
        jpanel.add(authorLabel);
		jpanel.add(fromLabel);
		jpanel.add(toLabel);
		jpanel.add(nameLabel);
		
        //获取当前日期
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String[] array = format.format(date).toString().split("-");
		String year = array[0];
		String month = array[1];

		//三个输入框
		final JTextField fromText, toText, nameText;
		fromText = new JTextField(year+"-"+month+"-");
		toText = new JTextField(year+"-"+month+"-");
		nameText = new JTextField();
		fromText.setBounds(90,35,150,28);
		toText.setBounds(90,70,150,28);
		nameText.setBounds(90,105,150,28);

		jpanel.add(fromText);
		jpanel.add(toText);
		jpanel.add(nameText);

		//两个按钮
		JButton find, search;
		find = new JButton("加载表格");
		search = new JButton("查询工资");

		//第一个按钮功能：建立数据
		find.addActionListener(new ActionListener() {
			String string = "";
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!fromText.getText().isEmpty() && !toText.getText().isEmpty()) {
					string = String.format("载入 %s 到 %s的工作数据.",fromText.getText().toString(),toText.getText().toString());
				}else{
					string = "[Error]: 输入日期格式有误.";
				}
				JOptionPane.showMessageDialog(null, string);
				//phase 1: input worksheet and work on it.
				System.out.println("1.-->载入班表中....");
				String[][] table1 = FileHandler.convertInputCSVFileToArray("1.csv");
				String[][] table2 = FileHandler.convertInputCSVFileToArray("2.csv");
				String[][] table3 = FileHandler.convertInputCSVFileToArray("3.csv");
				System.out.println("\n[Complete]: 表格数据已呈现完毕\n");

				//phase 2: create WorkData For sheets.
				System.out.println("2.-->转换班表中\n");
				workSlotsCreator.createWorkSheetData(table1);
				workSlotsCreator.createWorkSheetData(table2);
				workSlotsCreator.createWorkSheetData(table3);
				if (workSlotsCreator.getAllWorkSlotContainersList().size() != 21) {
					System.out.println("[Warning]: 3张班表应该有21天才对, 目前只有"+workSlotsCreator.getAllWorkSlotContainersList().size()+"天的班.\n");
				} else {
					System.out.println("[Complete]: 所有Containers都已建完, 共创建" + workSlotsCreator.getAllWorkSlotContainersList().size() +"天的Container, 班次总数为" +workSlotsCreator.getWorkSlotsSum()+"\n");
				}
				//phase 3: use the worked data, get parameter from input and fetch target data.
				System.out.println("将参数传递至Handler...\n");
				String fromDate = fromText.getText();
				String toDate = toText.getText();
				System.out.println("3.-->开始查找"+fromDate+"到"+toDate+"的班次.\n");
				try {
					workSlotsHandler.init(workSlotsCreator, fileHandler,fromDate, toDate);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				System.out.println("本次有工资记录的员工人数有: "+ workSlotsCreator.getPersonMap().keySet()+".\n");
			}
		});

		// 第二个按钮功能：查询工资并且记录
		search.addActionListener(new ActionListener() {
			String s = "";
			@Override
			public void actionPerformed (ActionEvent event) {
				fileHandler.setUpSalaryMap();
				System.out.println("[Complete]: 当前工资表呈现完毕\n");
				if (!nameText.getText().isEmpty()) {
					s = "开始查询"+nameText.getText()+"的工资";
					String personName = nameText.getText();
					try{
						String logs = "";
						String timeLine = "[RECORDED] 本次查询时间: "+Tools.getCurrentTime()+":@";
						logs += timeLine;
						logs += String.format("设定员工为:%s, 设定时间段为: %s 到 %s:@",nameText.getText(),fromText.getText(),toText.getText());
						logs += workSlotsHandler.findResult(personName);
						fileHandler.printSalarySummary(logs);
					} catch (ParseException p) {
						System.out.println(p.getMessage());
					}
				}else if (nameText.getText().equals("")) {
					s = "某个2货忘了输名字";
				}
				JOptionPane.showMessageDialog(null, s);

			}
		});
		find.setBackground(Color.pink);
		find.setOpaque(true);
		find.setBounds(5,140,107,35);
		search.setBackground(Color.pink);
		search.setOpaque(true);
		search.setBounds(130,140,107,35);

		jpanel.add(find);
		jpanel.add(search);
		
		//GUI 配置设定
		jf.setBounds(0,0,280,210);
		jf.setVisible(true);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
