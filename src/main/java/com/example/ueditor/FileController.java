package com.example.ueditor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.ueditorspringbootstarter.baidu.ueditor.define.FileType;
import com.example.ueditor.entity.StaticEntity;
import com.example.ueditor.util.CodeHelper;
import com.example.ueditor.util.FileUtil;
import com.example.ueditor.util.HTTPUtils;
import com.example.ueditor.util.ResponseUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-19 16:02:20
 */
@Controller
@RequestMapping("file")
public class FileController  {


	@ResponseBody
	@RequestMapping("/upload")
	public void upload(@RequestParam("file") MultipartFile file, HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		String fileName =  file.getOriginalFilename();
		fileName = FileUtil.renameToUUID(fileName);
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		String newName = CodeHelper.createYzm(6) + suffixName;//防止中文名称出问题
		String path = "/usr/local/tomcat/apache-tomcat-8.5.38/webapps/my_files/files/";
		try {
			FileUtil.uploadFile(file.getBytes(), path, newName);
			//todo 本地测试
			String showUrl = StaticEntity.staticFile+ "/my_files/files/"+newName;
//			String showUrl = "d:/usr/local/tomcat/webapps/my_files/files/"+newName;
			result.put("code",0);
			result.put("fileUrl",showUrl);
		} catch (Exception e) {
			result.put("code",1);
			result.put("msg","服务器异常");
		}
		ResponseUtil.sendJsonNoCache(response,result.toJSONString());
	}
	//删除
	@RequestMapping("/del")
	public void edit(HttpServletRequest request,HttpServletResponse response){
		String params = HTTPUtils.getPostParams(request);
		JSONObject paramJson = JSON.parseObject(params);
		String url = paramJson.getString("url");
		if(null != url && 0 < url.length()){
			File file = new File(url);
			if (file.isFile()){
				file.delete();
			}

		}

	}


	@ResponseBody
	@RequestMapping("/getContent")
	public void test( HttpServletRequest request,HttpServletResponse response) {
		String read;
		String readStr ="";
		String params = HTTPUtils.getPostParams(request);
		JSONObject paramJson = JSON.parseObject(params);
		JSONObject result = new JSONObject();
//		File file = new File(url);
		try {
			URL url = new URL(paramJson.getString("url"));
//			URL url = new URL("D:/usr/local/tomcat/webapps/my_files/conTxt/1547431841.html");

			HttpURLConnection urlCon = (HttpURLConnection)url.openConnection();
			urlCon.setConnectTimeout(5000);
			urlCon.setReadTimeout(5000);
			BufferedReader br =new BufferedReader(new InputStreamReader( urlCon.getInputStream()));
			while ((read = br.readLine()) !=null) {
				readStr = readStr + read;
			}

			result.put("code",0);
			result.put("con",readStr);
//			Document doc= Jsoup.parse(new URL(url),1000);
//			result.put("con",doc.toString());

		} catch (Exception e) {
			result.put("code",1);
			result.put("msg","服务器异常");
		}
		ResponseUtil.sendJsonNoCache(response,result.toJSONString());
	}



/*    @ResponseBody
    @RequestMapping("/getContent")
    public void test( HttpServletRequest request,HttpServletResponse response) {
        String params = HTTPUtils.getPostParams(request);
        JSONObject paramJson = JSON.parseObject(params);
        String url = paramJson.getString("url");
//        String url = "D:/usr/local/tomcat/webapps/my_files/conTxt/1547431841.html";
        JSONObject result = new JSONObject();
        File file = new File(url);
        try {
            result.put("code",0);
            result.put("con",txt2String(file));
			Document doc= Jsoup.parse(new URL(url),1000);
			result.put("con",doc.toString());

        } catch (Exception e) {
            result.put("code",1);
            result.put("msg","服务器异常");
        }
        ResponseUtil.sendJsonNoCache(response,result.toJSONString());
    }*/



	public static String txt2String(File file){
		StringBuilder result = new StringBuilder();
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
			String s = null;
			while((s = br.readLine())!=null){//使用readLine方法，一次读一行
				result.append(System.lineSeparator()+s);
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result.toString();
	}

}
