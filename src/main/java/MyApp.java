import com.jason.app.time.WorkSlotsHandler;
import com.jason.app.utils.FileHandler;
import com.jason.app.utils.WorkSlotsCreator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyApp {

	public static void main(String[] args) throws ParseException {
		
        //三个主要的workslot处理器
		final FileHandler fileHandler = new FileHandler();
		final WorkSlotsCreator workSlotsCreator = new WorkSlotsCreator();
		final WorkSlotsHandler workSlotsHandler = new WorkSlotsHandler();

        //开始建立gui
		JFrame jf=new JFrame(" 功夫茶工资计算器v1.0");
		jf.setLayout(null);

		JPanel jpanel= new JPanel();
		jpanel.setLayout(null);
		jpanel.setBackground(Color.WHITE);
		jpanel.setBounds(20,0,280,220);

		Container c=jf.getContentPane();
		c.add(jpanel,BorderLayout.CENTER);

		//建立label
		JLabel authorLabel,fromLabel, toLabel, nameLabel;
		authorLabel = new JLabel("       ~~Powered By Jason~~");
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
		
        //当前时间
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

		//2个按钮
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
					string = "[FBI WARNING]: 输入日期格式有误.";
				}
				JOptionPane.showMessageDialog(null, string);
				
				//phase 1: input worksheet and work on it.
				fileHandler.setUpSalaryMap();
				System.out.println("\n[Complete]: 当前工资表呈现完毕\n");
				System.out.println("2.Start converting .CSV File To Array");
				String[][] table1 = FileHandler.convertInputCSVFileToArray("1.csv");
				String[][] table2 = FileHandler.convertInputCSVFileToArray("2.csv");
				String[][] table3 = FileHandler.convertInputCSVFileToArray("3.csv");
				System.out.println("\n[Complete]: Data Table Display Complete-----\n");

				//phase 2: create WorkData For sheets.
				System.out.println("3.Start convert Data to Something used for program\n");
				workSlotsCreator.createWorkSheetData(table1);
				workSlotsCreator.createWorkSheetData(table2);
				workSlotsCreator.createWorkSheetData(table3);
				System.out.println("[Complete]: 所有Containers都已创建完成, 共创建" + workSlotsCreator.getAllWorkSlotContainersList().size() +"天的Container, 班次总数为" +workSlotsCreator.getWorkSlotsSum()+"\n");
				//phase 3: use the worked data, get parameter from input and fetch target data.
				String fromDate = fromText.getText();
				String toDate = toText.getText();
				System.out.format("4.开始查找 %s 到 %s的班次.\n",fromDate,toDate);
				try {
					workSlotsHandler.init(workSlotsCreator, fileHandler,fromDate, toDate);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		});
		// 第二个按钮功能：查询工资
		search.addActionListener(new ActionListener() {
			String s = "";
			@Override
			public void actionPerformed (ActionEvent event) {
				if (!nameText.getText().isEmpty()) {
					s = "开始查询"+nameText.getText()+"的工资";
					String personName = nameText.getText();
					try{
						workSlotsHandler.findResult(personName);
					} catch (ParseException p) {
						System.out.println(p.getMessage());
					}
				}else if (nameText.getText().equals("")) {
					s = "某些人是不是忘了输入名字呀~傻缺";
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
		
		//final GUI setting
		jf.setBounds(0,0,280,210);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//		String output_Name = "SalaryInfo.txt";
	}
}
