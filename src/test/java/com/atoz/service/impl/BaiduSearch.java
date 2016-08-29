package com.atoz.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.atoz.util.HtmlUtil;

public class BaiduSearch {
	private Map<String, String> map = new LinkedHashMap<String, String>();
	private Map<String, String> mapCatalog = new LinkedHashMap<String, String>();
	
	public Map<String, String> getMap() {
		return map;
	}

	public Map<String, String> getMapCatalog() {
		return mapCatalog;
	}

	public void setMapCatalog(Map<String, String> mapCatalog) {
		this.mapCatalog = mapCatalog;
	}

	public void searchKeyWord(String keyWord, int page) {
		int pageSize = 10;
		// 百度搜索结果每页大小为10，pn参数代表的不是页数，而是返回结果的开始数
		// 如获取第一页则pn=0，第二页则pn=10，第三页则pn=20，以此类推，抽象出模式：(page-1)*pageSize
		String url = "http://www.baidu.com/s?pn=" + (page - 1) * pageSize
				+ "&wd=" + keyWord;
		// 获取目录所在页面元素
		Document doc = HtmlUtil.getHtmlDoc(url);
		Elements links = doc.select("#content_left a");
		for (Element link : links) {
			String linkHref = link.attr("href");
			String title = link.text();
			String[] titiles = title.split("-");
			if (titiles.length > 1) {
				if(!"起点中文网".equals(titiles[titiles.length-1].trim())){
					//获取目录所在页面元素
					 Document docum = HtmlUtil.getHtmlDoc(linkHref);
					 linkHref = docum.location();
				}else{
					//获取目录所在页面元素
					 Document docum = HtmlUtil.getHtmlDoc(linkHref);
					 Map<String,String> urlMap =HtmlUtil.getElement(docum,linkHref);
					 linkHref = urlMap.get("点击阅读");
				}
				map.put(titiles[titiles.length - 1], linkHref);
			}
		}
	}
	
	public void showCatalog(String url){
		Document docum = HtmlUtil.getHtmlDoc(url);
		 //解析目录所在页面元素 获取所有章节url
		mapCatalog =HtmlUtil.getElement(docum,url);
	}

}
