import com.jason.app.time.WorkSlotsHandler;
import com.jason.app.utils.FileHandler;
import com.jason.app.utils.ImageHandler;
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

		//App的参数
		final int APP_WIDTH = 250;
		final int APP_HEIGHT = 205;
		final String KFT_VERSION_STRING  = " KungFuTea工资计算器v2.0";
		final String AUTHOR_LABEL = "        ~~ Developed By @Jason ~~";
		final String LABEL1 = "开始日期:";
		final String LABEL2 = "结束日期:";
		final String LABEL3 = "员工姓名:";
		final String BUTTON1 = "加载表格";
		final String BUTTON2 = "查询工资";
		final String ORIGINAL_IMAGE_PATH = "resource/1.jpg";

        //三个核心类的对象+一个背景处理器的对象
		final FileHandler fileHandler = new FileHandler();
		final WorkSlotsCreator workSlotsCreator = new WorkSlotsCreator();
		final WorkSlotsHandler workSlotsHandler = new WorkSlotsHandler();
		final ImageHandler imageHandler = new ImageHandler();

	    //背景图片处理
		imageHandler.printNewImage(ORIGINAL_IMAGE_PATH);
		JLabel bgLabel = imageHandler.setBackGrondImageLabel();

        //开始建立GUI
		JFrame jf = new JFrame(KFT_VERSION_STRING);
		jf.setLayout(null);
		jf.getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));

		JPanel jpanel = (JPanel)jf.getContentPane();
		jpanel.setLayout(null);
		jpanel.setBounds(0,0,APP_WIDTH,APP_HEIGHT);
		jpanel.setOpaque(false);

		//建立Label
		JLabel authorLabel,fromLabel, toLabel, nameLabel;
		authorLabel = new JLabel(AUTHOR_LABEL);
		fromLabel = new JLabel(LABEL1);
		toLabel = new JLabel(LABEL2);
		nameLabel = new JLabel(LABEL3);
		authorLabel.setBounds(0,7,250,21);;
		fromLabel.setBounds(30,35,70,30);
		toLabel.setBounds(30,70,70,30);
		nameLabel.setBounds(30,105,70,30);
		authorLabel.setFont(new Font("Times New Roman",1,14));
		fromLabel.setFont(new Font("Dialog",0,13));
		toLabel.setFont(new Font("Dialog",0,13));
		nameLabel.setFont(new Font("Dialog",0,13));
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
		fromText.setBounds(95,35,125,28);
		toText.setBounds(95,70,125,28);
		nameText.setBounds(95,105,125,28);

		jpanel.add(fromText);
		jpanel.add(toText);
		jpanel.add(nameText);

		//两个按钮
		JButton find, search;
		find = new JButton(BUTTON1);
		search = new JButton(BUTTON2);

		//第一个按钮功能：建立数据
		find.addActionListener(new ActionListener() {
			String string = "";
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!fromText.getText().endsWith("-") && !toText.getText().endsWith("-")) {
					string = String.format("载入 %s 到 %s的工作数据.",fromText.getText().toString(),toText.getText().toString());
				}else{
					string = "[Error]: 输入日期格式有误.";
				}
				JOptionPane.showMessageDialog(null, string);
				//phase 1: input worksheet and work on it.
				System.out.println("1.-->载入班表中....");
				String[][] table1 = FileHandler.convertInputCSVFileToArray("Tables/1.csv");
				String[][] table2 = FileHandler.convertInputCSVFileToArray("Tables/2.csv");
				String[][] table3 = FileHandler.convertInputCSVFileToArray("Tables/3.csv");
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
				System.out.println("本次有工资记录的员工人数有: "+ workSlotsCreator.getPersonMap().keySet()+"\n");
				System.out.println("请复制粘贴此列表以便逐个查询,或者你可以每次查人之前都点一下'加载表格'来重复显示该内容.👌");
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
						if (logs.endsWith("!")) {
							s = "工资表找不到这个人.";
						}
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

        //配置按钮参数
		find.setBackground(Color.WHITE);
		find.setOpaque(true);
		find.setBounds(10,140,107,35);
		search.setBackground(Color.WHITE);
		search.setOpaque(true);
		search.setBounds(135,140,107,35);

		jpanel.add(find);
		jpanel.add(search);
		
		//GUI 配置设定
		jf.setBounds(0,0,APP_WIDTH,APP_HEIGHT);
		jf.setVisible(true);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
