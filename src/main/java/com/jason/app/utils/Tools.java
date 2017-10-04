package com.jason.app.utils;

import org.springframework.beans.factory.config.YamlProcessor.ResolutionMethod;

public class Tools {
	public static int convertStringToInteger(String s){
		return Integer.parseInt(s);
	}
	
	public static String[] convertStringToArray(String s){
		String[] result = s.split("-");
		if (result.length == 1) {
			return s.split("－");
		}
		return result;
	}

	public static String removeBlankPrefixForString (String s) {
		if (s.startsWith(" ")) {
			return s.substring(1);
		}
		return s;
	}
	
	public static String correctDate (String date) {
		String[] dateArray = convertStringToArray(date);
		String modifiedDate = "";
		int len = dateArray.length;
		if (len != 3) {
			System.out.println("this date is wrong");
		}
		for(int i = 0;i < len;i ++){
			String s = dateArray[i];
			if (s.length() == 1){
				s = "0" + s;
			}
			if (i >= len-1) {
				modifiedDate += s;
			}else{
				modifiedDate += s + "-";
			}
		}
		return modifiedDate;
	}
}
