//package com.example.ueditor.util;
//
//
//import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTPReply;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//
///**
// * FtpUtil工具类 ftp文件上传类
// *
// * @Author 徐敏杰
// * @Date 2018-12-21
// */
//@Component
//public class FtpUtil {
//    //ftp服务器ip地址
//    private static final String FTP_ADDRESS = "192.168.31.41";
//    //端口号
//    private static final int FTP_PORT = 21;
//    //用户名
//    private static final String FTP_USERNAME = "xmj";
//    //密码
//    private static final String FTP_PASSWORD = "laolao521";
//    //图片路径
//    public final String FTP_BASEPATH = "/home/ftp/develop/";
//    public boolean uploadFile(String originFileName, InputStream input) {
//        boolean success = false;
//        FTPClient ftp = new FTPClient();
//        ftp.setControlEncoding("GBK");
//        try {
//            int reply;
//            ftp.connect(FTP_ADDRESS, FTP_PORT);// 连接FTP服务器
//            ftp.login(FTP_USERNAME, FTP_PASSWORD);// 登录
//            reply = ftp.getReplyCode();
//            if (!FTPReply.isPositiveCompletion(reply)) {
//                ftp.disconnect();
//                return success;
//            }
//            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
//            ftp.makeDirectory(FTP_BASEPATH);
//            ftp.changeWorkingDirectory(FTP_BASEPATH);
//            ftp.storeFile(originFileName, input);
//            input.close();
//            ftp.logout();
//            success = true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (ftp.isConnected()) {
//                try {
//                    ftp.disconnect();
//                } catch (IOException ioe) {
//                }
//            }
//        }
//        return success;
//    }
//
//}
//
