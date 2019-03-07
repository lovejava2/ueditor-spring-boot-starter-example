package com.example.ueditor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.ueditor.entity.AnchorEntity;
import com.example.ueditor.entity.NoticeEntity;
import com.example.ueditor.entity.StaticEntity;
import com.example.ueditor.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("anchor")
public class AnchorController {
    private String key = StaticEntity.key;
    private String nonce = StaticEntity.nonce;
    private String secret = StaticEntity.secret;

    @RequestMapping("/toList")
    public String toList(HttpServletRequest request, HttpServletResponse response){
        return "anchor/list";
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
            String uid = paramJson.getString("uid");
            Integer pageindex = paramJson.getInteger("page") - 1;
            Integer pagesize = paramJson.getInteger("size");

            //获取列表
            String sign1 = Md5Util.EncoderByMd5("key:"+key+ ",nonce:"+nonce+",pageindex:"+pageindex
                    +",pagesize:"+pagesize+",timestamp:"+timestamp+",token:"+token+",secret:"+secret);

            String urlStr1 = StaticEntity.staticUrl+"/lobby/admin_get_anchor_list";
            Map<String, Object> map2 = new HashMap<String, Object>();
            if(uid != null && uid.trim().length() > 0){
                map2.put("uid", uid);
            }
            map2.put("key", key);
            map2.put("nonce", nonce);
            map2.put("pageindex", pageindex);
            map2.put("pagesize", pagesize);
            map2.put("timestamp", timestamp);
            map2.put("token", token);
            map2.put("sign", sign1);
            Map<String, String> headers2 = new HashMap<String, String>();
            String b =  URLConnectionUtil.doPost(urlStr1,headers2,map2);
            System.out.println(b);
            //查询列表数据
            JSONObject jsStr = JSONObject.parseObject(b);
            System.out.print(jsStr);
            JSONArray myJsonArray = jsStr.getJSONArray("data");
            List<AnchorEntity> list = (List<AnchorEntity>) JSONArray.parseArray(jsStr.getString("data"), AnchorEntity.class);
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
    public String toAdd(HttpServletRequest request,ModelMap modelMap){
        return "anchor/add";
    }


    @RequestMapping("/save")
    public void save(HttpServletRequest request,HttpServletResponse response){
        String params = HTTPUtils.getPostParams(request);
        JSONObject result = new JSONObject();
        try {
            JSONObject paramJson = JSON.parseObject(params);
            String token = paramJson.getString("token");
            String uid = paramJson.getString("uid");
            String anchortype = paramJson.getString("anchortype");
            String nick = paramJson.getString("nick");
            //获取当前时间戳
            Timestamp ts = new Timestamp(new Date().getTime());
            long timestamp = ((ts.getTime())/1000);

            //保存接口
            String s = "uid:"+uid+",secret:"+secret;
            String sign = Md5Util.EncoderByMd5(s);
            String urlStr = StaticEntity.staticUrl+"/lobby/admin_add_or_update_anchor";
            Map<String, Object> map = new HashMap<String, Object>();

            if(uid != null){
                map.put("uid", uid);
            }
            if(anchortype != null){
                map.put("anchortype", anchortype);//
            }
            if(nick != null){
                map.put("nick", nick);//
            }
            map.put("timestamp", timestamp);
            map.put("token", token);
            map.put("key", key);
            map.put("nonce", nonce);
            map.put("sign", sign);
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
}
