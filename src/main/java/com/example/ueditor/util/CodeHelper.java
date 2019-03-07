package com.example.ueditor.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;


/**
 * 类名称：CodeHelper
 * 类描述：   生成id及code工具
 * mhc
 */
public class CodeHelper {
    /**
     * 生成UUID
     *
     * @return
     */
    public static String createUUID() {
        return String.valueOf(UUID.randomUUID()).replaceAll("-", "");
    }

    /**
     *
     */
    public static String createYzm(int number) {
        String pwd = "";
        for (int i = 0; i < number; i++) {
            double dblR = Math.random() * 10;
            int intR = (int) Math.floor(dblR);
            pwd += intR;
        }
        return pwd;
    }


    /**
     * 得到唯一平台编码
     *
     * @return
     */
    public synchronized static String getPreformCode() {
        int sjs = new Random().nextInt(900000) + 100000;
        return System.currentTimeMillis() + String.valueOf(sjs);
    }

}
