package com.manufacturing.controller;
 
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
 
@Controller
public class IndexController {
 
    @RequestMapping(value = {
        "/", "/login","/register","/orders"
    })
    public String forward() {
        return "forward:/index.html";
    }
}