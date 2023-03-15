package com.ayush.pidtracker.service;


import com.ayush.pidtracker.entity.UserInfo;
import com.ayush.pidtracker.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserInfoRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String addUser(UserInfo userInfo){
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInfo.setCurrently_reviewing(false);
        repository.save(userInfo);
        return "user added to system ";
    }

    public Optional<UserInfo> findUserById(Integer Id){
        Optional<UserInfo> userInfo = repository.findUserById(Id);
        return userInfo;
    }
    public Optional<UserInfo> findUserByName(String name){
        Optional<UserInfo> userInfo = repository.findUserByName(name);
        return userInfo;
    }

    public Optional<UserInfo> getUserByStatus(Boolean Status , String name){
        Optional<UserInfo> userInfo = repository.getUserByStatus(Status,name);
        return userInfo;
    }

    public int changeUserStatus(String name , Boolean Status){
        int success = repository.changeUserStatus(name,Status);
        return success;
    }


}
