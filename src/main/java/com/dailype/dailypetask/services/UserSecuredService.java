package com.dailype.dailypetask.services;

import com.dailype.dailypetask.dao.ManagerDao;
import com.dailype.dailypetask.dao.RefreshTokenDao;
import com.dailype.dailypetask.dao.UserSecuredDao;
import com.dailype.dailypetask.dao.VerifyTokenDao;
import com.dailype.dailypetask.entity.RefreshToken;
import com.dailype.dailypetask.entity.UserSecured;
import com.dailype.dailypetask.entity.VerifyToken;
import com.dailype.dailypetask.exceptions.BadRequestException;
import com.dailype.dailypetask.exceptions.NotFoundException;
import com.dailype.dailypetask.model.UserSecuredDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserSecuredService {

    public static final int REFRESH_TOKEN_EXPIRATION_TIME = 3600;//expiration task
    @Autowired
    UserSecuredDao userSecuredDao;
    @Autowired
    ManagerDao managerDao;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    VerifyTokenDao verifyTokenDao;
    @Autowired
    AwsService awsServices;
    @Autowired
    RefreshTokenDao refreshTokenDao;

    public String createUser(@Valid UserSecuredDto userSecuredDto, MultipartFile file) {
        if (Objects.equals(file.getContentType(), "image/png") || Objects.equals(file.getContentType(), "image/jpeg")) {
            String fileName = UUID.randomUUID() + "_" + file.getName();
            awsServices.uploadMedia(file, fileName);
            userSecuredDto.setImageUrl(fileName);
        }
        if (userSecuredDao.findByUserName(userSecuredDto.getUserName()).isPresent()) {
            throw new BadRequestException("user already exists with the same user_name !!");
        }

        if (userSecuredDto.getMobNum().length() == 10) {
            userSecuredDto.setMobNum("+91" + userSecuredDto.getMobNum());
        }

        if (userSecuredDto.getManagerId() != null && !managerDao.existsById(userSecuredDto.getManagerId())) {
            throw new NotFoundException("manager is not found in database check the id carefully !!");
        }

        log.info(String.valueOf(userSecuredDao.existsByMobNum(userSecuredDto.getMobNum())));
        if (userSecuredDao.existsByMobNum(userSecuredDto.getMobNum())) {
            throw new BadRequestException("mobile no already exist !!");
        }
        userSecuredDto.setPassword(passwordEncoder.encode(userSecuredDto.getPassword()));
        userSecuredDto.setCreatedAt(new Date());
        userSecuredDto.setActive(true);
        userSecuredDto.setPanNum(userSecuredDto.getPanNum().toUpperCase());
        UserSecured UserSecured = modelMapper.map(userSecuredDto, UserSecured.class);
        String userSecuredId = userSecuredDao.save(UserSecured).getUserSecuredId();
        log.info("UserSecured created successfully  " + userSecuredId);
        return userSecuredId;
    }


    public Object getUserWithManagerId(String managerId) {

        log.info("All UserSecured having manager_id  " + managerId);
        return userSecuredDao.findByManagerId(managerId).stream().map(object -> modelMapper.map(object, UserSecuredDto.class)).collect(Collectors.toList());
    }

    public Object getUserByMobNum(String mobNum) {
        String mobile;
        if (mobNum.length() == 10) {
            mobile = "+91" + mobNum;
        } else {
            mobile = mobNum;
        }
        if (!userSecuredDao.existsByMobNum(mobile)) {
            throw new NotFoundException("UserSecured not found with mob_num");
        }
        log.info("UserSecured having mob_num  " + mobile);
        return modelMapper.map(userSecuredDao.findByMobNum(mobile), UserSecuredDto.class);
    }

    public Object getUserById(String userSecuredId) {

        if (!userSecuredDao.existsById(userSecuredId)) {
            throw new BadRequestException("UserSecured not found with UserSecured_id");
        }
        log.info("UserSecured having UserSecured_id  " + userSecuredId);
        return modelMapper.map(userSecuredDao.findById(userSecuredId), UserSecuredDto.class);
    }

    public String deleteUser(String userSecuredId) {
        if (!userSecuredDao.existsById(userSecuredId)) {
            throw new NotFoundException("UserSecured not found check the UserSecured_id carefully");
        }
        userSecuredDao.deleteById(userSecuredId);
        log.info("UserSecured deleted successfully   " + userSecuredId);
        return userSecuredId;
    }

    public String deleteByMobNum(String mobNum) {

        String mobile;
        if (mobNum.length() == 10) {
            mobile = "+91" + mobNum;
        }
        mobile = mobNum;
        if (!userSecuredDao.existsByMobNum(mobile)) {
            throw new NotFoundException("UserSecured not found check the mob_num carefully");
        }
        UserSecured UserSecured = userSecuredDao.findByMobNum(mobile);
        userSecuredDao.delete(UserSecured);
        log.info("UserSecured deleted successfully   " + UserSecured.getUserSecuredId());
        return UserSecured.getUserSecuredId();
    }

    public String updateUser(@Valid UserSecuredDto userSecuredDto) {
        if (!userSecuredDao.existsById(userSecuredDto.getUserSecuredId())) {
            throw new NotFoundException("UserSecured not found check the User_secured_id carefully");
        }
        if (userSecuredDto.getManagerId() != null && !managerDao.existsById(userSecuredDto.getManagerId())) {
            throw new NotFoundException("manager not found check the manager_id carefully");
        }
        UserSecured userSecured = userSecuredDao.findById(userSecuredDto.getUserSecuredId()).get();
        if (userSecuredDto.getManagerId() == null) {
            return userSecuredDao.save(UserSecuredUpdate(userSecuredDto, userSecured)).getUserSecuredId();
        }

        if (userSecured.getManagerId() != null) {
            if (userSecured.getManagerId().equals(userSecuredDto.getManagerId())) {
                return userSecuredDao.save(UserSecuredUpdate(userSecuredDto, userSecured)).getUserSecuredId();
            } else {
                if (userSecured.getUpdatedAt() == null) {
                    userSecured.setUpdatedAt(new Date());
                    return userSecuredDao.save(UserSecuredUpdate(userSecuredDto, userSecured)).getUserSecuredId();
                } else {
                    log.info("creating new user_secured because manager_id is updated once !!");
                    userSecured.setActive(false);
                    UserSecured userSecured1 = new UserSecured();
                    userSecured1.setPanNum(userSecured.getPanNum());
                    userSecured1.setMobNum(userSecured.getMobNum());
                    userSecured1.setFullName(userSecured.getFullName());
                    userSecured1.setCreatedAt(userSecured.getCreatedAt());
                    userSecured1.setUpdatedAt(new Date());
                    userSecured1.setActive(true);
                    return userSecuredDao.save(UserSecuredUpdate(userSecuredDto, userSecured1)).getUserSecuredId();
                }
            }
        } else {
            userSecured.setUpdatedAt(new Date());
            return userSecuredDao.save(UserSecuredUpdate(userSecuredDto, userSecured)).getUserSecuredId();
        }
    }

    private UserSecured UserSecuredUpdate(UserSecuredDto userSecuredDto, UserSecured UserSecured) {

        if (userSecuredDto.getFullName() != null) {
            UserSecured.setFullName(userSecuredDto.getFullName());
        }
        if (userSecuredDto.getPanNum() != null) {
            UserSecured.setPanNum(userSecuredDto.getPanNum().toUpperCase());
        }
        if (userSecuredDto.getMobNum() != null) {
            if (userSecuredDto.getMobNum().length() == 10) {
                UserSecured.setMobNum("+91" + userSecuredDto.getMobNum());
            } else {
                UserSecured.setMobNum(userSecuredDto.getMobNum());
            }
        }
        if (userSecuredDto.getManagerId() != null) {
            UserSecured.setManagerId(userSecuredDto.getManagerId());
        }

        return UserSecured;
    }

    public void UserUpdateUsingList(List<UserSecuredDto> UserSecuredList) {
        for (UserSecuredDto userSecuredDto : UserSecuredList) {
            if (!userSecuredDao.existsById(userSecuredDto.getUserSecuredId())) {
                throw new NotFoundException("UserSecured not found check the user_secured_id carefully");
            }
            UserSecured UserSecured = userSecuredDao.findById(userSecuredDto.getUserSecuredId()).get();
            if (!UserSecured.getPanNum().equals(userSecuredDto.getPanNum()) || !UserSecured.getFullName().equals(userSecuredDto.getFullName()) || !UserSecured.getMobNum().equals(userSecuredDto.getMobNum())) {
                throw new BadRequestException("only manager key is updated in bulk list");
            } else {
                UserSecured.setManagerId(userSecuredDto.getManagerId());
            }
            UserSecured.setUpdatedAt(new Date());
            userSecuredDao.save(UserSecured);
        }
    }

    public Object getUserIdByUsername(String userName) {
        Optional<UserSecured> userSecured = userSecuredDao.findByUserName(userName);
        if (userSecured.isEmpty()) {
            throw new BadRequestException("unable to find user_secured by username !!");
        }
        return userSecured.get().getUserSecuredId();
    }

    public void saveVerificationToken(UserSecuredDto userSecuredDto, String token) {
        UserSecured userSecured = userSecuredDao.findByUserName(userSecuredDto.getUserName()).get();
        verifyTokenDao.save(new VerifyToken(token, userSecured));
    }

    public int validateToken(String token) {

        VerifyToken verifyToken = verifyTokenDao.findByToken(token);
        if (verifyToken == null) {
            return 1;
        }


        UserSecured userSecured = verifyToken.getUserSecured();
        Calendar calendar = Calendar.getInstance();
        if ((verifyToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            verifyTokenDao.delete(verifyToken);
            userSecuredDao.delete(verifyToken.getUserSecured());
            return 2;
        }
        userSecured.setVerified(true);
        userSecuredDao.save(userSecured);
        return 3;
    }

    public byte[] getImage(String userSecuredId) {
        if (!userSecuredDao.existsById(userSecuredId)) {
            throw new BadRequestException("User_secured doesn't exists");
        }
        String fileName = userSecuredDao.findById(userSecuredId).get().getImageUrl();
        return awsServices.downloadFile(fileName);
    }

    public String createRefreshToken(String username) {
        RefreshToken refreshTokenTemp = refreshTokenDao.findByUserSecured(userSecuredDao.findByUserName(username).get());
        if (refreshTokenTemp != null) {
            refreshTokenTemp.setUserSecured(null);
            refreshTokenDao.delete(refreshTokenDao.save(refreshTokenTemp));
        }
        RefreshToken refreshToken = RefreshToken.builder().userSecured(userSecuredDao.findByUserName(username).get()).token(UUID.randomUUID().toString()).expiryDate(Instant.now().plusMillis(60000 * REFRESH_TOKEN_EXPIRATION_TIME)).build();
        return refreshTokenDao.save(refreshToken).getToken();
    }

    public UserSecuredDto verifyExpiration(String refreshToken) {
        if (!refreshTokenDao.existsByToken(refreshToken)) {
            throw new BadRequestException("Unable to associate user with this token !!");
        }
        RefreshToken token = refreshTokenDao.findByToken(refreshToken);

        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenDao.delete(token);
            throw new BadRequestException(token.getToken() + " Refresh token was expired. Please make a new signin request");
        }
        return modelMapper.map(token.getUserSecured(), UserSecuredDto.class);
    }


    public Object getRefreshToken(String userName) {
        if (userSecuredDao.findByUserName(userName).isEmpty()) {
            throw new BadRequestException("user_secured doesn't exists !!");
        }
        return refreshTokenDao.findByUserSecured(userSecuredDao.findByUserName(userName).get()).getToken();
    }
}
