package com.jason.app.utils;

import java.io.*;
import java.util.HashMap;

import lombok.Getter;

/*
** 这个类主要是处理各类输入输出文件比如工资表啊，班表啊，以及要输出给用户的工资查询日志，所以
*/
public class FileHandler {

	@Getter HashMap<String, Double> salaryMap = new HashMap<>();

	//假设功夫茶的员工数量不会超过13个
	static final int ROW = 13;
	static final int COLUMN = 15;

	/**
	 *  将输出的csv文件转换为可以提取参数的数组模型
	 */	
	public static String[][] convertInputCSVFileToArray (String fileName) {
		String[][] workSheet = new String[ROW][COLUMN];
		try {
            @SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
			int i = 0;
            while((line = reader.readLine()) != null) {
            	if (i >= ROW) {
            		System.out.println("该程序只取前11人，如扩招请找Jason");
            		break;
            	}
            	String[] content = line.split(",");
            	int len = content.length;
            	if (len > COLUMN) {
            		System.out.println("是有人一个礼拜上8天么嘻嘻？");         		
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
			System.out.println("\n[Error]:"+fileName+"文件无法被打开，程序结束，具体原因:\n "+e.toString());
		}
		//这里打印出班表的二维数组模型以供对照//
		System.out.println("\n表格"+fileName.charAt(0)+":");
		for(int i = 0; i < ROW; i ++){
			if(workSheet[i][0] == null) {
				System.out.println("This Line is Empty");
				continue;
			}
			if (i == 0) {
				System.out.print("日期行 ");
			}else if (i ==1) {
				System.out.print("星期行 ");
			}else{
				System.out.print("第" + (i-1) + "行 ");
			}
			for(int j = 0; j < COLUMN; j ++){
				System.out.print(workSheet[i][j] + " ");
				if(j == COLUMN-1){
					System.out.println();
				}
			}
		}
		return workSheet;
	}

	public void setUpSalaryMap() {
		System.out.println("-->开始载入工资表格");
		try {
            @SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader("salary.txt"));
            String line = null;
            while ((line = reader.readLine()) != null){
            	String[] content = line.split("-");
            	if (content.length != 2) {
            		System.out.println("工资对照表格式不对，请参照Jason的样本修改格式\n");
            	}
            	String personName = content[0];
            	Double salary = Double.parseDouble(content[1]);
            	if (salaryMap.containsKey(personName)) {
            		System.out.println(personName+"已有工资，无需反复加载");
            		continue;
            	}
            	salaryMap.put(personName, salary);
				System.out.println("载入"+personName+"的工资");
            }
		} catch (Exception e) {
			System.out.println("\n工资表无法被打开，具体原因请参照:\n "+e.toString());
			return;
		}
		System.out.println("工资表载入完毕，将进行确认");
		//这里打印出班表的二维数组模型以供对照//

		for(String personName :salaryMap.keySet()) {
			System.out.println(personName +"的每小时工资是: " +salaryMap.get(personName));
		}
	}

	public void printSalarySummary (String logs) {
		try {
			File file = new File("工资查询记录.txt");
			boolean isExisted = file.exists();
			FileWriter fw = new FileWriter("工资查询记录.txt",true);
			BufferedWriter writer = new BufferedWriter(fw);
			if (!isExisted) {
				writer.write("🌚功夫茶员工工资查询日志🌝");
				writer.newLine();
				writer.write("========================");
				writer.newLine();
			}
			String[] logInfo = logs.split("@");

			for (String log : logInfo) {
				writer.write(log);
				writer.newLine();
			}
			writer.close();
			fw.close();
		} catch (IOException e) {
			System.out.println(e.getStackTrace());
			return;
		}
	}
}
