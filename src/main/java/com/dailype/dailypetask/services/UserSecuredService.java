package com.dailype.dailypetask.services;

import com.dailype.dailypetask.dao.ManagerDao;
import com.dailype.dailypetask.dao.UserSecuredDao;

import com.dailype.dailypetask.entity.UserSecured;
import com.dailype.dailypetask.exceptions.BadRequestException;
import com.dailype.dailypetask.exceptions.NotFoundException;
import com.dailype.dailypetask.model.UserSecuredDto;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserSecuredService {

    @Autowired
    UserSecuredDao userSecuredDao;

    @Autowired
    ManagerDao managerDao;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;


    public String createUser(UserSecuredDto userSecuredDto) {


        if(userSecuredDao.findByUserName(userSecuredDto.getUserName()).isPresent()){
            throw new BadRequestException("user already exists with the same user_name !!");
        }

        if(userSecuredDto.getMobNum().length()==10){
            userSecuredDto.setMobNum("+91"+userSecuredDto.getMobNum());
        }

        if( userSecuredDto.getManagerId()!=null && !managerDao.existsById(userSecuredDto.getManagerId())){
            throw new NotFoundException("manager is not found in database check the id carefully !!");
        }

        log.info(String.valueOf(userSecuredDao.existsByMobNum(userSecuredDto.getMobNum())));
        if(userSecuredDao.existsByMobNum(userSecuredDto.getMobNum())){
            throw new BadRequestException("mobile no already exist !!");
        }
        userSecuredDto.setPassword(passwordEncoder.encode(userSecuredDto.getPassword()));
        userSecuredDto.setCreatedAt(new Date());
        userSecuredDto.setActive(true);
        userSecuredDto.setPanNum(userSecuredDto.getPanNum().toUpperCase());
        UserSecured UserSecured=modelMapper.map(userSecuredDto,UserSecured.class);
        String userSecuredId=userSecuredDao.save(UserSecured).getUserSecuredId();
        log.info("UserSecured created successfully  "+ userSecuredId);
        return userSecuredId;
    }

    public Object getUserWithManagerId(String managerId) {

        log.info("All UserSecured having manager_id  "+managerId);
        return userSecuredDao.findByManagerId(managerId).stream().map(object->modelMapper.map(object,UserSecuredDto.class)).collect(Collectors.toList());
    }

    public Object getUserByMobNum(String mobNum) {
        String mobile;
        if(mobNum.length()==10){
            mobile="+91"+mobNum;
        }
        else {
            mobile=mobNum;
        }
        if(!userSecuredDao.existsByMobNum(mobile)){
            throw new NotFoundException("UserSecured not found with mob_num");
        }
        log.info("UserSecured having mob_num  "+mobile);
        return modelMapper.map(userSecuredDao.findByMobNum(mobile),UserSecuredDto.class);
    }

    public Object getUserById(String userSecuredId) {

        if(!userSecuredDao.existsById(userSecuredId)){
            throw new BadRequestException("UserSecured not found with UserSecured_id");
        }
        log.info("UserSecured having UserSecured_id  "+userSecuredId);
        return modelMapper.map(userSecuredDao.findById(userSecuredId),UserSecuredDto.class);
    }

    public String deleteUser(String userSecuredId) {
        if(!userSecuredDao.existsById(userSecuredId)){
            throw new NotFoundException("UserSecured not found check the UserSecured_id carefully");
        }
        userSecuredDao.deleteById(userSecuredId);
        log.info("UserSecured deleted successfully   "+userSecuredId);
        return userSecuredId;
    }

    public String deleteByMobNum(String mobNum) {

        String mobile;
        if(mobNum.length()==10){
            mobile="+91"+mobNum;
        }
        mobile=mobNum;
        if(!userSecuredDao.existsByMobNum(mobile)){
            throw new NotFoundException("UserSecured not found check the mob_num carefully");
        }
        UserSecured UserSecured=userSecuredDao.findByMobNum(mobile);
        userSecuredDao.delete(UserSecured);
        log.info("UserSecured deleted successfully   "+UserSecured.getUserSecuredId());
        return UserSecured.getUserSecuredId();
    }

    public String updateUser(@Valid UserSecuredDto userSecuredDto) {
        if(!userSecuredDao.existsById(userSecuredDto.getUserSecuredId())){
            throw new NotFoundException("UserSecured not found check the User_secured_id carefully");
        }
        if(userSecuredDto.getManagerId()!=null && !managerDao.existsById(userSecuredDto.getManagerId())){
            throw new NotFoundException("manager not found check the manager_id carefully");
        }
        UserSecured UserSecured=userSecuredDao.findById(userSecuredDto.getUserSecuredId()).get();
        if(userSecuredDto.getManagerId()==null ||UserSecured.getManagerId().equals(userSecuredDto.getManagerId())){
            return userSecuredDao.save(UserSecuredUpdate(userSecuredDto,UserSecured)).getUserSecuredId();
        }
        else {
            if(UserSecured.getUpdatedAt()==null){
                UserSecured.setUpdatedAt(new Date());
                return userSecuredDao.save(UserSecuredUpdate(userSecuredDto,UserSecured)).getUserSecuredId();
            }
            else {
                log.info("creating new user_secured because manager_id is updated once !!");
                UserSecured.setActive(false);
                UserSecured UserSecured1=new UserSecured();
                UserSecured1.setPanNum(UserSecured.getPanNum());
                UserSecured1.setMobNum(UserSecured.getMobNum());
                UserSecured1.setFullName(UserSecured.getFullName());
                UserSecured1.setCreatedAt(UserSecured.getCreatedAt());
                UserSecured1.setUpdatedAt(new Date());
                UserSecured1.setActive(true);
                return userSecuredDao.save(UserSecuredUpdate(userSecuredDto,UserSecured1)).getUserSecuredId();
            }
        }
    }

    private UserSecured UserSecuredUpdate(UserSecuredDto userSecuredDto, UserSecured UserSecured) {

        if(userSecuredDto.getFullName()!=null){
            UserSecured.setFullName(userSecuredDto.getFullName());
        }
        if(userSecuredDto.getPanNum()!=null){
            UserSecured.setPanNum(userSecuredDto.getPanNum().toUpperCase());
        }
        if(userSecuredDto.getMobNum()!=null){
            if(userSecuredDto.getMobNum().length()==10){
                UserSecured.setMobNum("+91"+userSecuredDto.getMobNum());
            }
            else {
                UserSecured.setMobNum(userSecuredDto.getMobNum());
            }
        }
        if(userSecuredDto.getManagerId()!=null){
            UserSecured.setManagerId(userSecuredDto.getManagerId());
        }

        return UserSecured;
    }

    public void UserUpdateUsingList(List<UserSecuredDto> UserSecuredList) {
        for (UserSecuredDto userSecuredDto:UserSecuredList){
            if(!userSecuredDao.existsById(userSecuredDto.getUserSecuredId())){
                throw new NotFoundException("UserSecured not found check the user_secured_id carefully");
            }
            UserSecured UserSecured=userSecuredDao.findById(userSecuredDto.getUserSecuredId()).get();
            if(!UserSecured.getPanNum().equals(userSecuredDto.getPanNum())|| !UserSecured.getFullName().equals(userSecuredDto.getFullName()) || !UserSecured.getMobNum().equals(userSecuredDto.getMobNum())){
                throw new BadRequestException("only manager key is updated in bulk list");
            }else {
                UserSecured.setManagerId(userSecuredDto.getManagerId());
            }
            UserSecured.setUpdatedAt(new Date());
            userSecuredDao.save(UserSecured);
        }
    }

    public Object getUserIdByUsername(String userName) {
        Optional<UserSecured> userSecured=userSecuredDao.findByUserName(userName);
        if(userSecured.isEmpty()){
            throw new BadRequestException("unable to find user_secured by username !!");
        }
        return userSecured.get().getUserSecuredId();
    }
}
