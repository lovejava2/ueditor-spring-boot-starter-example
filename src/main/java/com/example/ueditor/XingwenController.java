package com.example.ueditor;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.ueditorspringbootstarter.baidu.ueditor.ActionEnter;
import com.example.ueditor.entity.StaticEntity;
import com.example.ueditor.entity.XingWen;
import com.example.ueditor.util.*;
import org.json.JSONException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

@Controller
@RequestMapping("xingwen")
public class XingwenController {
    private String key = StaticEntity.key;
    private String nonce = StaticEntity.nonce;
    private String secret = StaticEntity.secret;


    @RequestMapping("/index")
    public String index(HttpServletRequest request){
        return "main";
    }

    @RequestMapping("/toList")
    public String toList(HttpServletRequest request,HttpServletResponse response){
        return "xingwen/list";
    }

    @RequestMapping("/getList")
    public void getList(HttpServletRequest request,HttpServletResponse response){
        String params = HTTPUtils.getRequestParams(request);
        JSONObject result = new JSONObject();
        try {
            Timestamp ts = new Timestamp(new Date().getTime());
            long timestamp = ((ts.getTime())/1000);
            JSONObject paramJson = JSON.parseObject(params);
            String token = paramJson.getString("token");
            String flag = paramJson.getString("flag");//默认查询自己的
            Integer pageindex = paramJson.getInteger("page") - 1;
            Integer pagesize = paramJson.getInteger("size");
//            String searchTs = paramJson.getString("ts");//查询创建时间
//            String searchNick = paramJson.getString("nick");//查询创建用户

            //获取列表
            String sign1 = Md5Util.EncoderByMd5("flag:"+1+",key:"+key+ ",nonce:"+nonce+",pageindex:"+pageindex
                    +",pagesize:"+pagesize+",timestamp:"+timestamp+",token:"+token+",secret:"+secret);

            String urlStr1 = StaticEntity.staticUrl+"/lobby/cms_get_posts";
            Map<String, Object> map2 = new HashMap<String, Object>();
            map2.put("flag", 1);
            map2.put("key", key);
            map2.put("nonce", nonce);
            map2.put("pageindex", pageindex);
            map2.put("pagesize", pagesize);
            map2.put("timestamp", timestamp);
            map2.put("token", token);
            map2.put("sign", sign1);
            map2.put("debug",1);
            Map<String, String> headers2 = new HashMap<String, String>();
            String b =  URLConnectionUtil.doPost(urlStr1,headers2,map2);
            System.out.println(b);
            //查询列表数据
            JSONObject jsStr = JSONObject.parseObject(b);
            System.out.print(jsStr);
            JSONArray myJsonArray = jsStr.getJSONArray("data");
            List<XingWen> list = (List<XingWen>) JSONArray.parseArray(jsStr.getString("data"), XingWen.class);
            Integer count = jsStr.getInteger("totalcount");
            PageUtils pageUtil = new PageUtils(list, count, pagesize, pageindex+1);
            result.put("success",1);
            result.put("data",jsStr);
            result.put("page",pageUtil);
        }catch (Exception e){
            result.put("success",0);
            result.put("msg","服务器异常");
        }finally {
            ResponseUtil.sendJsonNoCache(response,result.toJSONString());
        }
    }
    @RequestMapping("/toAdd")
    public String toAdd(HttpServletRequest request,String type,ModelMap modelMap){
        modelMap.put("type",type);
        return "xingwen/add";
    }

