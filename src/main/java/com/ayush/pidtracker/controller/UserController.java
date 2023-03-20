package com.ayush.pidtracker.controller;

import com.ayush.pidtracker.dto.AuthRequest;
import com.ayush.pidtracker.entity.UserInfo;
import com.ayush.pidtracker.service.JwtService;
import com.ayush.pidtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService service;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @PostMapping("/new")
    public Map<String, Object> addNewUser(@RequestBody UserInfo userInfo){
        String f = service.addUser(userInfo);
        Optional<UserInfo> userInfo1 = service.findUserByName(userInfo.getName());
        Map<String, Object> map = new HashMap<String, Object>();
        String token = jwtService.generateToken(userInfo1.get().getName(), String.valueOf(((int) userInfo1.get().getId())));
        map.put(f,true);
        map.put("authtoken",token);
        map.put("message","Done");
        return map;
    }

    @PostMapping("/authenticate")
    public Map<String, Object> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Map<String, Object> map = new HashMap<String, Object>();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            map.put("authtoken",jwtService.generateToken(authRequest.getUsername(),authRequest.getId()));
            map.put("success",true);
            return map;
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }

    }
    @GetMapping("/{Id}")
    public Optional<UserInfo> findUserById(@PathVariable Integer Id){
        Optional<UserInfo> userInfo = service.findUserById(Id);
        return userInfo;
    }

    @PostMapping("/name")
    public Map<String, Object> findUserByName(@RequestHeader("Authorization") String authHeader){
        Map<String, Object> map = new HashMap<String, Object>();
        String token2 = authHeader.substring(7);
        if(token2 == null)
        {
            map.put("success" , false);
            map.put("message","Error Fetching User");
            return map;
        }
        String username = jwtService.extractUsername(token2);
        Optional<UserInfo> userInfo = service.findUserByName(username);
        map.put("success",true);
        map.put("user_name",userInfo.get().getName());

        return map;
    }
}
