package com.manufacturing.controller;
 
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
 
@Controller
public class ForwardController {

    @RequestMapping(value = {
        "/", "/login", "/register", "/orders", "/home",
        "/{path:[^\\.]*}", "/**/{path:[^\\.]*}"
    })
    public String forward() {
        return "forward:/index.html";
    }
}