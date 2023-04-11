package com.ayush.pidtracker.service;

import com.ayush.pidtracker.entity.ImageData;
import com.ayush.pidtracker.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SchedulerService {

    @Autowired
    private StorageService storageService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 45 23 * * ?")
    public void scheduleMail(){
        List<ImageData> nonReviewedFiles = storageService.findFilesByStatus(false);
        if(!nonReviewedFiles.isEmpty())
        {

            for(ImageData file: nonReviewedFiles){
                Date dueDate = file.getCreationDate();
                Date currDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Calendar c = Calendar.getInstance();
                c.setTime(dueDate);
                c.add(Calendar.DATE,-3);

                if(currDate.after(c.getTime()))
                {
                    Optional<UserInfo> reviewer = userService.findUserByName(file.getReviewer_id());
                    Optional<UserInfo> developer = userService.findUserByName(file.getUser_id());
                    mailService.sendMailUsingTLS(developer.get().getName(),"ayush2002","paulayush2002@gmail.com",reviewer.get().getEmail(),"Scheduled Mail : PID Review Pending for " + file.getCreationDate().toString(), file.getComment());
                }
            }
        }
    }
}
