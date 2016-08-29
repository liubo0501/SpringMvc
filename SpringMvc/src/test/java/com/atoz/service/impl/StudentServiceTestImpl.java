package com.atoz.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.atoz.model.User;
import com.atoz.model.student;
import com.atoz.service.IStudentService;
import com.atoz.util.HtmlUtil;
import com.atoz.util.PropertiesUtil;
import com.atoz.util.XmlUtil;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类  
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})  
public class StudentServiceTestImpl {
	 private static Logger logger = Logger.getLogger(StudentServiceTestImpl.class);  
	@Autowired  
    private IStudentService userService;  
	
	 @Test  
	    public void test1() {  
	        student user = userService.getStudentById(-1);  
	        logger.info(JSON.toJSONString(user));  
	    } 
	 

	 @Test  
	    public void test2() {  
	       System.out.println(PropertiesUtil.pros1.getProperty("driver"));
	    } 
	 
	 @Test  
	    public void test3() {  
		 student stu = new student();
		 stu.setAge(25);
		 stu.setSex(true);
		 stu.setSId(11);
		 stu.setSName("张三");
	     System.out.println(XmlUtil.toXml(stu));
	    } 
	 
	 @Test  
	    public void test4() {  
		 student stu = new student();
		 stu.setAge(25);
		 stu.setSex(true);
		 stu.setSId(11);
		 stu.setSName("张三");
		 String xml = XmlUtil.toXml(stu);
		 stu= (student) XmlUtil.toT(xml, student.class);
		 System.out.println(stu.toString());
	    } 
	 
	 @Test  
	    public void test5() {  
		 File file = new File("students.xml");
		 List<student> list = null;
		try {
			InputStream input =  new FileInputStream(file);
			list = XmlUtil.parseXml(input, student.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		 System.out.println(list.toString());
	    } 
	 
	 @Test  
	    public void test6() {  
		 String path ="userss.xml";
		 List<User> list = new ArrayList<>();
		 
		 User user = new User(20, 1, "张三", "123456");
		 User user1 = new User(25, 2, "李四", "123456");
		 list.add(user);
		 list.add(user1);
		XmlUtil.createXml(list, path, User.class);
	    } 
	 
	 	@Test  
	    public void test7() {  
		//抓取站点url
		 String siteUrl = PropertiesUtil.pros1.getProperty("siteUrl");
		//获取目录所在页面元素
		 Document docum = HtmlUtil.getHtmlDoc(siteUrl);
		 //解析目录所在页面元素 获取所有章节url
		 Map<String,String> urlMap =HtmlUtil.getElement(docum,siteUrl);
		 //通过所有章节url 获取每个章节内容并保存
		 HtmlUtil.writeFile(siteUrl, "mojie.txt", urlMap, "UTF-8");
	    }
	 	
	 	@Test  
	    public void test8() throws IOException {  
//	 		 int pageSize = 10;  
	         //百度搜索结果每页大小为10，pn参数代表的不是页数，而是返回结果的开始数  
	         //如获取第一页则pn=0，第二页则pn=10，第三页则pn=20，以此类推，抽象出模式：(page-1)*pageSize  
//	         String url = "http://www.baidu.com/s?pn="+(page-1)*pageSize+"&wd="+keyword; 
		//抓取站点url
	 	String urlStr = "http://www.baidu.com/s?tn=ichuner&lm=-1&wd="+"魔界的女婿"+"&pn=100";
		//获取目录所在页面元素
		Document doc = HtmlUtil.getHtmlDoc(urlStr);
		Map<String,String> emap = new LinkedHashMap<String,String>();
		Elements links = doc.getElementsByTag("a");
		for(Element link:links){
			String linkHref =link.attr("href");
			String title = link.text();
			String[] titiles = title.split("-");
			if(titiles.length >1){
				emap.put(titiles[titiles.length-1], linkHref);
				System.out.println("网站："+ titiles[titiles.length-1] + "*****" + linkHref);
				if(!"起点中文网".equals(titiles[titiles.length-1].trim())){
					//获取目录所在页面元素
					 Document docum = HtmlUtil.getHtmlDoc(linkHref);
					 linkHref = docum.location();
					 docum = HtmlUtil.getHtmlDoc(linkHref);
					 //解析目录所在页面元素 获取所有章节url
					 Map<String,String> urlMap =HtmlUtil.getElement(docum,linkHref);
					 File file = new File("novel\\" + titiles[titiles.length-1] + ".txt");
				 	 if(!file.exists()){
				 		 File par = new File(file.getParent());
				 		 if(!par.exists()){
				 			par.mkdirs();
				 		 }
				 		file.createNewFile();
				 	 }
					 //通过所有章节url 获取每个章节内容并保存
					 HtmlUtil.writeFile(linkHref, file.getPath(), urlMap, "UTF-8");
				}else if("起点中文网".equals(titiles[titiles.length-1])){
					//获取目录所在页面元素
					 Document docum = HtmlUtil.getHtmlDoc(linkHref);
					 //解析目录所在页面元素 获取所有章节url
					 Map<String,String> urlMap =HtmlUtil.getElement(docum,linkHref);
					 linkHref = urlMap.get("点击阅读");
					 docum = HtmlUtil.getHtmlDoc(linkHref);
					 urlMap =HtmlUtil.getElement(docum,linkHref);
					 File file = new File("novel\\" + titiles[titiles.length-1] + ".txt");
				 	 if(!file.exists()){
				 		 File par = new File(file.getParent());
				 		 if(!par.exists()){
				 			par.mkdirs();
				 		 }
				 		file.createNewFile();
				 	 }
					 //通过所有章节url 获取每个章节内容并保存
					 HtmlUtil.writeFile(linkHref, file.getPath(), urlMap, "UTF-8");
				}
			}
		}
	    }
	 	
	 	 @Test  
		    public void test9() throws IOException {  
	 		String urlStr = "http://www.baidu.com/s?wd=魔界的女婿&pn=100";
	 		 URL url = new URL(urlStr);
	 		HttpURLConnection  uc = (HttpURLConnection) url.openConnection();
	         uc.setRequestMethod("GET");
	         uc.setConnectTimeout(6*1000);
	         if(uc.getResponseCode() == 200){
	         BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
	         PrintWriter print = new PrintWriter("C:\\Users\\zhuo.zhang\\Desktop\\123.txt");
	         String str= null;
	         while((str=br.readLine())!=null){
	        	 System.out.println(str);
	        	 print.write(str);
	         }
	         print.close();
	         br.close();	         }
		    } 
	 	 
	 	 @Test  
		 public void test10() {  
	 		BaiduSearch BaiduSearch =new BaiduSearch();
	 		BaiduSearch.searchKeyWord("魔界的女婿", 2);
	 		Map<String,String> map= BaiduSearch.getMap();
	 		for(Map.Entry<String, String> entry :map.entrySet()){
	 			System.out.println(entry.getKey()+entry.getValue());
	 		}
	 	 }
	 	 
	 	 @Test  
		 public void test11() {  
	 		BaiduSearch BaiduSearch =new BaiduSearch();
//	 		BaiduSearch.showCatalog("http://www.wxguan.com/wenzhang/0/165/");
	 		BaiduSearch.showCatalog("http://www.23wx.com/html/13/13760/");
	 		Map<String,String> map= BaiduSearch.getMapCatalog();
	 		for(Map.Entry<String, String> entry :map.entrySet()){
	 			System.out.println(entry.getKey()+entry.getValue());
	 		}
	 	 }
}
