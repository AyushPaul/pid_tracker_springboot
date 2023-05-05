package com.ayush.pidtracker.service;

import com.azure.identity.*;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;

import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import com.skype.ChatMessageAdapter;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.connector.Connector;
import com.skype.connector.ConnectorException;

import okhttp3.Request;
import org.springframework.stereotype.Service;

import javax.mail.event.ConnectionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



@Service
public class OutlookService {

    public static void main(String[] args) throws ConnectorException {
        String clientId = "e5558843-b8d9-4907-aaa0-94083dd8a33e";
        String clientSecret ="LbV8Q~waI~wySHyWhZns9aKxW.xL7GT_MI8O4cdi";
        String tenantId = "f8cdef31-a31e-4b4a-93e4-5f571e91255a";
        String authTenant= "common";
        String userEmail = "btech10487.19@bitmesra.ac.in";
        List<String> scopes= Arrays.asList("https://graph.microsoft.com/.default");
        final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .tenantId(tenantId)
                .build();

        final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes, clientSecretCredential);


        GraphServiceClient<Request> graphClient =
                GraphServiceClient
                        .builder()
                        .authenticationProvider(tokenCredentialAuthProvider)
                        .buildClient();
//        User me = graphClient.users("Ayush Paul")
//                .buildRequest().get();
        User outOfOfficeUser = graphClient.users(userEmail).buildRequest().get();
        if (outOfOfficeUser.mailboxSettings.automaticRepliesSetting != null
                && outOfOfficeUser.mailboxSettings.automaticRepliesSetting.status.equals("scheduled")) {
            System.out.println("User " + userEmail + " is currently out-of-office");
        } else {
            System.out.println("User " + userEmail + " is not out-of-office");
        }
        //System.out.println(me);


//        Skype.setDaemon(false);
//        try {
//            Skype.setDebug(false);
//        } catch (SkypeException e) {
//            throw new RuntimeException(e);
//        }
//        Skype client = new Skype();
//        client.setUser("skype_username");
//        User user = client.getUser("skype_username");
//        System.out.println(user.getStatus());
    }

}
