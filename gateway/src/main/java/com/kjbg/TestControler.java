package com.kjbg;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestControler {
    @GetMapping("/test")
    public String v() {
        return "ddd";
    }
}
