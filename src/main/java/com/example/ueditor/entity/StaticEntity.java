package com.example.ueditor.entity;

import lombok.Data;

import java.util.List;

@Data
public class StaticEntity {
    public static String key = "horoscope";
    public static String nonce = "3876662232";
    public static String secret = "WY#Bzd!91";
//    public static String staticUrl = "https://www.xingzuo8888.com";
    public static String staticUrl = "https://www.xingzuo2018.com";
//    public static String staticFile = "https://www.xingzuo8888.com";
    public static String staticFile = "https://www.xingzuo2018.com/admin";
    public static String appendCon = "<p style=\"text-align: center;\">\n" +
            "<span style=\"box-sizing: border-box; font-weight: 700; font-family: &quot;Helvetica Neue&quot;, Helvetica, Tahoma, Arial, sans-serif; font-size: 14px; background-color: rgb(248, 250, 254); color: rgb(165, 165, 165);\">免责声明：本文来自星座吧官网自媒体，不代表星座吧的观点和立场。</span>\n" +
            "</p>";

    public static List<GiftTypeEntity> giftTypeList;

    public static List<CountryEntity> countryList;


}
