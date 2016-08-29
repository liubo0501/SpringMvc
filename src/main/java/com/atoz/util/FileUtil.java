package com.atoz.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletResponse;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

	public static boolean upload(MultipartFile file) {  
        // 判断文件是否为空  
        if (!file.isEmpty()) {  
            try {  
            	String originalName = file.getOriginalFilename();			
    			int index = originalName.lastIndexOf(".");
    			
    			String dir = PropertiesUtil.pros1.get("file_upload_path") + File.separator;			
    			// 文件名（排掉扩展名）
    			String tempName = originalName.substring(0,index);
    			// 扩展名，类别
    			String extName = originalName.substring(index);
            	// 服务器路径
				String serverPath = dir + tempName;
				File fullDirF = new File(serverPath);
				if (!fullDirF.exists()) {
					fullDirF.mkdirs();
				}	
				File serverFile = new File(serverPath + File.separator + tempName + extName);
				if (!serverFile.exists()) {
					serverFile.createNewFile();
				}
                // 转存文件  
                file.transferTo(serverFile);  
                return true;  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return false;  
    }  
	

	/**
	 * 下载文件
	 * @param filePath
	 * @param response
	 * @param isOnLine
	 * @throws Exception
	 */
	public static void downLoad(String filePath,String fileName, HttpServletResponse response, boolean isOnLine) throws Exception {
        File f = new File(filePath);
        System.out.println(filePath);
        if (!f.exists()) {
            response.sendError(404, "File not found!");
            System.out.println("File not found!");
            throw new Exception("文件不存在!");
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;

        response.reset(); // 非常重要
        if (isOnLine) { // 在线打开方式
            URL u = new URL("file:///" + filePath);
            response.setContentType(u.openConnection().getContentType());
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
            response.setHeader("Content-Length", String.valueOf(f.length()));
        } else { // 纯下载方式
            response.setContentType("application/octet-stream;charset=utf-8");
            // 文件名应该编码成UTF-8
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "utf-8"));
            response.setHeader("Content-Length", String.valueOf(f.length()));
        }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0)
            out.write(buf, 0, len);
        out.flush();
        br.close();
        out.close();
    }


	public static void deleteDirectory(String fileDir) {
		File f = new File(fileDir);
        if (f.exists())
        {
            if (f.isDirectory())
            {
                File[] fs = f.listFiles();
                if (fs.length > 0)
                {
                    for (File file : fs)
                    {
                        deleteDirectory(file.getAbsolutePath());
                    }
                }
            }
            f.delete();
        }
	}


	public static void zip(String inputFile,String zipFileDir) {
		File zipFile = new File(zipFileDir);
		if(!zipFile.exists()){
			zipFile.mkdirs();
			zipFile.delete();
			try {
				zipFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File f = new File(inputFile);
		// File temp = new File(zipFileName);
		ZipOutputStream out;
		try {
			out = new ZipOutputStream(new FileOutputStream(zipFile));
			out.setEncoding("GBK");// 设置为GBK后在windows下就不会乱码了，如果要放到Linux或者Unix下就不要设置了
			// 设置压缩编码
			File[] fl = f.listFiles();
			String base = "";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
			out.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//zip(out, f, f.getName());// 递归压缩方法
		System.out.println("zip done");
	}

	private static void zip(ZipOutputStream out, File f, String base) throws Exception {
		System.out.println("Zipping   " + f.getName()); // 记录日志，开始压缩
		if (f.isDirectory()) { // 如果是文件夹，则获取下面的所有文件
			File[] fl = f.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));// 此处要将文件写到文件夹中只用在文件名前加"/"再加文件夹名
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else { // 如果是文件，则压缩
			out.putNextEntry(new ZipEntry(base)); // 生成下一个压缩节点
			FileInputStream in = new FileInputStream(f); // 读取文件内容
			int len;
			byte[] buf = new byte[1024];
			while ((len = in.read(buf, 0, 1024)) != -1) {
				out.write(buf, 0, len); // 写入到压缩包
			}
			in.close();
		}
	}

	/**
	 * 解压缩zip文件
	 * 
	 * @param fileName
	 *            要解压的文件名 包含路径 如："c:\\test.zip"
	 * @param filePath
	 *            解压后存放文件的路径 如："c:\\temp\\"
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void unZip(String fileName, String filePath) throws Exception {
		if(!filePath.endsWith(File.separator)){
			filePath += File.separator;
		}
		ZipFile zipFile = new ZipFile(fileName, "GBK"); // 以“GBK”编码创建zip文件，用来处理winRAR压缩的文件。
		Enumeration emu = zipFile.getEntries();

		while (emu.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) emu.nextElement();
			if (entry.isDirectory()) {
				new File(filePath + entry.getName()).mkdirs();
				continue;
			}
			BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

			File file = new File(filePath + entry.getName());
			File parent = file.getParentFile();
			if (parent != null && (!parent.exists())) {
				parent.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = bis.read(buf, 0, 1024)) != -1) {
				fos.write(buf, 0, len);
			}
			bos.flush();
			bos.close();
			bis.close();
			System.out.println("解压文件：" + file.getName());
		}
		System.out.println("unzip done");
		zipFile.close();
	}
}
