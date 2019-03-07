package com.example.ueditor.util;


import com.example.ueditor.entity.StaticEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class URLConnectionUtil {
    private final static Logger logger = LoggerFactory.getLogger(URLConnectionUtil.class);

    private static final String SERVLET_POST = "POST";
    private static final String SERVLET_GET = "GET";
    private static final String SERVLET_DELETE = "DELETE";
    private static final String SERVLET_PUT = "PUT";


    public static String request(String url, Map headers, String params, String method) {
        String result = "";
        try {
            if (SERVLET_GET.equalsIgnoreCase(method)) {
                result = doGet(url, headers, null);
            }
            if (SERVLET_POST.equalsIgnoreCase(method)) {
                result = doPost(url, headers, params);
            }
            if (SERVLET_DELETE.equalsIgnoreCase(method)) {
                result = doDelete(url, headers);
            }
            if (SERVLET_PUT.equalsIgnoreCase(method)) {
                result = doPut(url, headers, params);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;

    }

    public static String request(String url, Map headers, Map params, String method) {
        String result = "";
        try {
            if (SERVLET_GET.equalsIgnoreCase(method)) {
                result = doGet(url, headers, null);
            }
            if (SERVLET_POST.equalsIgnoreCase(method)) {
                result = doPost(url, headers, params);
            }
            if (SERVLET_DELETE.equalsIgnoreCase(method)) {
                result = doDelete(url, headers);
            }
            if (SERVLET_PUT.equalsIgnoreCase(method)) {
                result = doPut(url, headers, params);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;

    }

    /**
     * 表单格式提交数据
     * @param url 请求url
     * @param headers 头部数据
     * @param params 参数
     * @param method 暂时只提供POST方式
     * @param contantType  Form格式提交数据
     * @return
     */
    public static String request(String url, Map headers, Map params, String method, String contantType) {
        String result = "";
        try {
            if (SERVLET_POST.equalsIgnoreCase(method)) {
                result = doPost(url, headers, params, contantType);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }





    private static String prepareParam(Map<String, Object> paramMap) {
        StringBuffer sb = new StringBuffer();
        if (paramMap == null || paramMap.isEmpty()) {
            return "";
        } else {
            for (String key : paramMap.keySet()) {
                String value = paramMap.get(key).toString();
                if (sb.length() < 1) {
                    sb.append(key).append("=").append(value);
                } else {
                    sb.append("&").append(key).append("=").append(value);
                }
            }
            return sb.toString();
        }
    }

    public static String doPost(String urlStr, Map<String, String> headers, Map<String, Object> paramMap) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(SERVLET_POST);
        String paramStr = prepareParam(paramMap);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        if (headers != null) {
            for (String key : headers.keySet()) {
                conn.setRequestProperty(key, headers.get(key));
            }
        }
        OutputStream os = conn.getOutputStream();
        os.write(paramStr.toString().getBytes("utf-8"));
        os.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        String result = "";
        while ((line = br.readLine()) != null) {
            result += "" + line;
        }
        br.close();
        return result;

    }

    public static String doPost(String urlStr, Map<String, String> headers, String params) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(SERVLET_POST);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        if (headers != null) {
            for (String key : headers.keySet()) {
                conn.setRequestProperty(key, headers.get(key));
            }
        }
        OutputStream os = conn.getOutputStream();
        os.write(params.toString().getBytes("utf-8"));
        os.close();

        String result = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            result += "" + line;
        }
        //haier返回token
        String accessToken = conn.getHeaderField("accessToken");
        if (accessToken != null && result.endsWith("}")){
            result = result.substring(0,result.length() - 1) + ",\"accessToken\":\"" + accessToken + "\"}";
        }
        br.close();

        //当不是200、201、202时,打印错误信息。需要时再获取
        /*if (conn.getResponseCode() == HttpURLConnection.HTTP_FORBIDDEN){
            BufferedReader errorBufReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            String errorLine;
            while ((errorLine = errorBufReader.readLine()) != null) {
                result += "" + errorLine;
            }
            errorBufReader.close();
        }*/

        //System.out.println(result);
        return result;

    }

    public static String doGet(String urlStr, Map<String, String> headers, Map<String, Object> paramMap) throws Exception {
        String paramStr = prepareParam(paramMap);
        if (paramStr == null || paramStr.trim().length() < 1) {

        } else {
            urlStr += "?" + paramStr;
        }
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(SERVLET_GET);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "GBK");
        if (headers != null) {
            for (String key : headers.keySet()) {
                conn.setRequestProperty(key, headers.get(key));
            }
        }

        conn.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GBK"));
        String line;
        String result = "";
        while ((line = br.readLine()) != null) {
            result += "" + line;
        }
        br.close();
        return result;
    }


    public static String doPut(String urlStr, Map<String, String> headers, Map<String, Object> paramMap) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(SERVLET_PUT);
        String paramStr = prepareParam(paramMap);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        if (headers != null) {
            for (String key : headers.keySet()) {
                conn.setRequestProperty(key, headers.get(key));
            }
        }
        OutputStream os = conn.getOutputStream();
        os.write(paramStr.toString().getBytes("utf-8"));
        os.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        String result = "";
        while ((line = br.readLine()) != null) {
            result += "" + line;
        }
        br.close();
        return result;

    }

    public static String doPut(String urlStr, Map<String, String> headers, String params) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(SERVLET_PUT);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        if (headers != null) {
            for (String key : headers.keySet()) {
                conn.setRequestProperty(key, headers.get(key));
            }
        }
        OutputStream os = conn.getOutputStream();
        os.write(params.toString().getBytes("utf-8"));
        os.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        String result = "";
        while ((line = br.readLine()) != null) {
            result += "" + line;
        }
        br.close();
        return result;

    }

    public static String doDelete(String urlStr, Map<String, String> headers) throws Exception {

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod(SERVLET_DELETE);
        if (headers != null) {
            for (String key : headers.keySet()) {
                conn.setRequestProperty(key, headers.get(key));
            }
        }
        //屏蔽掉的代码是错误的，java.net.ProtocolException: HTTP method DELETE doesn't support output
/*		OutputStream os = conn.getOutputStream();
        os.write(paramStr.toString().getBytes("utf-8"));
		os.close();  */

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        String result = "";
        while ((line = br.readLine()) != null) {
            result += "" + line;
        }
        br.close();
        return result;
    }

    public static String doPost(String urlStr, Map<String, String> headers, Map<String, Object> params, String contentType) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        if (headers != null) {
            for (String key : headers.keySet()) {
                conn.setRequestProperty(key, headers.get(key));
            }
        }

        StringBuilder sb = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                try {
                    sb.append(entry.getKey())
                            .append("=")
                            .append(URLEncoder.encode(String.valueOf(entry.getValue()), "utf-8"))
                            .append("&");
                } catch (UnsupportedEncodingException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            // 删掉最后一个 & 字符
            sb.deleteCharAt(sb.length() - 1);
        }
        OutputStream os = conn.getOutputStream();
        os.write(sb.toString().getBytes("utf-8"));
        os.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        String result = "";
        while ((line = br.readLine()) != null) {
            result += "" + line;
        }
        br.close();
        return result;
    }

    public static void main(String[] args) throws Exception {
        String code = "0615rAiL0ivQW52lyekL0XsGiL05rAi5";
        String key = "horoscope";
        String nonce = "3876662232";
        String pwd = Md5Util.string2MD5("123456");
//        String pwd = "123456";

        String secret = "WY#Bzd!91";
        //获取当前时间戳
        Timestamp ts = new Timestamp(new Date().getTime());
        long timestamp = ((ts.getTime())/1000);
        String uname = "ErrishRoxas";
//        long timestamp = new Date().getTime();
        String sign = Md5Util.string2MD5("code:"+code+",key:"+key+",nonce:"+nonce+",pwd:"+pwd+",timestamp:"+timestamp+",uname:"+uname+",secret:WY#Bzd!91");

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
//		URLConnectionUtil.doPost(urlStr, map);
//		URLConnectionUtil.doPut(urlStr, map);
//        URLConnectionUtil.doDelete(urlStr,null, map);

    }


}
