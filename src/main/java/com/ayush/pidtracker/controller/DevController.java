package com.ayush.pidtracker.controller;

import com.ayush.pidtracker.entity.ImageData;
import com.ayush.pidtracker.entity.UserInfo;
import com.ayush.pidtracker.service.JwtService;
import com.ayush.pidtracker.service.StorageService;
import com.ayush.pidtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@RestController
@RequestMapping("/dev")
@CrossOrigin
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
        List<Optional<UserInfo>>  reviewer = userService.getUserByStatus(false,sender.get().getName());
        if(reviewer.isEmpty())
        {

            reviewer = userService.getUserByStatus(true,sender.get().getName());
            //logger.log(Level.INFO,reviewer.get().getName());
            if(reviewer == null)
            {
                map.put("success" , false);
                map.put("message","Error Finding Reviewer");
                return map;
            }
        }
        //logger.log(Level.INFO,reviewer.get().getName());
        int success = userService.changeUserStatus(reviewer.get(0).get().getName(),true);

        if(success <= 0)
        {
            map.put("success" , false);
            map.put("message","Error Changing User Status.");
            return map;
        }

        String fileUpload = storageService.uploadImage2(file,comment,reviewer.get(0).get().getName(),sender.get().getName());
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
    public List<ImageData> getApprovedFiles(@RequestHeader("Authorization") String authHeader){
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

    @PostMapping("/uploadfilerev")
    public Map<String, Object> uploadFileRev(@RequestParam("file") MultipartFile file , @RequestParam("comment") String comment, @RequestParam("pass") String pass, @RequestParam("fileName") String fileName, @RequestHeader("Authorization") String authHeader) throws IOException {
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
        Optional<UserInfo> dev = userService.findUserByName(username);
        Optional<ImageData> prev_file = storageService.findFileByName(fileName);
        Optional<UserInfo> reviewer = userService.findUserByName(prev_file.get().getUser_id());
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


        int userStatusChange = userService.changeUserStatus(reviewer.get().getName(),true);
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
        String fileUpload = storageService.uploadImage2(file,comment,reviewer.get().getName(),dev.get().getName());
        //logger.log(Level.INFO,fileUpload);
        if(fileUpload == null) {
            map.put("success", false);
            map.put("message", "Error Uploading File.");
            return map;
        }

        map.put("success" , true);
        map.put("message",fileUpload);

        return map;
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/dev/**")
//
//
//                        .allowedHeaders("file","fileName","comment","pass","Access-Control-Allow-Headers","Authorization")
//                        .exposedHeaders("Content-Disposition");
//            }
//        };
//    }
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000/**"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
//        configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Headers","file","fileName","comment","pass","Authorization"));
//        configuration.addExposedHeader("Content-Disposition");
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

}
