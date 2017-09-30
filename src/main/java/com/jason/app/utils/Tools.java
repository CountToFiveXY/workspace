package com.jason.app.utils;

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
}
