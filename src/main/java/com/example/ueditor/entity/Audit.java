package com.example.ueditor.entity;

import lombok.Data;

@Data
public class Audit {
    private String id;
    private String uid;
    private String update_time;
    private boolean  flag;
    private String create_time;
    private String bundle_id;
    private String audit_version;
    private String nick;

}
