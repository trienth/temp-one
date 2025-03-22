package com.tbb.payment.activities;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface PaymentProcessingActivity {
    String processPayment(String paymentId);
}
