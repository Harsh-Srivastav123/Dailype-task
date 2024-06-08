package com.dailype.dailypetask.model;

import com.dailype.dailypetask.entity.UserSecured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserSecuredInfo implements UserDetails {
    private String userName;
    private String password;

    private boolean isVerified;

    public UserSecuredInfo(UserSecured userSecured){
        this.userName=userSecured.getUserName();
        this.password=userSecured.getPassword();

        this.isVerified= userSecured.isVerified();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isVerified;
    }
}
