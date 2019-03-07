package com.example.ueditor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("dept")
public class DeptController {

    @RequestMapping("/toList")
    public String toList(HttpServletRequest request, HttpServletResponse response){
        return "dept/list";
    }

}
