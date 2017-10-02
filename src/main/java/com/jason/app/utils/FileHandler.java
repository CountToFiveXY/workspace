package com.jason.app.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class FileHandler {

	static HashMap<String, Double> salaryMap = new HashMap<>();
	
	/**
	 *  我假设功夫茶的员工数量不会超过15个（如果超过了请告诉我，我改一下参数）
	 */
	static int ROW = 13;
	static int COLUMN = 15;
	/**
	 *  将输出的csv文件转换为可以提取参数的数组模型
	 */	
	public static String[][] convertInputCSVFileToArray (String fileName) {
		System.out.println("2.Starting converting .CSV File To Array\n");
		String[][] workSheet = new String[ROW][COLUMN];
		try {
            @SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
            int i = 0;
            String line;
            
            while((line = reader.readLine()) != null) {
            	if (i >= ROW) {
            		System.out.println("输入文件人数太多，Jason设计人数时没考虑那么多人，请联系Jason或者自行修改代码参数");
            	}
            	String[] content = line.split(",");
            	int len = content.length;
            	if (len > COLUMN) {
            		System.out.println("你在逗我么...是有人一个礼拜上8天还是你打错了？");         		
            	} else {
            		for(int j = 0; j < len ; j ++) {
            			String s = content[j];
            			if(s.isEmpty() || s == ""){
            				workSheet[i][j] = "X";
            			}else{
            				workSheet[i][j] = Tools.removeBlankPrefixForString(s);
            			}
            		}
            		for(int k = len; k < COLUMN; k ++){
            			workSheet[i][k] = "X";
            		}
            	}
            	i++;
            }
		}catch (Exception e) {
			System.out.println("这个文件无法被打开，具体原因请参照:\n "+e.toString());
		}
		System.out.println("Converting .csv file succeed, start displaying Data Table\n");
		//这里打印出班表的二维数组模型以供对照//
		for(int i = 0; i < ROW; i ++){
			if(workSheet[i][0] == null) {
				System.out.println("This Line is Empty");
				continue;
			}
			System.out.print("第" + (i + 1) + "行 ");
			for(int j = 0; j < COLUMN; j ++){
				System.out.print(workSheet[i][j] + " ");
				if(j == COLUMN-1){
					System.out.println();
				}
			}
		}
		return workSheet;
	}

	public static void setUpSalaryMap() {
		System.out.println("1.Starting loading Salary Table\n");
		try {
            @SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader("salary.txt"));
            String line = null;
            while ((line = reader.readLine()) != null){
            	String[] content = line.split("-");
            	if (content.length != 2) {
            		System.out.println("工资对照表格式不对，请参照Jason的样本修改格式");
            	}
            	String personName = content[0];
            	Double salary = Double.parseDouble(content[1]);
            	if (salaryMap.containsKey(personName)) {
            		System.out.println("一个人不能有两份工资，请修改工资表");
            		continue;
            	}
            	salaryMap.put(personName, salary);
            }
		} catch (Exception e) {
			System.out.println("工资表无法被打开，具体原因请参照:\n "+e.toString());
		}
		System.out.println("Salary Map loading Success, start displaying Table\n");
		//这里打印出班表的二维数组模型以供对照//

		for(String personName :salaryMap.keySet()) {
			System.out.println(personName +"'s salary per hour is: " +salaryMap.get(personName));
		}
	}
	
	public static double findSalaryForThisPerson (String Name) {
		return salaryMap.get(Name);		
	}
}
