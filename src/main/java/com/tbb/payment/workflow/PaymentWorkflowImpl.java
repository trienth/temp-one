package com.tbb.payment.workflow;

import com.tbb.payment.activities.FraudCheckActivity;
import com.tbb.payment.activities.NotificationActivity;
import com.tbb.payment.activities.PaymentProcessingActivity;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.tbb.payment.constant.Constants.SUCCESS;

public class PaymentWorkflowImpl implements PaymentWorkflow {

    public static final ActivityOptions ACTIVITY_OPTIONS = ActivityOptions.newBuilder()
            .setScheduleToStartTimeout(Duration.of(100, ChronoUnit.MINUTES))
            .setScheduleToCloseTimeout(Duration.of(100, ChronoUnit.MINUTES))
            .build();
    PaymentProcessingActivity paymentActivity = Workflow.newActivityStub(PaymentProcessingActivity.class, ACTIVITY_OPTIONS);
    FraudCheckActivity fraudActivity = Workflow.newActivityStub(FraudCheckActivity.class, ACTIVITY_OPTIONS);
    NotificationActivity notificationActivity = Workflow.newActivityStub(NotificationActivity.class, ACTIVITY_OPTIONS);

    @Override
    public String processPayment(String paymentId) {
        // VI PHẠM: Sử dụng thời gian hệ thống trực tiếp trong workflow
        long currentTime = System.currentTimeMillis();
        String fraudResult = "N/A";
        String paymentResult = "N/A";
        if (currentTime % 2 == 0) {
            paymentResult = paymentActivity.processPayment(paymentId);
        } else {
            fraudResult = fraudActivity.verifyFraud(paymentId);
        }

        String notificationResult = notificationActivity.sendConfirmation(paymentId);
        return String.format("Processed at %d: %s | Fraud Check: %s | Notification: %s",
                currentTime, paymentResult, fraudResult, notificationResult);
    }
}
