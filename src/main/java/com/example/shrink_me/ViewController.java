package com.example.shrink_me;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping({ "/", "/app", "/index" })
    public String index() {
        return "forward:/index.html";
    }
}
