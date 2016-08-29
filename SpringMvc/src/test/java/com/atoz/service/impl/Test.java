package com.atoz.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test {
	
@org.junit.Test
 public void test1(){
	 List<String> names = new ArrayList<>();
	Collections.sort(names, (String a, String b) -> b.compareTo(a));
 }
}
