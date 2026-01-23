package com.rinar.trasactional_mgmt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String securedEndpoint(){
        return "JWT is working fine, authenticated";
    }
}
