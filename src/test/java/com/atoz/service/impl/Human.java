package com.atoz.service.impl;

public interface Human {
 int say();
 default void eat(){
	 System.out.println("123123");
 }
}
