package com.example.ueditor.entity;

import lombok.Data;

@Data
public class VersionEntity {
    private String id;
    private String platform;//平台id
    private String ver;//版本
    private String is_force;//是否强制更新
    private String ver_lst;//适用版本
    private String prod_name;//产品名称
    private String bundle_id;//包名
    private String apple_id;//苹果包名
    private String instructions;//更新内容
    private String url;//更新地址
    private String create_time;//创建时间
}
