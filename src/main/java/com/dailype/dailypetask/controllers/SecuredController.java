package com.dailype.dailypetask.controllers;

import com.dailype.dailypetask.entity.UserSecured;
import com.dailype.dailypetask.event.VerifyTokenEvent;
import com.dailype.dailypetask.exceptions.BadRequestException;
import com.dailype.dailypetask.model.RequestPayload;
import com.dailype.dailypetask.model.RequestSecuredPayload;
import com.dailype.dailypetask.model.UserDto;
import com.dailype.dailypetask.model.UserSecuredDto;
import com.dailype.dailypetask.security.JwtServices;
import com.dailype.dailypetask.services.UserSecuredService;
import com.dailype.dailypetask.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/secured")
@Slf4j
public class SecuredController {


    @Autowired
    JwtServices jwtServices;

    @Autowired
    UserSecuredService userSecuredService;

    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    Gson gson;

    @Autowired
    ApplicationEventPublisher publisher;




    public record Response(String user_id, Object object){};

    @PostMapping("/create_user")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserSecuredDto userSecuredDto, HttpServletRequest request){
        String userId=userSecuredService.createUser(userSecuredDto);
        publisher.publishEvent(new VerifyTokenEvent(userSecuredDto,generateUrl(request)));
        return new ResponseEntity<>(new Response(userId,"User_Secured Created Successfully"), HttpStatus.OK);
    }


    private String generateUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }

    @PostMapping("/auth")
    public ResponseEntity<Object> authUser(@RequestBody String response){
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        String userName=jsonObject.get("user_name").getAsString();
        String password=jsonObject.get("password").getAsString();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName ,password));
        if(authentication.isAuthenticated()){
            HashMap<String ,Object> token=new HashMap<>();
            token.put("user_secured_id",userSecuredService.getUserIdByUsername(userName));
            token.put("user_name",userName);
            token.put("token",jwtServices.generateToken(userName));
            return new ResponseEntity<>(token,HttpStatus.OK);
        }
        return new ResponseEntity<>("check the credentials carefully !!", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/get_user")
    public ResponseEntity<Object> getUser(@RequestBody RequestSecuredPayload requestSecuredPayload){
        String user_secured_id=requestSecuredPayload.getUserSecuredId();
        String mob_num=requestSecuredPayload.getMobNum();
        String manager_id= requestSecuredPayload.getManagerId();
        if(user_secured_id==null && manager_id==null && mob_num==null){
            throw  new BadRequestException("Please enter the user_id, mob_num and manager_id correctly. !!");
        }
        if(manager_id!=null){
            return new ResponseEntity<>(userSecuredService.getUserWithManagerId(manager_id),HttpStatus.OK);
        }
        else if(mob_num!=null){
            return new ResponseEntity<>(userSecuredService.getUserByMobNum(mob_num),HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(userSecuredService.getUserById(user_secured_id),HttpStatus.OK);
        }
    }

    @GetMapping("verifyRegistration")
    public ResponseEntity<Object> verifyRegistration(@RequestParam("token") String token){
        int status=userSecuredService.validateToken(token);
        if(status==0){
            return new ResponseEntity<>("enter valid token", HttpStatus.BAD_REQUEST);
        }
        if(status==1){
            return new ResponseEntity<>("token expired",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("token  verified Successfully ",HttpStatus.OK);
    }

    @PostMapping("/delete_user")
    public ResponseEntity<Object> deleteUser(@RequestBody RequestSecuredPayload requestSecuredPayload) {
        if (requestSecuredPayload.getUserSecuredId() == null && requestSecuredPayload.getMobNum() == null) {
            throw new BadRequestException("Please enter the user_secured_id, mob_num correctly. !!");
        }
        String user_id = requestSecuredPayload.getUserSecuredId();
        String mob_num = requestSecuredPayload.getMobNum();
        String responseId = null;
        if (user_id != null) {
            responseId = userSecuredService.deleteUser(user_id);
        }
        if (mob_num != null) {
            responseId = userSecuredService.deleteByMobNum(mob_num);
        }
        return new ResponseEntity<>(new Response(responseId, "user_secured deleted successfully"), HttpStatus.OK);
    }

    @PostMapping("/update_user")
    public ResponseEntity<Object> updateUser(@RequestBody String response){
        if(response.startsWith("{")){
            UserSecuredDto userSecuredDto= null;
            try {
                userSecuredDto = objectMapper.readValue(response, UserSecuredDto.class);
            } catch (JsonProcessingException e) {
                log.info(e.getMessage());
                throw new BadRequestException(e.getMessage());
            }
            String responseId=userSecuredService.updateUser(userSecuredDto);
            if(userSecuredDto.getUserSecuredId().equals(responseId)){
                return new ResponseEntity<>(new Response(responseId,"user updated successfully"),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Response(responseId, "new user_secured is created because manager_id is already updated"),HttpStatus.OK);

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
        List<UserSecuredDto> userList = objectMapper.convertValue(objectList, new TypeReference<List<UserSecuredDto>>() {});
        userSecuredService.UserUpdateUsingList(userList);
        return new ResponseEntity<>("List is updated successfully",HttpStatus.OK);
    }


}
