package com.example.ueditor.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import static java.net.URLEncoder.encode;

/**
 */
public class ResponseUtil {

    public static String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    public static String CONTENT_TYPE_XML = "text/xml;charset=UTF-8";

    public static String CONTENT_TYPE_TEXT = "text/plain;charset=UTF-8";

    public static String CONTENT_TYPE_HTML = "text/html;charset=UTF-8";

    public static String CONTENT_TYPE_TEXT_JSON = "text/json;charset=UTF-8";


    public static void sendJsonNoCache(HttpServletResponse response, String message) {
        response.setContentType(CONTENT_TYPE_JSON);
        setNoCacheHeader(response);
        sendMessage(response, message);
        logger.info("sendJsonNoCache::" + message);
    }

    private static Logger logger = LoggerFactory.getLogger(ResponseUtil.class);


    public static void sendCodeJson(HttpServletResponse response, String message) {
        response.setContentType(CONTENT_TYPE_JSON);
        setNoCacheHeader(response);
        logger.info("sendTextNoCache::" + message);
        sendMessage(response, message);
    }
    public static void sendTextNoCache(HttpServletResponse response, String message) {
        response.setContentType(CONTENT_TYPE_JSON);
        setNoCacheHeader(response);
        logger.info("sendTextNoCache::" + message);
        sendMessage(response, message);
    }


    public static void sendFailMessage(HttpServletRequest request, HttpServletResponse response, String errorcode, String key, String... args) {
        JSONObject json = new JSONObject();
        json.put("success", 0);
        json.put("msg", new RequestContext(request).getMessage(key, args));
        json.put("errorcode", errorcode);
        sendTextNoCache(response, json.toJSONString());
    }

    public static void sendFailMessage(HttpServletRequest request, HttpServletResponse response, String errorcode, String key, int status, String... args) {
        JSONObject json = new JSONObject();
        json.put("success", 0);
        json.put("status", status);
        json.put("msg", new RequestContext(request).getMessage(key, args));
        json.put("errorcode", errorcode);
        sendTextNoCache(response, json.toJSONString());
    }

    public static void sendFailMessage(HttpServletRequest request, HttpServletResponse response, String errorcode, String msg) {
        JSONObject json = new JSONObject();
        json.put("success", 0);
        json.put("msg", msg);
        json.put("errorcode", errorcode);
        sendTextNoCache(response, json.toJSONString());
    }

    public static void sendSuccessMessage(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        json.put("success", 1);
        ResponseUtil.sendTextNoCache(response, json.toJSONString());
        return;
    }

    public static void sendJsonpNoCache(HttpServletResponse response, String callback, String message) throws IOException {
        response.setContentType(CONTENT_TYPE_JSON);
        setNoCacheHeader(response);
        sendMessage(response, callback + "(" + message + ")");
    }


    public static void sendMessageNoCache(HttpServletResponse response, String message, String contentType) throws IOException {
        response.setContentType(contentType);
        setNoCacheHeader(response);
        sendMessage(response, message);
    }


    public static void sendMessageNoCache(HttpServletRequest req, HttpServletResponse response, String message) throws IOException {
        String fmt = RequestUtils.getFmt(req);
        if (fmt.equals("json")) {
            sendJsonNoCache(response, message);
        } else if (fmt.equals("jsonp")) {
            sendJsonpNoCache(response, RequestUtils.getJSONPCallback(req), message);
        } else if (fmt.equals("xml")) {
            sendMessageNoCache(response, "not support yet!!!", ResponseUtil.CONTENT_TYPE_XML);
        }
    }


    public static void sendMessageWithCache(HttpServletResponse response, String message, String contentType, int cacheSec) throws IOException {
        response.setContentType(contentType);
        setCacheHeader(response, cacheSec);
        sendMessage(response, message);
    }


    private static void sendMessage(HttpServletResponse response, String message) {
        StringBuilder sb = new StringBuilder();
        //response.setContentLength(message.getBytes("utf-8").length);
        try {
            java.io.PrintWriter writer = response.getWriter();
            sb.append(message);
            writer.write(sb.toString());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置cache header
     *
     * @param response
     * @param cacheSec
     */
    public static void setCacheHeader(HttpServletResponse response, int cacheSec) {
        // HTTP 1.0
        response.setHeader("Pragma", "Public");
        // HTTP 1.1
        response.setHeader("Cache-Control", "max-age=" + cacheSec);
        response.setDateHeader("Expires", System.currentTimeMillis() + cacheSec * 1000L);
    }


    /**
     * 设置no-cache header
     *
     * @param response
     */
    public static void setNoCacheHeader(HttpServletResponse response) {
        // HTTP 1.0
        response.setHeader("Pragma", "No-cache");
        // HTTP 1.1
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
    }


    public static String addParam(String url, String paramValuePair) {
        return url + (url.contains("?") ? "&" : "?") + paramValuePair;
    }


    public static String addParam(String url, JSONObject paramValuePair) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : paramValuePair.entrySet()) {
            if (sb.length() > 0) sb.append('&');
            try {
                sb.append(encode(entry.getKey(), "UTF-8")).append('=').append(encode(entry.getValue().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return url + (url.contains("?") ? "&" : "?") + sb;
    }
}
