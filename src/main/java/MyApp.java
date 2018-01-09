import com.jason.app.objects.Person;
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

	//App的参数
	private final static int APP_WIDTH = 250;
	private final static int APP_HEIGHT = 205;
	private final static String KFT_VERSION_STRING  = " KungFuTea工资计算器v2.0";
	private final static String AUTHOR_LABEL = "        ~~ Developed By @Jason ~~";
	private final static String LABEL1 = "开始日期:";
	private final static String LABEL2 = "结束日期:";
	private final static String LABEL3 = "员工姓名:";
	private final static String BUTTON1 = "加载表格";
	private final static String BUTTON2 = "查询";
	private final static String BUTTON3 = "一键查询";
	private final static String ORIGINAL_IMAGE_PATH = "resource/1.jpg";

	static JFrame jf = new JFrame(KFT_VERSION_STRING);
	static JPanel jpanel = (JPanel)jf.getContentPane();

	public static void main(String[] args) throws ParseException {

        //三个核心类的对象+一个背景处理器的对象
		final FileHandler fileHandler = new FileHandler();
		final WorkSlotsCreator workSlotsCreator = new WorkSlotsCreator();
		final WorkSlotsHandler workSlotsHandler = new WorkSlotsHandler();
		final ImageHandler imageHandler = new ImageHandler();

	    //背景图片处理
		imageHandler.printNewImage(ORIGINAL_IMAGE_PATH);
		JLabel bgLabel = imageHandler.setBackGrondImageLabel();

        //开始建立GUI
		jf.setLayout(null);
		jf.getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));

		jpanel.setLayout(null);
		jpanel.setBounds(0,0,APP_WIDTH,APP_HEIGHT);
		jpanel.setOpaque(false);

		//建立Label
		JLabel authorLabel,fromLabel, toLabel, nameLabel;

		authorLabel = createLable(AUTHOR_LABEL, new Font("Times New Roman",1,14));
		authorLabel.setBackground(Color.YELLOW);
		authorLabel.setForeground(Color.BLUE);
		authorLabel.setOpaque(true);

		fromLabel = createLable(LABEL1, new Font("Dialog",0,13));
		toLabel = createLable(LABEL2, new Font("Dialog",0,13));
		nameLabel = createLable(LABEL3, new Font("Dialog",0,13));

		setLabel(authorLabel,0,7,250,21);
		setLabel(fromLabel, 30,35,70,30);
		setLabel(toLabel, 30,70,70,30);
		setLabel(nameLabel,30,145,70,30);
		
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
		nameText.setBounds(95,145,60,32);

		jpanel.add(fromText);
		jpanel.add(toText);
		jpanel.add(nameText);

		//三个按钮
		JButton find, search, superSearch;
		find = createButton(BUTTON1, Color.white);
		search = createButton(BUTTON2, Color.white);
		superSearch = createButton(BUTTON3, Color.white);

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
				if (table1[0][0] == null) {
					System.out.println("\n[Error]: 找不到表格, 请将表格放在Tables文件夹下.\n");
				} else {
					System.out.println("\n[Complete]: 表格数据已呈现完毕\n");
				}

				//phase 2: create WorkData For sheets.
				System.out.println("2.-->转换班表中\n");
				workSlotsCreator.createWorkSheetData(table1);
				workSlotsCreator.createWorkSheetData(table2);
				workSlotsCreator.createWorkSheetData(table3);
				if (workSlotsCreator.getAllWorkSlotContainersList().size() == 0)  {
					System.out.println("[Error]: 一个班次都没找到，场面一度非常尴尬.\n");
				}
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
					System.out.println("出错了!错误信息："+e1.getStackTrace());
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
				//先把工资表的数据加载进来
				fileHandler.setUpSalaryMap();

				//开始对输入的那个人进行工资查询
				if (!nameText.getText().isEmpty()) {
					s = "查询"+nameText.getText()+"的工资";

					String personName = nameText.getText();
					//每次查询的Log前缀
					String logs = "";
					String timeLine = "[RECORDED] 本次单独查询时间: "+Tools.getCurrentTime()+":@";
					logs += timeLine;

					try{
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

		// 第三个按钮功能: 一键全查
		superSearch.addActionListener(new ActionListener() {
			String s = "";
			@Override
			public void actionPerformed(ActionEvent e) {
				s = "查询所有人的工资";

				//还是要先把工资表的数据加载进来
				fileHandler.setUpSalaryMap();

				//每次查询的Log前缀
				String logs = "";
				String timeLine = "[RECORDED] 本次全体查询时间: "+Tools.getCurrentTime()+":@";
				logs += timeLine;
				for(String employee : workSlotsCreator.getPersonMap().keySet()) {
					try{
						workSlotsHandler.findResult(employee);
					} catch (ParseException p) {
						System.out.println(p.getMessage());
					}
				}
				logs += String.format("时间段为: %s 到 %s, 所有人的工资为:@",fromText.getText(),toText.getText());
				for(String summaryString: workSlotsHandler.getPersonSalaryMap().values()) {
					logs += summaryString;
				}
				for(String person: workSlotsCreator.getPersonMap().keySet()) {
					if(!workSlotsHandler.getPersonSalaryMap().containsKey(person)) {
						logs += String.format("[出错啦!]一键查询并没有查询到 "+ person + "，请使用单独查询功能对此人检查错误.@");
					}
				}
				logs += "========================";
				fileHandler.printSalarySummary(logs);
				JOptionPane.showMessageDialog(null, s);
			}
		});


        //配置按钮参数
		setButton(find, 20,102,100,35);
		setButton(superSearch,125,102,100,35);
		setButton(search,165,144,60,30);
		
		//GUI 配置设定
		setJFrame();
	}

	private static JLabel createLable(String labelName, Font font) {
		JLabel label = new JLabel(labelName);
		label.setFont(font);
		return label;
	}

	private static void setLabel(JLabel label, int x, int y, int width, int height) {
		label.setBounds(x, y, width, height);
		jpanel.add(label);
	}

	private static JButton createButton(String buttonName, Color color) {
		JButton button = new JButton(buttonName);
		button.setBackground(color);
        button.setOpaque(true);
        return button;
	}

	private static void setButton(JButton button, int x, int y, int width, int height) {
		button.setBounds(x, y, width, height);
		jpanel.add(button);
	}

    //TBD
	private static JTextField createTextWindow(String text) {
		return null;
	}

	private static void setJFrame() {
		jf.setBounds(0,0,APP_WIDTH,APP_HEIGHT);
		jf.setVisible(true);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
