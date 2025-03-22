package com.tbb.payment.activities;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface FraudCheckActivity {
    String verifyFraud(String paymentId);
}
