package com.dailype.dailypetask.controllers;

import com.dailype.dailypetask.services.UserSecuredService;
import com.dailype.dailypetask.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @Autowired
    UserService userService;
    @GetMapping("/")
    public ResponseEntity<Object> home(){
        return new ResponseEntity<>("Welcome to DailyPe , Server is working  !!", HttpStatus.OK);
    }
    @GetMapping("/test1")
    public ResponseEntity<Object> test1(){
        log.info("server test1 working !!");
        return new ResponseEntity<>("server test1 working !!", HttpStatus.OK);
    }

    @GetMapping("/test2")
    public ResponseEntity<Object> test2(){
        log.info("server test1 working !!");
        return new ResponseEntity<>("server test2 working !!", HttpStatus.OK);
    }

    @GetMapping("/test/{userId}")
    public ResponseEntity<Object> getUserTest(@PathVariable String userId){
        return new ResponseEntity<>(userService.findUserforTest(userId),HttpStatus.OK);
    }
}
