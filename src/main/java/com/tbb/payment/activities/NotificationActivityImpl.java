package com.tbb.payment.activities;

import com.tbb.payment.util.RandomUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class NotificationActivityImpl implements NotificationActivity {
    @Override
    @SneakyThrows
    public String sendConfirmation(String paymentId) {
        Thread.sleep(5000);
        // Implement notification logic
        return RandomUtils.randomResult();
    }
}
