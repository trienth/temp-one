package com.tbb.payment.workflow;

import com.tbb.payment.activities.FraudCheckActivity;
import com.tbb.payment.activities.NotificationActivity;
import com.tbb.payment.activities.PaymentProcessingActivity;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class PaymentWorkflowImpl implements PaymentWorkflow {

    public static final ActivityOptions ACTIVITY_OPTIONS = ActivityOptions.newBuilder()
            .setScheduleToStartTimeout(Duration.of(1, ChronoUnit.MINUTES))
            .setScheduleToCloseTimeout(Duration.of(1, ChronoUnit.MINUTES))
            .build();
    PaymentProcessingActivity paymentActivity = Workflow.newActivityStub(PaymentProcessingActivity.class, ACTIVITY_OPTIONS);
    FraudCheckActivity fraudActivity = Workflow.newActivityStub(FraudCheckActivity.class, ACTIVITY_OPTIONS);
    NotificationActivity notificationActivity = Workflow.newActivityStub(NotificationActivity.class, ACTIVITY_OPTIONS);

    @Override
    public String processPayment(String paymentId) {

        String paymentResult = paymentActivity.processPayment(paymentId);
        String fraudResult = fraudActivity.verifyFraud(paymentId);
        String notificationResult = notificationActivity.sendConfirmation(paymentId);

        return String.format("Processed: %s | Fraud Check: %s | Notification: %s",
                paymentResult, fraudResult, notificationResult);
    }
}
