package com.dailype.dailypetask.services;

import com.dailype.dailypetask.dao.UserSecuredDao;
import com.dailype.dailypetask.entity.UserSecured;
import com.dailype.dailypetask.model.UserSecuredInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsSecuredInfoService implements UserDetailsService {

    @Autowired
    UserSecuredDao userSecuredDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserSecured> user=userSecuredDao.findByUserName(username);

        return user.map(UserSecuredInfo::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
    }
}
