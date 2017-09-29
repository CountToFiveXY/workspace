package com.jason.app.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Person {
	private String name;
	private Gender gender;
	@Setter private double salary;
	private boolean fulltime;
	
	public Person(String name,Gender gender) {
		this.name = name;	
		this.gender = gender;		
		this.salary = 0;
		this.fulltime = false;
	}
}
