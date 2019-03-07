package com.example.ueditor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.ueditor.entity.CountryEntity;
import com.example.ueditor.entity.OperateEntity;
import com.example.ueditor.entity.StaticEntity;
import com.example.ueditor.entity.XingWen;
import com.example.ueditor.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("operate")
public class OperateController {
    private String key = StaticEntity.key;
    private String nonce = StaticEntity.nonce;
    private String secret = StaticEntity.secret;

    @RequestMapping("/toList")
    public String toList(HttpServletRequest request, HttpServletResponse response){
        return "operate/list";
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
            String begintime = paramJson.getString("begintime");
            String endtime = paramJson.getString("endtime");
            Integer pageindex = paramJson.getInteger("page") - 1;
            Integer pagesize = paramJson.getInteger("size");

            //获取列表
            String sign1 = Md5Util.EncoderByMd5("begintime:"+begintime+",endtime:"+endtime+",key:"+key+ ",nonce:"+nonce+",pageindex:"+pageindex
                    +",pagesize:"+pagesize+",timestamp:"+timestamp+",token:"+token+",secret:"+secret);

            String urlStr1 = StaticEntity.staticUrl+"/lobby/cms_get_banner_list";
            Map<String, Object> map2 = new HashMap<String, Object>();
            if(begintime != null){
                map2.put("begintime", begintime);
            }
            if(endtime != null){
                map2.put("endtime", endtime);
            }
            map2.put("key", key);
            map2.put("nonce", nonce);
            map2.put("pageindex", pageindex);
            map2.put("pagesize", pagesize);
            map2.put("timestamp", timestamp);
            map2.put("token", token);
            map2.put("sign", sign1);
            map2.put("img", "img");
            map2.put("debug",1);
            Map<String, String> headers2 = new HashMap<String, String>();
            String b =  URLConnectionUtil.doPost(urlStr1,headers2,map2);
            System.out.println(b);
            //查询列表数据
            JSONObject jsStr = JSONObject.parseObject(b);
            System.out.print(jsStr);
            JSONArray myJsonArray = jsStr.getJSONArray("data");
            List<OperateEntity> list = (List<OperateEntity>) JSONArray.parseArray(jsStr.getString("data"), OperateEntity.class);
            Integer count = jsStr.getInteger("totalcount");
            PageUtils pageUtil = new PageUtils(list, count, pagesize, pageindex+1);
            result.put("success",1);
            result.put("data",jsStr);
            result.put("page",pageUtil);
            getTypeCountryList(token);
        }catch (Exception e){
            result.put("success",0);
            result.put("msg","服务器异常");
        }finally {
            ResponseUtil.sendJsonNoCache(response,result.toJSONString());
        }
    }


    @RequestMapping("/toAdd")
    public String toAdd(HttpServletRequest request,ModelMap modelMap){
        String params = HTTPUtils.getRequestParams(request);
        return "operate/add";
    }

    @RequestMapping("/save")
    public void save(HttpServletRequest request,HttpServletResponse response){
        String params = HTTPUtils.getPostParams(request);
        JSONObject result = new JSONObject();
        try {
            SimpleDateFormat daySdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            JSONObject paramJson = JSON.parseObject(params);
            String token = paramJson.getString("token");
            String begintime = paramJson.getString("startTime");
            String endtime = paramJson.getString("endTime");
            String url = paramJson.getString("url");
            String img = paramJson.getString("img");

            String title = paramJson.getString("title");
            String content = paramJson.getString("content");
            String type = paramJson.getString("type");
            String enabled = paramJson.getString("enabled");
            String disporder = paramJson.getString("disporder");

            //获取当前时间戳
            Timestamp ts = new Timestamp(new Date().getTime());
            long timestamp = ((ts.getTime())/1000);
            String createtime = daySdf.format(ts);

            //保存接口
            String s = "begintime:"+begintime+",content:"+content+",createtime:"+createtime
                    +",endtime:"+endtime+",secret:"+secret;

            String sign = Md5Util.EncoderByMd5(s);
            String urlStr = StaticEntity.staticUrl+"/lobby/cms_add_banner";
            Map<String, Object> map = new HashMap<String, Object>();
            if(begintime != null){
                map.put("begintime", begintime);
            }
            if(endtime != null){
                map.put("endtime", endtime);//过期时间
            }

            if(createtime != null){
                map.put("createtime", createtime);
            }
            if(title != null){
                map.put("title", title);
            }
            if(content != null){
                map.put("content", content);//内容
            }

            if(url != null){
                map.put("url", url);
            }

            if(img != null){
                map.put("img", img);
            }

            if(type != null){
                map.put("banner_type", type);
            }
            if(disporder != null){
                map.put("disporder", disporder);
            }
            if(enabled != null){
                map.put("enabled", enabled);
            }
            getTypeCountryList(token);
            String country = "";
            if(null != StaticEntity.countryList && StaticEntity.countryList.size() > 0){
                for(CountryEntity c : StaticEntity.countryList){
                    if(c.getName().equals("CHINA")){
                        country = c.getCid();
                        continue;
                    }

                }

            }
            if(country != null && country != ""&& country.trim().length() > 0){
                map.put("areas", country);
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

    @RequestMapping("/toEdit")
    public String toEdit(ModelMap modelMap, HttpServletRequest request){
        return "operate/edit";
    }

    @RequestMapping("/update")
    public void update(HttpServletRequest request,HttpServletResponse response){
        String params = HTTPUtils.getPostParams(request);
        JSONObject result = new JSONObject();
        try {
            SimpleDateFormat daySdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            JSONObject paramJson = JSON.parseObject(params);
            String id = paramJson.getString("id");
            String token = paramJson.getString("token");
            String begintime = paramJson.getString("startTime");
            String endtime = paramJson.getString("endTime");
            String url = paramJson.getString("url");
            String img = paramJson.getString("img");

            String title = paramJson.getString("title");
            String content = paramJson.getString("content");
            String type = paramJson.getString("type");
            String disporder = paramJson.getString("disporder");

            //获取当前时间戳
            Timestamp ts = new Timestamp(new Date().getTime());
            long timestamp = ((ts.getTime())/1000);
            String createtime = daySdf.format(ts);

            //保存接口
            String s = "begintime:"+begintime+",content:"+content+",createtime:"+createtime
                    +",endtime:"+endtime+",secret:"+secret;

            String sign = Md5Util.EncoderByMd5(s);
            String urlStr = StaticEntity.staticUrl+"/lobby/cms_update_banner";
            Map<String, Object> map = new HashMap<String, Object>();
            if(begintime != null){
                map.put("begintime", begintime);
            }
            if(endtime != null){
                map.put("endtime", endtime);//过期时间
            }

            if(createtime != null){
                map.put("createtime", createtime);
            }
            if(title != null){
                map.put("title", title);
            }
            if(content != null){
                map.put("content", content);//内容
            }

            if(url != null){
                map.put("url", url);
            }

            if(img != null){
                map.put("img", img);
            }

            if(type != null){
                map.put("banner_type", type);
            }
            if(disporder != null){
                map.put("disporder", disporder);
            }
            getTypeCountryList(token);
            String country = "";
            if(null != StaticEntity.countryList && StaticEntity.countryList.size() > 0){
                for(CountryEntity c : StaticEntity.countryList){
                    if(c.getName().equals("CHINA")){
                        country = c.getCid();
                        continue;
                    }

                }

            }
            if(country != null && country != ""&& country.trim().length() > 0){
                map.put("areas", country);
            }

            map.put("timestamp", timestamp);
            map.put("id", id);
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
            String urlStr = StaticEntity.staticUrl+"/lobby/cms_delete_banner";
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

    public String getTypeCountryList(String token){

        try {
            Timestamp ts = new Timestamp(new Date().getTime());
            long timestamp = ((ts.getTime())/1000);
            //获取国家列表
            String sign1 = Md5Util.EncoderByMd5("key:"+key+ ",nonce:"+nonce+",timestamp:"+timestamp+",token:"+token+",secret:"+secret);
            Map<String, Object> map2 = new HashMap<String, Object>();
            map2.put("key", key);
            map2.put("nonce", nonce);
            map2.put("timestamp", timestamp);
            map2.put("token", token);
            map2.put("sign", sign1);
            map2.put("debug",1);
            String urlStr1 = StaticEntity.staticUrl+"/lobby/cms_country_list";
            Map<String, String> headers2 = new HashMap<String, String>();
            String b =  URLConnectionUtil.doPost(urlStr1,headers2,map2);
            System.out.println(b);
            //查询列表数据
            JSONObject jsStr = JSONObject.parseObject(b);
            System.out.print(jsStr);
            JSONArray myJsonArray = jsStr.getJSONArray("data");
            List<CountryEntity> list = (List<CountryEntity>) JSONArray.parseArray(jsStr.getString("data"), CountryEntity.class);
            StaticEntity.countryList = list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "gift/add";
    }
}
