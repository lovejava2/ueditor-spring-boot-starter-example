package com.example.ueditor.entity;

import lombok.Data;

@Data
public class NoticeEntity {
    private String id;
    private String title;
    private String content;
    private String url;//公共跳转地址
    private String showarea;//显示位置
    private String begintime;//开始时间
    private String endtime;//结束
    private String status;//发送状态
    private String isdeleted;//是否已删除
    private String createtime;//创建时间
}
