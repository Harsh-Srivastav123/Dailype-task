package com.dailype.dailypetask.services;

import com.dailype.dailypetask.dao.ManagerDao;
import com.dailype.dailypetask.dao.UserDao;
import com.dailype.dailypetask.entity.Manager;
import com.dailype.dailypetask.entity.User;
import com.dailype.dailypetask.exceptions.BadRequestException;
import com.dailype.dailypetask.exceptions.NotFoundException;
import com.dailype.dailypetask.model.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

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

        if(userDto.getMobNum().length()==10){
            userDto.setMobNum("+91"+userDto.getMobNum());
        }

        if(!managerDao.existsById(userDto.getManagerId())){
            throw new NotFoundException("manager is not found in database check the id carefully !!");
        }

        log.info(String.valueOf(userDao.existsByMobNum(userDto.getMobNum())));
        if(userDao.existsByMobNum(userDto.getMobNum())){
            throw new BadRequestException("mobile no already exist !!");
        }

        userDto.setCreatedAt(new Date());
        userDto.setActive(true);
        userDto.setPanNum(userDto.getPanNum().toUpperCase());
        User user=modelMapper.map(userDto,User.class);
        String userId=userDao.save(user).getUserId();
        log.info("user created successfully  "+ userId);
        return userId;
    }

    public Object getUserWithManagerId(String managerId) {

        log.info("All user having manager_id  "+managerId);
        return userDao.findByManagerId(managerId).stream().map(object->modelMapper.map(object,UserDto.class)).collect(Collectors.toList());
    }

    public Object getUserByMobNum(String mobNum) {
        log.info("user having mob_num  "+mobNum);
        return modelMapper.map(userDao.findByMobNum(mobNum),UserDto.class);
    }

    public Object getUserById(String userId) {
        log.info("user having user_id  "+userId);
        return modelMapper.map(userDao.findById(userId),UserDto.class);
    }

    public String deleteUser(String userId) {
        if(!userDao.existsById(userId)){
            throw new NotFoundException("user not found check the user_id carefully");
        }
        userDao.deleteById(userId);
        log.info("user deleted successfully   "+userId);
        return userId;
    }

    public String deleteByMobNum(String mobNum) {
        if(!userDao.existsByMobNum(mobNum)){
            throw new NotFoundException("user not found check the mob_num carefully");
        }
        User user=userDao.findByMobNum(mobNum);
        userDao.delete(user);
        log.info("user deleted successfully   "+user.getUserId());
        return user.getUserId();
    }
}
