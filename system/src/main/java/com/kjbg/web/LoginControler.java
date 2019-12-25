package com.kjbg.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginControler {
    @GetMapping("/user/login")
    public String login(){
        return "";
    }

}
