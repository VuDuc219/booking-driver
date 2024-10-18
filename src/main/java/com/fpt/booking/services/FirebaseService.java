package com.fpt.booking.services;

import com.fpt.booking.domain.entities.FirebaseDevice;
import com.fpt.booking.domain.entities.FirebaseNotification;

public interface FirebaseService {

    void sendPushNotification(FirebaseDevice device, FirebaseNotification notification) ;

    void sendPushNotificationToTopic(String topic, FirebaseNotification firebaseNotification) ;

    void sendNotificationToDevices(Long receiverId, FirebaseNotification firebaseNotification) ;
}