package com.ayush.pidtracker.controller;

import com.ayush.pidtracker.entity.ImageData;
import com.ayush.pidtracker.entity.UserInfo;
import com.ayush.pidtracker.service.JwtService;
import com.ayush.pidtracker.service.MailService;
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
@RequestMapping("/rev")
public class RevController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private MailService mailService;

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
        return storageService.findFilesForRev(username,false);
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
        return storageService.findFilesForRev(username,true);
    }

    @PostMapping("/uploadfile")
    public Map<String, Object> uploadNewFile(@RequestParam("file") MultipartFile file , @RequestParam("comment") String comment, @RequestParam("pass") String pass, @RequestParam("fileName") String fileName, @RequestHeader("Authorization") String authHeader) throws IOException {
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
        Optional<UserInfo> reviewer = userService.findUserByName(username);
        Optional<ImageData> prev_file = storageService.findFileByName(fileName);
        Optional<UserInfo> dev = userService.findUserByName(prev_file.get().getUser_id());
        if(reviewer.isEmpty())
        {
            map.put("success" , false);
            map.put("message","Error Finding Reviewer");
            return map;
        }
        if(dev.isEmpty())
        {
            map.put("success" , false);
            map.put("message","Error Finding Developer");
            return map;
        }
        if(prev_file.isEmpty())
        {
            map.put("success" , false);
            map.put("message","Error Finding Previous File");
            return map;
        }

        // Need to write a method which checks if the reviewere has some files pending before setting currently_reviewing status to false
        int userStatusChange = userService.changeUserStatus(username,false);
        //logger.log(Level.INFO,reviewer.get().getName());
        //int success = userService.changeUserStatus(reviewer.get().getName(),true);

        if(userStatusChange <= 0)
        {
            map.put("success" , false);
            map.put("message","Error Changing User Status.");
            return map;
        }
        int fileChanged = storageService.deleteFileByName(prev_file.get().getName());
        if (fileChanged<=0)
        {
            map.put("success" , false);
            map.put("message","Error Changing File Status.");
            return map;
        }
        String fileUpload = storageService.uploadImage2(file,comment,reviewer.get().getName(),dev.get().getName(),true);
        //logger.log(Level.INFO,fileUpload);
        if(fileUpload == null) {
            map.put("success", false);
            map.put("message", "Error Uploading File.");
            return map;
        }
        mailService.sendMailUsingSSL(reviewer.get().getName(),pass,reviewer.get().getEmail(),dev.get().getEmail(),"Hey your PID review is Done",comment);
        map.put("success" , true);
        map.put("message",fileUpload);

        return map;
    }




}
