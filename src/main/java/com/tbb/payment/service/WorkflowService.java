package com.tbb.payment.service;

import com.tbb.payment.constant.Constants;
import com.tbb.payment.workflow.PaymentWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final WorkflowClient workflowClient;

    public void startPaymentWorkflows(String paymentId) {
        PaymentWorkflow workflow = getPaymentWorkflow(paymentId);
        WorkflowClient.execute(workflow::processPayment, paymentId);
    }

    private PaymentWorkflow getPaymentWorkflow(String paymentId) {
        String workflowId = "PAYMENT-" + paymentId;
        return workflowClient.newWorkflowStub(
                PaymentWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue(Constants.PAYMENT_TASK_QUEUE)
                        .setWorkflowId(workflowId)
                        .build());
    }
}
