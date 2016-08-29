package com.atoz.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlUtil {

	/**
	1.解析方式 
	（1）从字符串解析 
	String html = "<html><head><title>First parse</title></head>" + "<body><p>Parse HTML into a doc.</p></body></html>";
	Document  doc = Jsoup.parse(html);
	从URL获取解析
	Document doc = Jsoup.connect("http://example.com/").get();
	String title = doc.title();
	
	Document doc = Jsoup.connect("http://example.com").data("query", "Java").userAgent("Mozilla").cookie("auth","token").timeout(3000).post();
	（3）从文件解析
	File input = new File("/tmp/input.html");
	Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
	
	2.DOM方式遍历元素
	（1）搜索元素
	getElementById(String id)
	getElementByTag(String tag)
	getElementByClass(String className)
	getElementByAttribute(String key)
	siblingElements(), firstElementSibling(), lastElementSibling(), nextElementSibling(), previousElementSibling()
	parent(), children(), child(int index)
	
	（2）获取元素数据
	attr(String key) – 获取key属性
	attributes() – 获取属性
	id(), className(), classNames()
	text() – 获取文本内容
	html() – 获取元素内部HTML内容
	outerHtml() – 获取包括此元素的HTML内容
	data() – 获取<srcipt>或<style>标签中的内容
	tag(), tagName()
	
	
	Elements items = doc.select("form[id=form1]");
	 **/
	
	public static Document getHtmlDoc(String siteUrl) {
		Document doc=null;
		try {
			Connection conn = Jsoup.connect(siteUrl).timeout(60*1000).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
			if (conn!=null){
				doc = conn.get();
			}
		} catch (IOException e) {
		e.printStackTrace();
		}
		return doc;
	}
	
	/**
	* 获取数据元素
	* @param doc
	* @return Map<String,String>
	*	key 章节名
	*	value url
	*/
	public static Map<String,String> getElement(Document doc,String url){
	Map<String,String> emap = new LinkedHashMap<String,String>();
	Elements links = doc.getElementsByTag("a");
	for(Element link:links){
		String linkHref =link.attr("href");
		if(linkHref.endsWith(".html")&&!linkHref.startsWith("http")){
			String linkText = link.text().trim();
			if(linkHref.startsWith("/")&&url.endsWith("/")){
				String reg = "^-?\\d{4,}$";
		        boolean flg =Pattern.compile(reg).matcher(linkHref.substring(linkHref.lastIndexOf("/")+1, linkHref.lastIndexOf("."))).find();
		        if(flg){
		        	emap.put(linkText, url + linkHref.substring(linkHref.lastIndexOf("/")+1));
		        }
			}else if(linkHref.startsWith("/") || url.endsWith("/")){
				emap.put(linkText, url + linkHref);
			}else{
				emap.put(linkText, url + "/" + linkHref);
			}
			
		}
	}
	return emap;
	}
	/**
	* 文件缓存
	* @param filePath 路径
	* @param emap	写入内容
	* @param encoding	文件编码
	*/
	public static void writeFile(String siteUrl, String filePath, Map<String,String> emap,
	String encoding) {
	try {
	File file = new java.io.File(filePath);
	if (!file.exists()) {
	file.createNewFile();
	}
	if (file.isFile() && file.exists()) {
	OutputStreamWriter write = new OutputStreamWriter(
	new FileOutputStream(file), encoding);
	BufferedWriter bufferedWriter = new BufferedWriter(write);
	//遍历Map
	Set<String> eset = emap.keySet();
	Iterator<String> it = eset.iterator();
	String eName = "";
	String content = "";
	while(it.hasNext()){
		eName = it.next();
		content = getContents(eName, emap.get(eName));
		if(content!=""){
			bufferedWriter.write(eName);
			bufferedWriter.newLine();
			bufferedWriter.write(content);
			bufferedWriter.newLine();
		}
	}
	bufferedWriter.flush();
	write.close();
	} else {
	System.out.println("file error");
	}
	} catch (Exception e) {
	System.out.println("writeFile error");
	e.printStackTrace();
	}
	}
	/**
	* catch内容
	* @param conUrl
	* @return
	*/
	private static String getContents(String eName, String conUrl){
		Document doc = getHtmlDoc(conUrl);
		if(doc == null){
			return "";
		}
		Elements content = doc.select("[id*='content']");
		if(content == null){
			return "章节缓存失败";
		}
		return content.text();
	}
}
