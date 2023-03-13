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
        repository.save(userInfo);
        return "user added to system ";
    }

    public Optional<UserInfo> findUserById(Integer Id){
        Optional<UserInfo> userInfo = repository.findUserById(Id);
        return userInfo;
    }
}
