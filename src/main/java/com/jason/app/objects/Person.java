package com.jason.app.objects;

import static org.mockito.Matchers.booleanThat;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Person {
	private String name;
	@Setter private double salary;
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
