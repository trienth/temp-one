package com.tbb.payment.activities;

import com.tbb.payment.util.RandomUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class PaymentProcessingActivityImpl implements PaymentProcessingActivity {
    @SneakyThrows
    @Override
    public String processPayment(String paymentId) {
        Thread.sleep(5000);
        // Implement payment processing logic
        return RandomUtils.randomResult();
    }
}
