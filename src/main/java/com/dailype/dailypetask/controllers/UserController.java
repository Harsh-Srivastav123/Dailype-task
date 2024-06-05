package com.dailype.dailypetask.controllers;

import com.dailype.dailypetask.exceptions.BadRequestException;
import com.dailype.dailypetask.model.RequestPayload;
import com.dailype.dailypetask.model.UserDto;
import com.dailype.dailypetask.services.UserService;
import jakarta.validation.Valid;
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

    @PostMapping("/get_user")
    public ResponseEntity<Object> getUser(@RequestBody RequestPayload requestPayload){
        String user_id=requestPayload.getUserId();
        String mob_num=requestPayload.getMobNum();
        String manager_id= requestPayload.getManagerId();
        if(user_id==null && manager_id==null && mob_num==null){
            throw  new BadRequestException("Please enter the user_id, mob_num and manager_id correctly. !!");
        }
        if(manager_id!=null){
            return new ResponseEntity<>(userService.getUserWithManagerId(manager_id),HttpStatus.OK);
        }
        else if(mob_num!=null){
            return new ResponseEntity<>(userService.getUserByMobNum(mob_num),HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(userService.getUserById(user_id),HttpStatus.OK);
        }
    }

    @PostMapping("/delete_user")
    public ResponseEntity<Object> deleteUser(@RequestBody RequestPayload requestPayload){
        if(requestPayload.getUserId()==null && requestPayload.getMobNum()==null){
            throw  new BadRequestException("Please enter the user_id, mob_num correctly. !!");
        }
        String user_id=requestPayload.getUserId();
        String mob_num=requestPayload.getMobNum();
        String responseId=null;
        if(user_id!=null){
            responseId=userService.deleteUser(user_id);
        }
        if(mob_num!=null){
            responseId=userService.deleteByMobNum(mob_num);
        }
        HashMap<String,Object> response=new HashMap<>();
        response.put("user_id",responseId);
        response.put("message","user deleted successfully");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
