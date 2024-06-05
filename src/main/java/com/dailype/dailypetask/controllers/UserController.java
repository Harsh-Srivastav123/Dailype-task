package com.dailype.dailypetask.controllers;

import com.dailype.dailypetask.entity.User;
import com.dailype.dailypetask.exceptions.BadRequestException;
import com.dailype.dailypetask.model.RequestPayload;
import com.dailype.dailypetask.model.UserDto;
import com.dailype.dailypetask.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    
    
    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    public record Response(String user_id, Object object){};

    @PostMapping("/create_user")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto){
        String userId=userService.createUser(userDto);
        return new ResponseEntity<>(new Response(userId,"User Created Successfully"), HttpStatus.OK);
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
    public ResponseEntity<Object> deleteUser(@RequestBody RequestPayload requestPayload) {
        if (requestPayload.getUserId() == null && requestPayload.getMobNum() == null) {
            throw new BadRequestException("Please enter the user_id, mob_num correctly. !!");
        }
        String user_id = requestPayload.getUserId();
        String mob_num = requestPayload.getMobNum();
        String responseId = null;
        if (user_id != null) {
            responseId = userService.deleteUser(user_id);
        }
        if (mob_num != null) {
            responseId = userService.deleteByMobNum(mob_num);
        }
        return new ResponseEntity<>(new Response(responseId, "user deleted successfully"), HttpStatus.OK);
    }

    @PostMapping("/update_user")
    public ResponseEntity<Object> updateUser(@RequestBody String response){
        if(response.startsWith("{")){
            UserDto userDto= null;
            try {
                userDto = objectMapper.readValue(response, UserDto.class);
            } catch (JsonProcessingException e) {
                log.info(e.getMessage());
                throw new BadRequestException(e.getMessage());
            }
            String responseId=userService.updateUser(userDto);
            if(userDto.getUserId().equals(responseId)){
                return new ResponseEntity<>(new Response(responseId,"user updated successfully"),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Response(responseId, "new user is created because manager_id is already updated"),HttpStatus.OK);

        }

//        List<?> tempList = (List<?>) object;
//        try {
//            List<UserDto> userList = (List<UserDto>) tempList;
//            userService.userUpdateUsingList(userList);
//        } catch (ClassCastException e) {
//            log.info(e.getMessage());
//            e.printStackTrace();
//            throw new BadRequestException("Unable to evaluate list for update");
//        }

        List<Object> objectList = null;
        try {
            objectList = objectMapper.readValue(response, new TypeReference<List<Object>>(){});
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
            throw new BadRequestException(e.getMessage());
        }

        // If you want to convert the List<Object> to List<User> later
        List<UserDto> userList = objectMapper.convertValue(objectList, new TypeReference<List<UserDto>>() {});
        userService.userUpdateUsingList(userList);
        return new ResponseEntity<>("List is updated successfully",HttpStatus.OK);
    }

}
