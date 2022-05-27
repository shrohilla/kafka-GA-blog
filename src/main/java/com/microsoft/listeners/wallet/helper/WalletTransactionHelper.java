package com.microsoft.listeners.wallet.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.microsoft.commons.Constant;
import com.microsoft.entity.Header;
import com.microsoft.entity.KafkaEntity;
import com.microsoft.entity.event.Notification;
import com.microsoft.entity.event.WalletTransaction;
import com.microsoft.entity.type.NotificationType;

import java.time.LocalDateTime;
import java.util.HashMap;

public class WalletTransactionHelper {

    private static final ObjectMapper mapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    public KafkaEntity buildNotificationEventEntity(WalletTransaction walletTxn) throws JsonProcessingException {
        Notification notification = new Notification.NotificationEventBuilder().
                setEventCode("101").setCustomerId(walletTxn.getCustomerId()).setEmailId("*****@microsoft.com")
                .setMobileNum("+91-0445454").setType(NotificationType.ALL.toString())
                .setTemplateMappingData(new HashMap<String, Object>()).build();
        Header[] headerArr = new Header[3];
        headerArr[0] = new Header(Constant.NOTIFICATION_HEADER_KEY, NotificationType.EMAIL.toString());
        headerArr[1] = new Header(Constant.NOTIFICATION_HEADER_KEY, NotificationType.SMS.toString());
        headerArr[2] = new Header(Constant.NOTIFICATION_HEADER_KEY, NotificationType.INAPP.toString());

        return new KafkaEntity.KafkaEntityBuilder().setOffset(0)
                .setHeaders(headerArr).setPartition(0).setTimestamp(LocalDateTime.now().toString())
                .setValue(mapper.writeValueAsString(notification)).setTopic("notification_event_topic")
                .build();
    }
}
