package com.example.ueditor;


import com.alibaba.fastjson.JSONObject;
import com.baidu.ueditorspringbootstarter.baidu.ueditor.ActionEnter;
import com.example.ueditor.entity.StaticEntity;
import com.example.ueditor.util.Md5Util;
import com.example.ueditor.util.R;
import com.example.ueditor.util.URLConnectionUtil;
import org.apache.http.cookie.Cookie;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("login")
public class Login {
    @Autowired
    private HttpServletRequest request;


    @RequestMapping("/index")
    public String index(HttpServletRequest request){
        return "index";
    }

    @RequestMapping("/tologin")
    public String login(HttpServletRequest request, HttpServletResponse response){
        return "login";
    }
    @RequestMapping("/test")
    public String test(HttpServletRequest request, HttpServletResponse response){
        return "view/test";
    }

    @RequestMapping("/ueditorConfig")
    public void getUEditorConfig(HttpServletResponse response){
        String rootPath = "src/main/resources/static";
        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/login")
    @ResponseBody
    public R login(@RequestParam Map<String, Object> params,HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = "0615rAiL0ivQW52lyekL0XsGiL05rAi5";
        String key = "horoscope";
        String nonce = "3876662232";
//        String pwd = Md5Util.string2MD5("123456");
        String pwd = params.get("pwd").toString();


        String secret = "WY#Bzd!91";
        //获取当前时间戳
        Timestamp ts = new Timestamp(new Date().getTime());
        long timestamp = ((ts.getTime())/1000);
//        String uname = "ErrishRoxas";
        String uname = params.get("uname").toString();
//        long timestamp = new Date().getTime();
        String sign = Md5Util.EncoderByMd5("code:"+code+",key:"+key+",nonce:"+nonce+",pwd:"+pwd+",timestamp:"+timestamp+",uname:"+uname+",secret:WY#Bzd!91");

        String urlStr = StaticEntity.staticUrl+ "/auth/cmslogin";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", code);
        map.put("key", key);
        map.put("nonce", nonce);
        map.put("timestamp", timestamp);
        map.put("sign", sign);
        map.put("uname", uname);
        map.put("pwd", pwd);
        Map<String, String> headers = new HashMap<String, String>();
        String a =  URLConnectionUtil.doPost(urlStr,headers,map);
        System.out.println(a);
        //查询列表数据
        JSONObject jsStr = JSONObject.parseObject(a);

        return R.ok().put("res",jsStr);
    }

    public static void main(String[] args) throws Exception {
        String code = "0615rAiL0ivQW52lyekL0XsGiL05rAi5";
        String key = "horoscope";
        String nonce = "3876662232";
        String pwd = Md5Util.EncoderByMd5("123456");
//        String pwd = params.get("pwd").toString();


        String secret = "WY#Bzd!91";
        //获取当前时间戳
        Timestamp ts = new Timestamp(new Date().getTime());
        long timestamp = ((ts.getTime())/1000);
        String uname = "ErrishRoxas";
//        String uname = params.get("uname").toString();
//        long timestamp = new Date().getTime();
        String sign = Md5Util.EncoderByMd5("code:"+code+",key:"+key+",nonce:"+nonce+",pwd:"+pwd+",timestamp:"+timestamp+",uname:"+uname+",secret:WY#Bzd!91");

        String urlStr = StaticEntity.staticUrl+"/auth/cmslogin";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", code);
        map.put("key", key);
        map.put("nonce", nonce);
        map.put("timestamp", timestamp);
        map.put("sign", sign);
        map.put("uname", uname);
        map.put("pwd", pwd);
        Map<String, String> headers = new HashMap<String, String>();
        String a =  URLConnectionUtil.doPost(urlStr,headers,map);
        System.out.println(a);
        //查询列表数据
        JSONObject jsStr = JSONObject.parseObject(a);
        System.out.println(a);
        //查询列表数据
        if(null != a){
            JSONObject jsData = jsStr.getJSONObject("data");
            if(jsData != null){
                String accesstoken = jsData.getString("accesstoken");
                //获取列表
                String sign1 = Md5Util.EncoderByMd5("flag:"+1+",key:"+key+ ",nonce:"+nonce+",pageindex:"+0
                        +",pagesize:"+500+",timestamp:"+timestamp+",token:"+accesstoken+",secret:WY#Bzd!91");

                String urlStr1 = StaticEntity.staticUrl+"/lobby/cms_get_posts";
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("flag", 1);
                map2.put("key", key);
                map2.put("nonce", nonce);
                map2.put("pageindex", 0);
                map2.put("pagesize", 500);
                map2.put("timestamp", timestamp);
                map2.put("token", accesstoken);
                map2.put("sign", sign1);
                Map<String, String> headers2 = new HashMap<String, String>();
                String b =  URLConnectionUtil.doPost(urlStr1,headers2,map2);
                System.out.print(b);
            }
        }
    }
}
