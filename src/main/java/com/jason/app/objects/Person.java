package com.jason.app.objects;

import lombok.Getter;
import lombok.Setter;

/*
** 员工类，没啥好说的
 */
@Getter
public class Person {
	private String name;
	@Setter private double salary;
	//先设定在这里，万一以后有用
	private boolean fullTime;
	
	public Person(String name) {
		this.name = name;
		this.salary = 0;
		this.fullTime = false;
	}
	public boolean isSame (Person p1) {
		boolean name = p1.getName().equals(getName());
		boolean salary = p1.getSalary() == getSalary();
		return name && salary;
	}
}
