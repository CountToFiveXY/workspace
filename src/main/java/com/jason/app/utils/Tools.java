package com.jason.app.utils;

public class Tools {
	public static int convertStringToInteger(String s){
		return Integer.parseInt(s);
	}
	
	public static String[] convertStringToArray(String s){
		return s.split("-");
	}
}
