package com.ayush.pidtracker.controller;

import com.ayush.pidtracker.entity.ImageData;
import com.ayush.pidtracker.entity.UserInfo;
import com.ayush.pidtracker.service.JwtService;
import com.ayush.pidtracker.service.StorageService;
import com.ayush.pidtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

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
    public Map<String, Object> uploadNewFile(@RequestParam("file") MultipartFile file , @RequestParam("comment") String comment, @RequestParam("pass") String pass, @RequestParam("token") String token, @RequestHeader("Authorization") String authHeader) throws IOException {
        LogManager logManager = LogManager.getLogManager();
        Logger logger = logManager.getLogger(Logger.GLOBAL_LOGGER_NAME);
        Map<String, Object> map = new HashMap<String, Object>();
        String token2 = authHeader.substring(7);
        if(token2 == null)
        {
            map.put("success" , false);
            map.put("message","Error Fetching User");
            return map;
        }
        String username = jwtService.extractUsername(token2);
        Optional<UserInfo> sender = userService.findUserByName(username);
        Optional<UserInfo> reviewer = userService.getUserByStatus(false,sender.get().getName());
        if(reviewer.isEmpty())
        {

            reviewer = userService.getUserByStatus(true,sender.get().getName());
            logger.log(Level.INFO,reviewer.get().getName());
            if(reviewer == null)
            {
                map.put("success" , false);
                map.put("message","Error Finding Reviewer");
                return map;
            }
        }
        logger.log(Level.INFO,reviewer.get().getName());
        int success = userService.changeUserStatus(reviewer.get().getName(),true);

        if(success <= 0)
        {
            map.put("success" , false);
            map.put("message","Error Changing User Status.");
            return map;
        }

        String fileUpload = storageService.uploadImage2(file,comment,reviewer.get().getName(),sender.get().getName());
        logger.log(Level.INFO,fileUpload);
        if(fileUpload == null) {
            map.put("success", false);
            map.put("message", "Error Uploading File.");
            return map;
        }

        map.put("success" , true);
        map.put("message",fileUpload);

        return map;
    }

    @GetMapping("/approved")
    public List<ImageData> getApprovedFiles(@RequestHeader("token") String authHeader){
        Map<String, Object> map = new HashMap<String, Object>();
        String token2 = authHeader.substring(7);
        if(token2 == null)
        {
            map.put("success" , false);
            map.put("message","Error Fetching User");
            return null;
        }
        String username = jwtService.extractUsername(token2);
        return storageService.findFilesForDev(username,true);
    }

    @GetMapping("/pending")
    public List<ImageData> getPendingFiles(@RequestHeader("Authorization") String authHeader){
        Map<String, Object> map = new HashMap<String, Object>();
        String token2 = authHeader.substring(7);
        if(token2 == null)
        {
            map.put("success" , false);
            map.put("message","Error Fetching User");
            return null;
        }
        String username = jwtService.extractUsername(token2);
        return storageService.findFilesForDev(username,false);
    }
}
