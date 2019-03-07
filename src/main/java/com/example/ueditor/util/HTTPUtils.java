package com.example.ueditor.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;

/**
 * .
 * User: Liupeizhi
 * Date: 15-7-7
 * Time: 上午9:34
 */
public class HTTPUtils {
    private static final Logger logger = LoggerFactory.getLogger(HTTPUtils.class);

    private static final String REQ_KEY = UUIDGen.generate();

    public static String getPostParams(HttpServletRequest request){

        String resp = "";
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream(), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            if (br != null )
            {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        if(StringUtil.isEmpty(sb.toString())){
            if(request.getAttribute(REQ_KEY)!=null){
                resp = request.getAttribute(REQ_KEY).toString();
            }
        }else{
            resp = sb.toString();
            request.setAttribute(REQ_KEY,resp);
        }
        return resp;
//        return getRequestParams(request);
    }

    public static String getRequestBody(HttpServletRequest request){
        if(request.getAttribute(REQ_KEY)!=null){
            return request.getAttribute(REQ_KEY).toString();
        } else{
            return null;
        }
    }


    /**
     * 获取get请求参数
     * @param request
     * @return
     */
    public static String getRequestParams(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        Map parameterMap = request.getParameterMap();
        if(parameterMap.size()==0){
            try {
                Object reqKey = request.getAttribute(REQ_KEY);
                if(reqKey == null){
                    return "";
                }
                String str_params = URLDecoder.decode(reqKey.toString(), "UTF-8");
                String[] params = str_params.split("&");
                if(params.length>0){
                    for(int i=0;i<params.length;i++){
                        String param = params[i];
                        String[] kv = param.split("=");
                        if(kv.length>1) {
                            if(kv[0].equals("limit")){
                                jsonObject.put("size",kv[1]);
                            }else{
                                jsonObject.put(kv[0],kv[1]);
                            }
                        }
                    }
                }
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }else {
            Iterator<Map.Entry<String, String[]>> it = parameterMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String[]> entry = it.next();
                if (entry.getKey().equalsIgnoreCase("limit")) { //bussiness-manager 分页自动添加start、limit
                    jsonObject.put("size", entry.getValue()[0]);
                } else {
                    jsonObject.put(entry.getKey(), entry.getValue()[0]);
                }
            }
        }
        return jsonObject.toJSONString();
    }


    public static String getRemortIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }
}
