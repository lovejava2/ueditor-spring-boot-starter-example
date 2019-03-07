
package com.example.ueditor.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//import org.apache.commons.beanutils.BeanUtils;


public class RequestUtils {

    public static <T> T convertWith(HttpServletRequest req, Class<T> clazz, List<String> properties) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        T obj = clazz.newInstance();
        Map<String, String[]> propertiesMap = new HashMap<String, String[]>();
        for (String property : properties) {
            String beanName;
            String paramName;
            if (property.contains(":")) {
                String[] split = property.split(":", 2);
                beanName = split[0];
                paramName = split[1];
            } else {
                beanName = property;
                paramName = property;
            }
            propertiesMap.put(beanName, req.getParameterValues(paramName));
        }
//        BeanUtils.populate(obj, propertiesMap);
        return obj;
    }









    /**
     * 取客户端ip地址
     *
     * @param request
     * @return
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (notFound(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (notFound(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (notFound(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }



    private static boolean notFound(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "0.0.0.0".equals(ip);
    }



    public static String getRequestParams(HttpServletRequest req, List<String> exceptParams) {
        StringBuilder strBuffer = new StringBuilder();
        Enumeration parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {

            if (strBuffer.length() > 0) {
                strBuffer.append("&");
            }

            String name = (String) parameterNames.nextElement();

            if (exceptParams.contains(name)) continue;

            String value = req.getParameter(name);

            strBuffer.append(name).append("=").append(value);
        }
        return strBuffer.toString();
    }



    public static Map<String, String> getRequestParamMap(HttpServletRequest req, List<String> exceptParams) {
        return getRequestParamMap(req, exceptParams, false);
    }



    public static Map<String, String> getRequestParamMap(HttpServletRequest req, List<String> exceptParams, boolean filterEmpty) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Enumeration parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = (String) parameterNames.nextElement();
            if (exceptParams.contains(name)) continue;
            String value = req.getParameter(name);
            if (filterEmpty && StringUtil.isEmpty(value)) {
                continue;
            }
            paramMap.put(name, value);
        }
        return paramMap;
    }



    public static String toParam(Map<String, String> params, String encode) throws UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            if (encode == null) {
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
            } else {
                stringBuilder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encode));
            }

        }
        return stringBuilder.toString();
    }



    public static Map parseQuery(HttpServletRequest req) throws ParseException {
        Map<String, String> requestParamMap = getRequestParamMap(req, Collections.<String>emptyList(), true);
        JSONObject query = new JSONObject();

        String q_native = req.getParameter("q_native");
        if (StringUtil.notEmpty(q_native)) {
            return JSONObject.parseObject(q_native);
        }


        for (Map.Entry<String, String> entry : requestParamMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            //q_n_eq_name=xxxx
            String[] split = key.split("_");
            if (split.length != 4 || !split[0].equalsIgnoreCase("q")) {
                continue;
            }

            query.put(split[3], filedLimit(split[2], getValue(split[1], value)));
        }
        return query;
    }



    public static Map parseField(HttpServletRequest req) throws ParseException {
        String q_f = req.getParameter("q_f");
        if (StringUtil.notEmpty(q_f)) {
            String[] split = q_f.split(",");
            Map<String, Integer> field = new HashMap<String, Integer>();
            for (String s : split) {
                field.put(s, 1);
            }
            return field;
        } else {
            return null;
        }
    }



    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");



    static public Object getValue(String typeKey, String value) throws ParseException {
        if (typeKey.equals("s")) {
            return value;
        }

        if (typeKey.equals("n")) {
            return Long.parseLong(value);
        }

        if (typeKey.equals("b")) {
            return Boolean.parseBoolean(value);
        }

        if (typeKey.equals("d")) {
            return dateFormat.parse(value).getTime();
        }

        throw new RuntimeException("type key [" + typeKey + "] not support");
    }



    public static Object filedLimit(String limitKey, Object value) throws ParseException {
        if (limitKey.equals("eq")) {
            return value;
        }

        if (limitKey.equals("ne")) {
            return MapUtil.getMap("$ne", value);
        }

        if (limitKey.equals("gt")) {
            return MapUtil.getMap("$gt", value);
        }

        if (limitKey.equals("gte")) {
            return MapUtil.getMap("$gte", value);
        }

        if (limitKey.equals("lt")) {
            return MapUtil.getMap("$lt", value);
        }

        if (limitKey.equals("lte")) {
            return MapUtil.getMap("$lte", value);
        }

        throw new RuntimeException("limit key [" + limitKey + "] not support");
    }



    public static Map<String, String> parseParam(String param) {
        Map<String, String> paramMap = new HashMap<String, String>();
        String[] split = param.split("&");
        for (String s : split) {
            String[] kv = s.split("=");
            if (kv.length == 2) {
                paramMap.put(kv[0], kv[1]);
            }
        }
        return paramMap;
    }



    public static String getJSONPCallback(HttpServletRequest req) {
        String jsonp = req.getParameter("callback");
        if (StringUtil.notEmpty(jsonp)) {
            return jsonp;
        }
        return "callback";
    }



    public static String getRedirect(HttpServletRequest req) {
        String jsonp = req.getParameter("redirect");
        if (StringUtil.notEmpty(jsonp)) {
            return jsonp;
        }
        return null;
    }



    public static String getFmt(HttpServletRequest req) {
        String fmt = req.getParameter("fmt");
        if (StringUtil.notEmpty(fmt)) {
            return fmt;
        }
        return "json";
    }



    
}
