package com.example.ueditor;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.ueditor.entity.Audit;
import com.example.ueditor.entity.StaticEntity;
import com.example.ueditor.entity.XingWen;
import com.example.ueditor.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("audit")
public class AuditController {

    private String key = StaticEntity.key;
    private String nonce = StaticEntity.nonce;
    private String secret = StaticEntity.secret;

    @RequestMapping("/toList")
    public String toList(HttpServletRequest request,HttpServletResponse response){
        return "audit/list";
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
            //获取列表
            String sign1 = Md5Util.EncoderByMd5("key:"+key+ ",nonce:"+nonce+",pageindex:"+",timestamp:"+timestamp+",token:"+token+",secret:"+secret);
            String urlStr1 = StaticEntity.staticUrl+"/lobby/admin_get_audit_version_log";
            Map<String, Object> map2 = new HashMap<String, Object>();
            map2.put("key", key);
            map2.put("nonce", nonce);
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
            List<Audit> list = (List<Audit>) JSONArray.parseArray(jsStr.getString("data"), Audit.class);
            Integer count = jsStr.getInteger("totalcount");
            result.put("success",1);
            result.put("data",jsStr);
            result.put("list",list);
            result.put("count",count);
        }catch (Exception e){
            result.put("success",0);
            result.put("msg","服务器异常");
        }finally {
            ResponseUtil.sendJsonNoCache(response,result.toJSONString());
        }
    }
    @RequestMapping("/toAdd")
    public String toAdd(HttpServletRequest request){
        return "audit/add";
    }

    @RequestMapping("/save")
    public void save(HttpServletRequest request,HttpServletResponse response){
        String params = HTTPUtils.getPostParams(request);
        JSONObject result = new JSONObject();
        SimpleDateFormat daySdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            JSONObject paramJson = JSON.parseObject(params);
            String ver = paramJson.getString("ver");
            String boundID = paramJson.getString("boundID");
            String switchType = paramJson.getString("switchType");
            String token = paramJson.getString("token");
            boolean flag = true;
            if(switchType.equals("1")){
                flag = true;
            }else{
                flag = false;
            }

            //获取当前时间戳
            Timestamp ts = new Timestamp(new Date().getTime());
            long timestamp = ((ts.getTime())/1000);

            //保存接口
            String s ="audit_version:"+ver+ ",bundle_id:"+boundID +",flag:"+flag+",key:"+key+",nonce:"+nonce+
                    ",timestamp:"+timestamp+",token:"+token+",secret:"+secret;

            String sign = Md5Util.EncoderByMd5(s);
            String urlStr = StaticEntity.staticUrl+"/lobby/admin_set_audit_version_log";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("key", key);
            map.put("nonce", nonce);
            map.put("timestamp", timestamp);
            map.put("sign", sign);
            map.put("token", token);
            map.put("debug",1);
            map.put("audit_version",ver);
            map.put("bundle_id",boundID);
            map.put("flag",flag);
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
            String id = paramJson.getString("id");
            String token = paramJson.getString("token");
            //获取当前时间戳
            Timestamp ts = new Timestamp(new Date().getTime());
            long timestamp = ((ts.getTime())/1000);

            String s = "id:"+id+",key:"+key+",nonce:"+nonce+
                    ",timestamp:"+timestamp+",token:"+token+",secret:"+secret;
            String sign = Md5Util.EncoderByMd5(s);
            String urlStr = StaticEntity.staticUrl+"/lobby/admin_del_audit_version_log";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("key", key);
            map.put("id", id);
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
