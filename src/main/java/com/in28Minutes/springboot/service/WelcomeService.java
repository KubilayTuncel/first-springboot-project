package com.in28Minutes.springboot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Component
public class WelcomeService {

    @Value("${welcome.message}")
    private String welcomeMessage;

        @RequestMapping("/welcome")
        public String retrieveWelcomeMessage(){
            return welcomeMessage;
        }
}
