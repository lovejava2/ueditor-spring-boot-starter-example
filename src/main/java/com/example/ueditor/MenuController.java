package com.example.ueditor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("menu")
public class MenuController {


    @RequestMapping("/toList")
    public String toList(HttpServletRequest request, HttpServletResponse response){
        return "menu/list";
    }
}
