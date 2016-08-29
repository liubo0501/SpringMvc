package com.atoz.model;

public class User {
	
	private int age;
	
	private int id;
	
	private String username;
	
	private String password;

	@Override
	public String toString() {
		return "User [age=" + age + ", id=" + id + ", username=" + username
				+ ", password=" + password + "]";
	}

	public User(int age, int id, String username, String password) {
		super();
		this.age = age;
		this.id = id;
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User() {
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
