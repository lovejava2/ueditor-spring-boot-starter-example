package com.example.ueditor.entity;

import lombok.Data;

@Data
public class OperateEntity {
    private String id;
    private String title;//名称
    private String category;//类别
    private String url;
    private String img;//图片地址
    private String begintime;
    private String endtime;
    private String disporder;//显示序号
    private String enabled;//是否启用
    private String areas;//区域
    private String roomid;//房间id todo 做什么用
    private String banner_type;

}
