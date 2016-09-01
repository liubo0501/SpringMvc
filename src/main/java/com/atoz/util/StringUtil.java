package com.atoz.util;

public class StringUtil {
	/**
	 *  字符串按照大写字母分割如：abcDceCdf -> abc,Dce,Cdf
	 *  @param str 要分割的字符串
	 *  @return 分割后的字符串数组
	 */
  public static String[] splitByUpper(String str){
	 return str.split("(?=[A-Z])");
  }
}
