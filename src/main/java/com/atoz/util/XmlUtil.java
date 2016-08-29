package com.atoz.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.thoughtworks.xstream.XStream;

public class XmlUtil {
	/**
	 * java对象装换成xml格式的字符串
	 * 
	 * @param obj
	 *            java对象
	 * @return xml格式的字符串
	 */
	public static <T> String toXml(T obj) {
		XStream xstream = new XStream();
		// System.out.println("------------Bean->XML------------");
		// System.out.println(xstream.toXML(obj));
		// System.out.println("重命名后的XML");
		// 类重命名
		// xstream.alias("account", Student.class);
		// xstream.alias("生日", Birthday.class);
		// xstream.aliasField("生日", Student.class, "birthday");
		// xstream.aliasField("生日", Birthday.class, "birthday");
		// System.out.println(xstream.toXML(bean));
		// 属性重命名
		// xstream.aliasField("邮件", Student.class, "email");
		// //包重命名
		// xstream.aliasPackage("hoo", "com.hoo.entity");
		// 类重命名
		xstream.aliasType(obj.getClass().getSimpleName().toLowerCase(),
				obj.getClass());
		return xstream.toXML(obj);
	}

	/**
	 * xml格式的字符串装换成java对象
	 * 
	 * @param xml
	 *            java对象
	 * @param className
	 *            类型
	 * @return java对象
	 */
	public static <T> T toT(String xml, Class<T> className) {
		XStream xstream = new XStream();
		xstream.aliasType(className.getSimpleName().toLowerCase(), className);
		return (T) xstream.fromXML(xml);
	}

	public static <T> List<T> parseXml(InputStream input, Class<T> className) {
		List<T> list = new ArrayList<T>();
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(input);
			Element rootElm = document.getRootElement();
			List nodes = rootElm.elements(className.getSimpleName()
					.toLowerCase());
			T t = null;
			for (Iterator it = nodes.iterator(); it.hasNext();) {
				t = className.newInstance();
				Element elm = (Element) it.next();

				Field[] fields = className.getDeclaredFields();
				for (Field field : fields) {
					String fieldName = field.getName();
					field.setAccessible(true);

					String value = "";
					if (elm.element(fieldName) != null) {
						Element el = elm.element(fieldName);
						value = el.getText();
					} else if (elm.attribute(fieldName) != null) {
						Attribute attr = elm.attribute(fieldName);
						value = attr.getValue();
					}
					if (field.getType() == String.class) {
						field.set((Object) t, value);
					} else if (field.getType() == Integer.class || field.getType() == int.class) {
						if (value == "") {
							field.set((Object) t, 0);
						} else {
							field.set((Object) t, Integer.parseInt(value));
						}
					}else if(field.getType() == Boolean.class){
						field.set((Object) t, Boolean.parseBoolean(value));
					}
				}
				list.add(t);
				t = null;
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static <T> void createXml(List<T> list,String path,Class<T> className){
		Document doc = DocumentHelper.createDocument();
		  //增加根节点
		  Element users = doc.addElement(className.getSimpleName().toLowerCase()+"s");
		  for(T t:list){
			  //增加子元素
			  Element user = users.addElement(className.getSimpleName().toLowerCase());
			  Field[] fields = className.getDeclaredFields();
			  for (Field field : fields) {
					String fieldName = field.getName();
					Element  fieldEle = user.addElement(fieldName);
					field.setAccessible(true);
					try {
						fieldEle.setText(String.valueOf(field.get(t)));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
		  }
		 
		  //实例化输出格式对象
		  OutputFormat format = OutputFormat.createPrettyPrint();
		  //设置输出编码
		  format.setEncoding("UTF-8");
		  //生成XMLWriter对象，构造函数中的参数为需要输出的文件流和格式
		  XMLWriter writer;
		try {
			writer = new XMLWriter(new FileOutputStream(path), format);
			//开始写入，write方法中包含上面创建的Document对象
			writer.write(doc);
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
