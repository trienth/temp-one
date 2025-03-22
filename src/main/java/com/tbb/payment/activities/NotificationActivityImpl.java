package com.tbb.payment.activities;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class NotificationActivityImpl implements NotificationActivity {
    @Override
    @SneakyThrows
    public String sendConfirmation(String paymentId) {
        Thread.sleep(5000);
        // Implement notification logic
        return "Notification sent for " + paymentId;
    }
}
