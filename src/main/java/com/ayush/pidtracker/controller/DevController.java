package com.ayush.pidtracker.controller;

import com.ayush.pidtracker.entity.UserInfo;
import com.ayush.pidtracker.service.JwtService;
import com.ayush.pidtracker.service.StorageService;
import com.ayush.pidtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/dev")
public class DevController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private StorageService storageService;

    @PostMapping("/uploadfile")
    public Map<String, Object> uploadNewFile(@RequestParam("file") MultipartFile file , @RequestParam("comment") String comment, @RequestParam("pass") String pass, @RequestParam("token") String token) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        String username = jwtService.extractUsername(token);
        Optional<UserInfo> sender = userService.findUserByName(username);
        Optional<UserInfo> reviewer = userService.getUserByStatus(false,sender.get().getName());
        if(reviewer == null)
        {
            reviewer = userService.getUserByStatus(true,sender.get().getName());
        }

        int success = userService.changeUserStatus(reviewer.get().getName(),true);

        if(success <= 0)
        {
            map.put("success" , false);
            map.put("message","Error Changing User Status.");
        }

        String fileUpload = storageService.uploadImage2(file,comment,reviewer.get().getName(),sender.get().getName());

        if(fileUpload == null) {
            map.put("success", false);
            map.put("message", "Error Uploading File.");
        }

        map.put("success" , true);
        map.put("message",fileUpload);

        return map;
    }
}
