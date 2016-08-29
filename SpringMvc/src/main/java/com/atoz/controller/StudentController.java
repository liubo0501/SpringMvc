package com.atoz.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.atoz.model.StudentList;
import com.atoz.model.student;
import com.atoz.service.IStudentService;
import com.atoz.util.FileUtil;
import com.atoz.util.PropertiesUtil;

@Controller
@RequestMapping("/student")
public class StudentController {

	@Autowired
	private IStudentService studentSer;
	@RequestMapping("/show")
	 public String toIndex(HttpServletRequest request,HttpServletResponse rep,Model model){  
	        int userId = Integer.parseInt(request.getParameter("id"));  
	        student user = this.studentSer.getStudentById(userId);  
	        model.addAttribute("student", user);  
	        System.out.println(JSON.toJSONString(user));
	        return "showUser";  
	 } 
	
	@RequestMapping("/file")
	 public String file(){  
	        return "fileUpload";  
	 } 
	//http://localhost:8880/SpringMvc/student/student?sId=1&sName=lisi&age=20
	@RequestMapping("/student")
	@ResponseBody
	 public void student(HttpServletRequest request,student student){  
	       System.out.println(student.toString());
	 } 
	
	//http://localhost:8880/SpringMvc/student/studentList?studentList[0].sId=1&studentList[0].sName=lisi&studentList[0].age=20&studentList[1].sId=2&studentList[1].sName=wangwu&studentList[1].age=20
	@RequestMapping("/studentList")
	@ResponseBody
	 public void studentList(HttpServletRequest request,StudentList studentList){  
	       System.out.println(studentList.toString());
	 } 
	
	// http://localhost:8880/SpringMvc/student/studentJson  method:POST
	// {"sId":"12","sName":"赵四","age":"15","sex":"true"}
	@RequestMapping("/studentJson")
	@ResponseBody
	 public void studentJson(@RequestBody student student){  
	       System.out.println(student.toString());
	 } 
	
	// http://localhost:8880/SpringMvc/student/studentXml
//	<?xml version="1.0" encoding="UTF-8"?>  
//	<student>
//	  <age>20</age>
//	 <sId>21</sId>
//	 <sName>张三</sName>
//	 <sex>true</sex>
//	</student>
	// student类上需要追加@XmlRootElement(name="student")和 @XmlElement("sId")注解
	@RequestMapping("/studentXml")
	@ResponseBody
	 public void studentXml(@RequestBody student student){  
	       System.out.println(student.toString());
	 } 
	

	@RequestMapping(value="/upload", method = RequestMethod.POST)
	public void upload(MultipartHttpServletRequest request,  HttpServletResponse response,@RequestParam("files") MultipartFile[] files) throws Exception {
		 if(files!=null&&files.length>0){  
	            //循环获取file数组中得文件  
	            for(int i = 0;i<files.length;i++){  
	                MultipartFile file = files[i];  
	                //保存文件  
	                FileUtil.upload(file);  
	            }  
	        } 
	}
	
	 @RequestMapping("/download")   
	    public void download(HttpServletRequest req, HttpServletResponse response,@RequestParam(value = "folderId") Long[] longFolder)   
	            throws Exception {
			try {
				String SEP = File.separator;
				String fileDir = PropertiesUtil.pros1.get("file_upload_path") + File.separator + "planeModel" + SEP ;
				String dst = fileDir + "123.zip";

				//执行下载
				FileUtil.downLoad(dst,"123.zip", response, false);
				
				File downLoadFile = new File(dst);
				downLoadFile.delete();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	 
	 @RequestMapping(value="/zipFile", method=RequestMethod.GET)  
	    public void zipReleasePM(HttpServletRequest req, HttpServletResponse response,@RequestParam(value = "folderId") Long[] longFolder)   
	            throws Exception {
			try {
				String SEP = File.separator;
				String fileDir = PropertiesUtil.pros1.get("file_upload_path") + SEP  + "planeModel" + SEP;
				
				File dstFile = new File(fileDir);
				if (!dstFile.exists()) {
					dstFile.mkdirs();
				}else{
					FileUtil.deleteDirectory(fileDir);
					dstFile.mkdirs();
				}
				String dst = fileDir+ "123.zip";
				FileUtil.zip("C:\\Users\\zhuo.zhang\\Desktop\\昌飞IT运维系统-需求确认书V1.docx", dst);
			} catch (Exception e) {
			}
	    }
	 
}
