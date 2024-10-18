package com.fpt.booking.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public  class  FCMInitializer { 

    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath;

    @Value("${app.firebase-app-name}")
    private String firebaseAppName;

    Logger logger  = LoggerFactory.getLogger(FCMInitializer.class);
    @Bean
    FirebaseMessaging firebaseMessaging() {
        try{
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream());
            FirebaseOptions firebaseOptions = FirebaseOptions
                    .builder()
                    .setCredentials(googleCredentials)
                    .build();
            FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, firebaseAppName);
            return FirebaseMessaging.getInstance(app);
        } catch (IOException e){
            logger.error(e.getMessage());
        }
        return null;
    }
}