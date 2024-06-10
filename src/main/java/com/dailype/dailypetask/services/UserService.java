package com.dailype.dailypetask.services;

import com.dailype.dailypetask.dao.ManagerDao;
import com.dailype.dailypetask.dao.UserDao;
import com.dailype.dailypetask.entity.User;
import com.dailype.dailypetask.exceptions.BadRequestException;
import com.dailype.dailypetask.exceptions.NotFoundException;
import com.dailype.dailypetask.model.UserDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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

        if (userDto.getMobNum().length() == 10) {
            userDto.setMobNum("+91" + userDto.getMobNum());
        }

        if (userDto.getManagerId() != null && !managerDao.existsById(userDto.getManagerId())) {
            throw new NotFoundException("manager is not found in database check the id carefully !!");
        }

        log.info(String.valueOf(userDao.existsByMobNum(userDto.getMobNum())));
        if (userDao.existsByMobNum(userDto.getMobNum())) {
            throw new BadRequestException("mobile no already exist !!");
        }

        userDto.setCreatedAt(new Date());
        userDto.setActive(true);
        userDto.setPanNum(userDto.getPanNum().toUpperCase());
        User user = modelMapper.map(userDto, User.class);
        String userId = userDao.save(user).getUserId();
        log.info("user created successfully  " + userId);
        return userId;
    }

    public Object getUserWithManagerId(String managerId) {

        log.info("All user having manager_id  " + managerId);
        return userDao.findByManagerId(managerId).stream().map(object -> modelMapper.map(object, UserDto.class)).collect(Collectors.toList());
    }

    public Object getUserByMobNum(String mobNum) {
        String mobile;
        if (mobNum.length() == 10) {
            mobile = "+91" + mobNum;
        } else {
            mobile = mobNum;
        }
        if (!userDao.existsByMobNum(mobile)) {
            throw new NotFoundException("user not found with mob_num");
        }
        log.info("user having mob_num  " + mobile);
        return modelMapper.map(userDao.findByMobNum(mobile), UserDto.class);
    }

    public Object getUserById(String userId) {

        if (!userDao.existsById(userId)) {
            throw new BadRequestException("user not found with user_id");
        }
        log.info("user having user_id  " + userId);
        return modelMapper.map(userDao.findById(userId), UserDto.class);
    }

    public String deleteUser(String userId) {
        if (!userDao.existsById(userId)) {
            throw new NotFoundException("user not found check the user_id carefully");
        }
        userDao.deleteById(userId);
        log.info("user deleted successfully   " + userId);
        return userId;
    }

    public String deleteByMobNum(String mobNum) {

        String mobile;
        if (mobNum.length() == 10) {
            mobile = "+91" + mobNum;
        }
        mobile = mobNum;
        if (!userDao.existsByMobNum(mobile)) {
            throw new NotFoundException("user not found check the mob_num carefully");
        }
        User user = userDao.findByMobNum(mobile);
        userDao.delete(user);
        log.info("user deleted successfully   " + user.getUserId());
        return user.getUserId();
    }

    public String updateUser(@Valid UserDto userDto) {
        log.info("updating user user_id = " + userDto.getUserId());
        if (!userDao.existsById(userDto.getUserId())) {
            throw new NotFoundException("user not found check the user_id carefully");
        }
        if (userDto.getManagerId() != null && !managerDao.existsById(userDto.getManagerId())) {
            throw new NotFoundException("manager not found check the manager_id carefully");
        }
        User user = userDao.findById(userDto.getUserId()).get();
        if (userDto.getManagerId() == null) {
            return userDao.save(userUpdate(userDto, user)).getUserId();
        }
        if (user.getManagerId() != null ) {
            if(user.getManagerId().equals(userDto.getManagerId())){
                return userDao.save(userUpdate(userDto, user)).getUserId();
            }
            else {
                if (user.getUpdatedAt() == null) {
                    user.setUpdatedAt(new Date());
                    return userDao.save(userUpdate(userDto, user)).getUserId();
                } else {
                    log.info("creating new user because manager_id is updated once !!");
                    user.setActive(false);
                    User user1 = new User();
                    user1.setPanNum(user.getPanNum());
                    user1.setMobNum(user.getMobNum());
                    user1.setFullName(user.getFullName());
                    user1.setCreatedAt(user.getCreatedAt());
                    user1.setUpdatedAt(new Date());
                    user1.setActive(true);
                    return userDao.save(userUpdate(userDto, user1)).getUserId();
                }
            }
        }
        else {
            user.setUpdatedAt(new Date());
            return userDao.save(userUpdate(userDto, user)).getUserId();
        }
    }

    private User userUpdate(UserDto userDto, User user) {

        if (userDto.getFullName() != null) {
            user.setFullName(userDto.getFullName());
        }
        if (userDto.getPanNum() != null) {
            user.setPanNum(userDto.getPanNum().toUpperCase());
        }
        if (userDto.getMobNum() != null) {
            if (userDto.getMobNum().length() == 10) {
                user.setMobNum("+91" + userDto.getMobNum());
            } else {
                user.setMobNum(userDto.getMobNum());
            }
        }
        if (userDto.getManagerId() != null) {
            user.setManagerId(userDto.getManagerId());
        }

        return user;
    }

    public void userUpdateUsingList(List<UserDto> userList) {
        for (UserDto userDto : userList) {
            if (!userDao.existsById(userDto.getUserId())) {
                throw new NotFoundException("user not found check the user_id carefully");
            }
            User user = userDao.findById(userDto.getUserId()).get();
            if (!user.getPanNum().equals(userDto.getPanNum()) || !user.getFullName().equals(userDto.getFullName()) || !user.getMobNum().equals(userDto.getMobNum())) {
                throw new BadRequestException("only manager key is updated in bulk list");
            } else {
                user.setManagerId(userDto.getManagerId());
            }
            user.setUpdatedAt(new Date());
            userDao.save(user);
        }
    }

    public Object findUserforTest(String userId) {
        if (!userDao.existsById(userId)) {
            throw new NotFoundException("user not found check the id carefully!!");
        }
        return userDao.findById(userId);
    }
}
