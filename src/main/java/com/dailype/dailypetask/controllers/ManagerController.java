package com.dailype.dailypetask.controllers;

import com.dailype.dailypetask.model.ManagerDto;
import com.dailype.dailypetask.services.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    ManagerService managerService;

    @PostMapping("/create_manager")
    public ResponseEntity<Object> createManager(@RequestBody ManagerDto managerDto){
        String managerId=managerService.createManager(managerDto);
        HashMap<String,Object> response =new HashMap<>();
        response.put("manager_id",managerId);
        response.put("message","Manager Created Successfully ");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
