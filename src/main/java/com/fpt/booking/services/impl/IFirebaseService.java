package com.fpt.booking.services.impl;

import com.fpt.booking.domain.entities.FirebaseDevice;
import com.fpt.booking.domain.entities.FirebaseNotification;
import com.fpt.booking.repository.FirebaseDeviceRepository;
import com.fpt.booking.services.FirebaseService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class IFirebaseService extends BaseService implements FirebaseService {

    private final FirebaseMessaging firebaseMessaging;

    private final FirebaseDeviceRepository firebaseDeviceRepository;

    public void sendPushNotification(FirebaseDevice device, FirebaseNotification firebaseNotification) {
        try {
            Notification notification = Notification
                    .builder()
                    .setTitle(firebaseNotification.getSubject())
                    .setBody(firebaseNotification.getContent())
                    .setImage(firebaseNotification.getImage())
                    .build();

            Message message = Message
                    .builder()
                    .setToken(device.getDeviceToken())
                    .setNotification(notification)
                    .putAllData(firebaseNotification.getData() != null ? firebaseNotification.getData() : new HashMap<>())
                    .build();


            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException ignored) {}
    }

    public void sendPushNotificationToTopic(String topic, FirebaseNotification firebaseNotification) {
        try {
            Notification notification = Notification
                    .builder()
                    .setTitle(firebaseNotification.getSubject())
                    .setBody(firebaseNotification.getContent())
                    .setImage(firebaseNotification.getImage())
                    .build();

            Message message = Message
                    .builder()
                    .setTopic(topic)
                    .setNotification(notification)
                    .putAllData(firebaseNotification.getData())
                    .build();


            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException ignored) {}
    }

    @Override
    public void sendNotificationToDevices(Long receiverId, FirebaseNotification firebaseNotification) {
        firebaseNotification.setReceiver(receiverId);
        for (FirebaseDevice device : firebaseDeviceRepository.findByUserId(receiverId)) {
            if (device != null) {
                sendPushNotification(device, firebaseNotification);
            }
        }
    }


}
