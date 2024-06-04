package com.dailype.dailypetask.controllers;

import com.dailype.dailypetask.entity.User;
import com.dailype.dailypetask.model.UserDto;
import com.dailype.dailypetask.services.UserService;
import jakarta.validation.Valid;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@Slf4j
public class UserController {
    
    
    @Autowired
    UserService userService;

    @PostMapping("/create_user")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto){
        String userId=userService.createUser(userDto);
        HashMap<String,Object> response=new HashMap<>();
        response.put("user_id",userId);
        response.put("message","User Created Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    } 

}
