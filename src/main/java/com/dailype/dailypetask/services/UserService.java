package com.dailype.dailypetask.services;

import com.dailype.dailypetask.dao.ManagerDao;
import com.dailype.dailypetask.dao.UserDao;
import com.dailype.dailypetask.entity.Manager;
import com.dailype.dailypetask.entity.User;
import com.dailype.dailypetask.exceptions.NotFoundException;
import com.dailype.dailypetask.model.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    ManagerDao managerDao;

    @Autowired
    ModelMapper modelMapper;


    public String createUser(UserDto userDto) {

        if(!managerDao.existsById(userDto.getManager_id())){
            throw new NotFoundException("manager is not found in database check the id carefully !!");
        }

        userDto.setCreated_at(new Date());
        userDto.set_active(true);
        userDto.setPan_num(userDto.getPan_num().toUpperCase());
        User user=modelMapper.map(userDto,User.class);
        String userId=userDao.save(user).getUser_id();
        log.info("user created successfully  "+ userId);
        return userId;
    }
}