    //发布星文
    @RequestMapping("/save")
    public void save(HttpServletRequest request,HttpServletResponse response){
        String params = HTTPUtils.getPostParams(request);
        JSONObject result = new JSONObject();
        SimpleDateFormat daySdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            JSONObject paramJson = JSON.parseObject(params);
            String title = paramJson.getString("title");
//            String content = paramJson.getString("content") + StaticEntity.appendCon;
            String content = paramJson.getString("content");
            String contentHtml = paramJson.getString("contentHtml");
            String token = paramJson.getString("token");
            String cover_url_1 = paramJson.getString("cover_url_1");
            String cover_url_2 = paramJson.getString("cover_url_2");
            String cover_url_3 = paramJson.getString("cover_url_3");
            Integer flag = paramJson.getInteger("flag");
            Date ptm = paramJson.getDate("tm");
            Date t = new Date();
            if(flag == 0){
                ptm = t;
            }
            String tm = "";
            if(ptm != null && ptm.toString().trim().length() > 0){
                tm = daySdf.format(ptm);
            }
            //将内容写到文本中
            //获取当前时间戳
            Timestamp ts = new Timestamp(new Date().getTime());
            long timestamp = ((ts.getTime())/1000);

            //内容文件路径
          // String contentUrl = "/usr/local/tomcat/apache-tomcat-8.5.38/webapps/my_files/conTxt/"+timestamp+".html";
            String contentUrl = "E:/tomcat/apache-tomcat-8.0.53/webapps/my_files/conTxt/"+timestamp+".html";
            PrintWriter pw = new PrintWriter( new FileWriter( contentUrl) );
            String url_body = StaticEntity.staticFile+"/my_files/conTxt/"+timestamp+".html";
//          String url_body = contentUrl;
            pw.print(content);
            pw.close();
           String contentAllUrl = "E:/tomcat/apache-tomcat-8.0.53/webapps/my_files/conTxt/"+timestamp+"all.html";
           //String contentAllUrl = "/usr/local/tomcat/apache-tomcat-8.5.38/webapps/my_files/conTxt/"+timestamp+"all.html";
            PrintWriter pwAll = new PrintWriter( new FileWriter( contentAllUrl) );
            String url = StaticEntity.staticFile+"/my_files/conTxt/"+timestamp+"all.html";
//            String url = contentAllUrl;
            pwAll.print(contentHtml);
            pwAll.close();

            //保存接口
            String s = "body_url:"+url_body+"cover_url_1:"+cover_url_1+",cover_url_2:"+cover_url_2+",cover_url_3:"+cover_url_3
                    +",flag:"+flag+",key:"+key+",nonce:"+nonce+
                    ",timestamp:"+timestamp+",title:"+title+",tm:"+tm+",token:"+token+",url:"+url+",secret:"+secret;

            String sign = Md5Util.EncoderByMd5(s);
            String urlStr = StaticEntity.staticUrl+"/lobby/cms_add_posts";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("cover_url_1", cover_url_1);
            map.put("cover_url_2", cover_url_2);
            map.put("cover_url_3", cover_url_3);
            map.put("flag", flag);
            map.put("key", key);
            map.put("nonce", nonce);
            map.put("timestamp", timestamp);
            map.put("tm", tm);
            map.put("sign", sign);
            map.put("title", title);
            map.put("token", token);
            map.put("url", url);
            map.put("body_url", url_body);
            map.put("debug",1);
            Map<String, String> headers = new HashMap<String, String>();
            String a =  URLConnectionUtil.doPost(urlStr,headers,map);
            System.out.println(a);
            //查询列表数据
            JSONObject jsStr = JSONObject.parseObject(a);
            System.out.print(jsStr);
            result.put("code",JSON.parseObject(a).getString("errcode"));
            result.put("msg",JSON.parseObject(a).getString("errmsg"));
        }catch (Exception e){
            result.put("code","1");
            result.put("msg","服务器异常");
        }finally {
            ResponseUtil.sendJsonNoCache(response,result.toJSONString());
        }
    }


    @RequestMapping("/toEdit")
    public String toEdit(ModelMap modelMap, HttpServletRequest request){
        return "xingwen/edit";
    }

    @RequestMapping("/toShow")
    public String toShow(ModelMap modelMap, HttpServletRequest request){
        return "xingwen/show";
    }


    //修改
    @RequestMapping("/edit")
    public void edit(HttpServletRequest request,HttpServletResponse response){
        String params = HTTPUtils.getPostParams(request);

        JSONObject result = new JSONObject();
        SimpleDateFormat daySdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            JSONObject paramJson = JSON.parseObject(params);
            String title = paramJson.getString("title");
            String urlOld = paramJson.getString("url");
            String urlOldBodyUrl = paramJson.getString("body_url");

            if(null != urlOld && 0 < urlOld.length()){
                String[]  urls=urlOld.split("/");
                if(null != urls && 0 < urls.length){
                    String delStr = urls[urls.length - 1];
                    String delUrl = "/usr/local/tomcat/apache-tomcat-8.5.38/webapps/my_files/conTxt/"+delStr;
                    File file = new File(delUrl);
                    if (file.isFile()){
                        file.delete();
                    }
                }
            }

            if(null != urlOldBodyUrl && 0 < urlOldBodyUrl.length()){
                String[]  urls=urlOldBodyUrl.split("/");
                if(null != urls && 0 < urls.length){
                    String delStr = urls[urls.length - 1];
                    String delUrl = "/usr/local/tomcat/apache-tomcat-8.5.38/webapps/my_files/conTxt/"+delStr;
                    File file = new File(delUrl);
                    if (file.isFile()){
                        file.delete();
                    }
                }
            }

            String content = paramJson.getString("content");
            String contentHtml = paramJson.getString("contentHtml");
            String id = paramJson.getString("id");
            String token = paramJson.getString("token");
            String cover_url_1 = paramJson.getString("cover_url_1");
            String cover_url_2 = paramJson.getString("cover_url_2");
            String cover_url_3 = paramJson.getString("cover_url_3");
            Integer flag = paramJson.getInteger("flag");
            Date ptm = paramJson.getDate("tm");
            Date t = new Date();
            String tm = "";
            if(ptm != null && ptm.toString().trim().length() > 0){
                tm = daySdf.format(ptm);
            }
            //将内容写到文本中
            //获取当前时间戳
            Timestamp ts = new Timestamp(new Date().getTime());
            long timestamp = ((ts.getTime())/1000);
            //内容文件路径
            String contentUrl = "/usr/local/tomcat/apache-tomcat-8.5.38/webapps/my_files/conTxt/"+timestamp+".html";
            PrintWriter pw = new PrintWriter( new FileWriter( contentUrl) );
            String url_body = StaticEntity.staticFile+"/my_files/conTxt/"+timestamp+".html";
//            String url_body = contentUrl;
            pw.print(content);
            pw.close();


            String contentAllUrl = "/usr/local/tomcat/apache-tomcat-8.5.38/webapps/my_files/conTxt/"+timestamp+"all.html";
            PrintWriter pwAll = new PrintWriter( new FileWriter( contentAllUrl) );
            String url = StaticEntity.staticFile+"/my_files/conTxt/"+timestamp+"all.html";
//            String url = contentAllUrl;
            pwAll.print(contentHtml);
            pwAll.close();
            //保存接口
            String s = "body_url:"+url_body+"cover_url_1:"+cover_url_1+",cover_url_2:"+cover_url_2+",cover_url_3:"+cover_url_3
                    +",flag:"+flag+",id:"+id+",key:"+key+",nonce:"+nonce+
                    ",timestamp:"+timestamp+",title:"+title+",tm:"+tm+",token:"+token+",url:"+url+",secret:"+secret;

            String sign = Md5Util.EncoderByMd5(s);
            String urlStr = StaticEntity.staticUrl+"/lobby/cms_update_posts";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", id);
            map.put("cover_url_1", cover_url_1);
            map.put("cover_url_2", cover_url_2);
            map.put("cover_url_3", cover_url_3);
            map.put("flag", flag);
            map.put("key", key);
            map.put("nonce", nonce);
            map.put("timestamp", timestamp);
            map.put("tm", tm);
            map.put("sign", sign);
            map.put("title", title);
            map.put("token", token);
            map.put("url", url);
            map.put("debug",1);
            Map<String, String> headers = new HashMap<String, String>();
            String a =  URLConnectionUtil.doPost(urlStr,headers,map);
            System.out.println(a);
            //查询列表数据
            JSONObject jsStr = JSONObject.parseObject(a);
            System.out.print(jsStr);
            result.put("code",JSON.parseObject(a).getString("errcode"));
            result.put("msg",JSON.parseObject(a).getString("errmsg"));
        }catch (Exception e){
            result.put("code","1");
            result.put("msg","服务器异常");
        }finally {
            ResponseUtil.sendJsonNoCache(response,result.toJSONString());
        }
    }

    @RequestMapping("/del")
    public void del(HttpServletRequest request,HttpServletResponse response){
        String params = HTTPUtils.getPostParams(request);
        JSONObject result = new JSONObject();
        try {
            JSONObject paramJson = JSON.parseObject(params);
            //System.out.println(paramJson+"这是啥");
            String id = paramJson.getString("id");
            String token = paramJson.getString("token");
            //获取当前时间戳
            Timestamp ts = new Timestamp(new Date().getTime());
            long timestamp = ((ts.getTime())/1000);

            String s = "id:"+id+",key:"+key+",nonce:"+nonce+
                    ",timestamp:"+timestamp+",token:"+token+",secret:"+secret;
            String sign = Md5Util.EncoderByMd5(s);
            String urlStr = StaticEntity.staticUrl+"/lobby/cms_delete_posts";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", id);
            map.put("key", key);
            map.put("nonce", nonce);
            map.put("timestamp", timestamp);
            map.put("sign", sign);
            map.put("token", token);
            map.put("debug",1);
            Map<String, String> headers = new HashMap<String, String>();
            String a =  URLConnectionUtil.doPost(urlStr,headers,map);
            System.out.println(a);
            //查询列表数据
            JSONObject jsStr = JSONObject.parseObject(a);
            System.out.print(jsStr);
            result.put("code",JSON.parseObject(a).getString("errcode"));
            result.put("msg",JSON.parseObject(a).getString("errmsg"));
        }catch (Exception e){
            result.put("success",0);
            result.put("msg","服务器异常");
        }finally {
            ResponseUtil.sendJsonNoCache(response,result.toJSONString());
        }
    }

}
