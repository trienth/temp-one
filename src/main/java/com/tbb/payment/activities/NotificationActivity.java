package com.tbb.payment.activities;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface NotificationActivity {
    String sendConfirmation(String paymentId);
}