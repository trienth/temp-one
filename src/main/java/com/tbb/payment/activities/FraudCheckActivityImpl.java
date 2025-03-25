package com.tbb.payment.activities;

import com.tbb.payment.util.RandomUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class FraudCheckActivityImpl implements FraudCheckActivity {
    @Override
    @SneakyThrows
    public String verifyFraud(String paymentId) {
        // Implement fraud check logic
        Thread.sleep(5000);
        return RandomUtils.randomResult();
    }
}
