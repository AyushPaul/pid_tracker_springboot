package com.ayush.pidtracker.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class MailService {

//    @Autowired
//    private JavaMailSender javaMailSender;

    public Map<String,Object> sendMailUsingTLS(
                                        String username, String password, String from,
                                        String to, String subject, String text) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.office365.com");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.port", "587");
            sendMail2(properties, username, password, from, to,
                    subject, text);
            map.put("success",true);
            map.put("message","Mail Sent Successfully");
        }
        catch (Exception e)
        {
            map.put("success",false);
            map.put("message",e.getMessage());
        }
        return map;
    }

    public static void sendMailUsingSSL(
                                        String username, String password, String from,
                                        String to, String subject, String text) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp-mail.outlook.com");
        properties.put("mail.smtp.socketFactory.port", "587");
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        sendMail2(properties, username, password, from, to,
                subject, text);
    }

    public static void sendMail2(Properties properties,
                                String username, String password,
                                String fromEmailAddress, String toEmailAddress,
                                String subject, String messageText) {
        Session session = Session.getInstance(properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication
                    getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmailAddress,
                                password);
                    }
                });
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmailAddress));

            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmailAddress));
            msg.setSubject(subject);
            msg.setText(messageText);
            Transport.send(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    public Map<String,Object> sendMail(String from_mail, String to_mail, String body,String subject){
//        Map<String, Object> map = new HashMap<String, Object>();
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setFrom(from_mail);
//            message.setTo(to_mail);
//            message.setText(body);
//            message.setSubject(subject);
//            javaMailSender.send(message);
//            map.put("success",true);
//            map.put("message","Mail Sent Successfully");
//        }
//        catch(Exception e){
//            map.put("success",false);
//            map.put("message",e.getMessage());
//        }
//
//        return map;
//    }

}
