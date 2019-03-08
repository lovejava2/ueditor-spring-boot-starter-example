package com.example.ueditor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.ueditor.entity.GiftEntity;
import com.example.ueditor.entity.GiftTypeEntity;
import com.example.ueditor.entity.StaticEntity;
import com.example.ueditor.util.*;
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
@RequestMapping("gift")
public class GiftController {

    private String key = StaticEntity.key;
    private String nonce = StaticEntity.nonce;
    private String secret = StaticEntity.secret;

    @RequestMapping("/toList")
    public String toList(HttpServletRequest request, HttpServletResponse response){
        return "gift/list";
    }

    @RequestMapping("/getList")//获取礼物列表信息
    public void getList(HttpServletRequest request,HttpServletResponse response){
        String params = HTTPUtils.getRequestParams(request);
        JSONObject result = new JSONObject();
        try {
            Timestamp ts = new Timestamp(new Date().getTime());
            long timestamp = ((ts.getTime())/1000);
            JSONObject paramJson = JSON.parseObject(params);
            String token = paramJson.getString("token");
            String keyword = paramJson.getString("keyword");
            String gifttype = paramJson.getString("gifttype");
            Integer pageindex = paramJson.getInteger("page") - 1;
            Integer pagesize = paramJson.getInteger("size");
//            String searchTs = paramJson.getString("ts");//查询创建时间
//            String searchNick = paramJson.getString("nick");//查询创建用户
            Map<String, Object> map2 = new HashMap<String, Object>();
            String sign = "";
            if(gifttype != null){
                sign = "gifttype:"+gifttype+",";
                map2.put("gifttype", gifttype);
            }
            if(null != keyword && keyword.length() > 0){
                sign = "keyword:"+keyword+",";
                map2.put("keyword", keyword);
            }

            //获取列表
            String sign1 = Md5Util.EncoderByMd5(sign+"key:"+key+ ",nonce:"+nonce+",pageindex:"+pageindex
                    +",pagesize:"+pagesize+",timestamp:"+timestamp+",token:"+token+",secret:"+secret);
            String urlStr1 = StaticEntity.staticUrl+"/lobby/cms_search_gift";

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
            List<GiftEntity> list = (List<GiftEntity>) JSONArray.parseArray(jsStr.getString("data"), GiftEntity.class);
            if(null != list && list.size() > 0){
                getType(token);
                for(GiftEntity g:list){
                    for(GiftTypeEntity t:StaticEntity.giftTypeList){
                        if(g.getGift_type().equals(t.getTypeid())){
                            g.setType_name(t.getType_name());
                            continue;
                        }

                    }

                }

            }
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
        String params = HTTPUtils.getRequestParams(request);
        JSONObject paramJson = JSON.parseObject(params);
        String token = paramJson.getString("token");
        getType(token);
        modelMap.put("typeList",StaticEntity.giftTypeList);
        return "gift/add";
    }

    /**
     *
     * @param request
     * @param response
     */
    @RequestMapping("/save")
    public void save(HttpServletRequest request,HttpServletResponse response){
        String params = HTTPUtils.getPostParams(request);
        JSONObject result = new JSONObject();
        try {
            JSONObject paramJson = JSON.parseObject(params);
            String gname = paramJson.getString("gname");
            String gift_type = paramJson.getString("gift_type");
            String icon = paramJson.getString("icon");
            String price = paramJson.getString("price");
            String Anieffectaddress = paramJson.getString("Anieffectaddress");
            Integer Anieffecttimes = paramJson.getInteger("Anieffecttimes");//动态礼物时长
            String exp = paramJson.getString("exp");
            String anchor_exp = paramJson.getString("anchor_exp");
            String anchor_getgold = paramJson.getString("anchor_getgold");
            String user_getgold = paramJson.getString("user_getgold");
            String sys_getgold = paramJson.getString("sys_getgold");
            String pos = paramJson.getString("pos");
            String country = paramJson.getString("country");
            String showarea = paramJson.getString("showarea");
             Integer visible = paramJson.getInteger("visible");
            String remark = paramJson.getString("remark");
            String token = paramJson.getString("token");
            Date ptm = paramJson.getDate("tm");

            //将内容写到文本中
            //获取当前时间戳
            Timestamp ts = new Timestamp(new Date().getTime());
            long timestamp = ((ts.getTime())/1000);

            //保存接口
//            String s = "Anieffectaddress:"+Anieffectaddress+",anchor_exp:"+anchor_exp+",anchor_getgold:"+anchor_getgold
//                    +",country:"+country+"exp"+exp+",gift_type:"+gift_type+",gname:"+gname+",icon:"+icon
//                    +",price:"+price+",pos:"+pos+",remark:"+remark+",showarea:"+showarea+",sys_getgold:"+sys_getgold
//                    +",timestamp:"+timestamp+",token:"+token
//                    +",user_getgold:"+user_getgold+",visible:"+visible+",secret:"+secret;

            String s = "Anieffecttimes"+Anieffecttimes+",Anieffectaddress:"+Anieffectaddress+",anchor_exp:"+anchor_exp+",anchor_getgold:"+anchor_getgold
                    +",country:"+country+"exp"+exp+",gift_type:"+gift_type+",gname:"+gname+",icon:"+icon
                    +",price:"+price+",pos:"+pos+",remark:"+remark+",showarea:"+showarea+",sys_getgold:"+sys_getgold
                    +",timestamp:"+timestamp+",token:"+token
                    +",user_getgold:"+user_getgold+",visible:"+visible+",secret:"+secret;

            String sign = Md5Util.EncoderByMd5(s);
            String urlStr = StaticEntity.staticUrl+"/lobby/cms_add_gift";
            Map<String, Object> map = new HashMap<String, Object>();
            if(Anieffecttimes != null){
                map.put("Anieffecttimes",Anieffecttimes);
            }
            if(Anieffectaddress != null){
                map.put("Anieffectaddress", Anieffectaddress);
            }
            if(anchor_exp != null){
                map.put("anchor_exp", anchor_exp);
            }
            if(anchor_getgold != null){
                map.put("anchor_getgold", anchor_getgold);
            }
            if(country != null){
                map.put("country", country);
            }
            if(exp != null){
                map.put("exp", exp);
            }
            if(gift_type != null){
                map.put("gift_type", gift_type);
            }
            if(gname != null){
                map.put("gname", gname);
            }
            if(icon != null){
                map.put("icon", icon);
            }
            if(price != null){
                map.put("price", price);
            }
            if(pos != null){
                map.put("pos", pos);
            }
            if(remark != null){
                map.put("remark", remark);
            }
            if(showarea != null){
                map.put("showarea", showarea);
            }
            if(sys_getgold != null){
                map.put("sys_getgold", sys_getgold);
            }
            if(user_getgold != null){
                map.put("user_getgold", user_getgold);
            }
            if(visible != null){
                map.put("visible", visible);
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
        String params = HTTPUtils.getRequestParams(request);
        JSONObject paramJson = JSON.parseObject(params);
        String token = paramJson.getString("token");
        getType(token);
        modelMap.put("typeList",StaticEntity.giftTypeList);
        return "gift/edit";
    }

    //修改update
    @RequestMapping("/update")
    public void update(HttpServletRequest request,HttpServletResponse response){
        String params = HTTPUtils.getPostParams(request);
        JSONObject result = new JSONObject();
        try {
            JSONObject paramJson = JSON.parseObject(params);
            String gid = paramJson.getString("gid");
            String token = paramJson.getString("token");
            Integer visible = paramJson.getInteger("visible");

            String gname = paramJson.getString("gname");
            String gift_type = paramJson.getString("gift_type");
            String icon = paramJson.getString("icon");
            String price = paramJson.getString("price");
            String Anieffectaddress = paramJson.getString("Anieffectaddress");
            String exp = paramJson.getString("exp");
            String anchor_exp = paramJson.getString("anchor_exp");
            String anchor_getgold = paramJson.getString("anchor_getgold");
            String user_getgold = paramJson.getString("user_getgold");
            String sys_getgold = paramJson.getString("sys_getgold");
            String pos = paramJson.getString("pos");
            String country = paramJson.getString("country");
            String showarea = paramJson.getString("showarea");
            String remark = paramJson.getString("remark");



            //将内容写到文本中
            //获取当前时间戳
            Timestamp ts = new Timestamp(new Date().getTime());
            long timestamp = ((ts.getTime())/1000);

            //保存接口
            String s = "gid:"+gid+",token:"+token+",visible:"+visible+",secret:"+secret;

            String sign = Md5Util.EncoderByMd5(s);
            String urlStr = StaticEntity.staticUrl+"/lobby/cms_update_gift";
            Map<String, Object> map = new HashMap<String, Object>();

            if(Anieffectaddress != null){
                map.put("Anieffectaddress", Anieffectaddress);
            }
            if(anchor_exp != null){
                map.put("anchor_exp", anchor_exp);
            }
            if(anchor_getgold != null){
                map.put("anchor_getgold", anchor_getgold);
            }
            if(country != null){
                map.put("country", country);
            }
            if(exp != null){
                map.put("exp", exp);
            }
            if(gift_type != null){
                map.put("gift_type", gift_type);
            }
            if(gname != null){
                map.put("gname", gname);
            }
            if(icon != null){
                map.put("icon", icon);
            }
            if(price != null){
                map.put("price", price);
            }
            if(pos != null){
                map.put("pos", pos);
            }
            if(remark != null){
                map.put("remark", remark);
            }
            if(showarea != null){
                map.put("showarea", showarea);
            }
            if(sys_getgold != null){
                map.put("sys_getgold", sys_getgold);
            }
            if(user_getgold != null){
                map.put("user_getgold", user_getgold);
            }
            if(visible != null){
                map.put("visible", visible);
            }

            map.put("gid", gid);
            map.put("timestamp", timestamp);
            map.put("token", token);
            map.put("key", key);
            map.put("nonce", nonce);
            map.put("sign", sign);
            map.put("debug",1);
            System.out.print(map.toString());
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
            String gid = paramJson.getString("gid");
            String token = paramJson.getString("token");
            //获取当前时间戳
            Timestamp ts = new Timestamp(new Date().getTime());
            long timestamp = ((ts.getTime())/1000);

            String s = "gid:"+gid+",key:"+key+",nonce:"+nonce+
                    ",timestamp:"+timestamp+",token:"+token+",secret:"+secret;
            String sign = Md5Util.EncoderByMd5(s);
            String urlStr = StaticEntity.staticUrl+"/lobby/cms_delete_gift";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("gid", gid);
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


    public String getType(String token){

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
            String urlStr1 = StaticEntity.staticUrl+"/lobby/cms_gift_type_list";
            Map<String, String> headers2 = new HashMap<String, String>();
            String b =  URLConnectionUtil.doPost(urlStr1,headers2,map2);
            System.out.println(b);
            //查询列表数据
            JSONObject jsStr = JSONObject.parseObject(b);
            System.out.print(jsStr);
            JSONArray myJsonArray = jsStr.getJSONArray("data");
            List<GiftTypeEntity> list = (List<GiftTypeEntity>) JSONArray.parseArray(jsStr.getString("data"), GiftTypeEntity.class);
            StaticEntity.giftTypeList = list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "gift/add";
    }

}
