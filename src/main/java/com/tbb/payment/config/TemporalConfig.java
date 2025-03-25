package com.tbb.payment.config;

import com.tbb.payment.activities.FraudCheckActivityImpl;
import com.tbb.payment.activities.NotificationActivityImpl;
import com.tbb.payment.activities.PaymentProcessingActivityImpl;
import com.tbb.payment.constant.Constants;
import com.tbb.payment.workflow.PaymentWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.worker.WorkerOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalConfig {

    // 1. Create WorkflowServiceStubs connect to local temporal server
    @Bean
    public WorkflowServiceStubs workflowServiceStubs() {
        return WorkflowServiceStubs.newLocalServiceStubs();
    }

    // 2. Create WorkflowClient using WorkflowServiceStubs
    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs workflowServiceStubs) {
        return WorkflowClient.newInstance(workflowServiceStubs);
    }

    // 3. Create WorkerFactory using WorkflowClient
    @Bean
    public WorkerFactory workerFactory(WorkflowClient workflowClient) {
        return WorkerFactory.newInstance(workflowClient);
    }

    // 4. Create Worker register WorkflowImplementationTypes & ActivitiesImplementations
    @Bean
    public Worker worker(WorkerFactory workerFactory) {
        // Use WorkerOption to configure thread for specific Worker
        WorkerOptions workerOptions = WorkerOptions.newBuilder()
                // configure polling workflow & activity
                .setMaxConcurrentWorkflowTaskPollers(2)
                .setMaxConcurrentActivityTaskPollers(4)
                // configure executing tasks
                .setMaxConcurrentWorkflowTaskExecutionSize(20)
                .setMaxConcurrentActivityExecutionSize(30)
                .setMaxConcurrentLocalActivityExecutionSize(40)
                .build();
        Worker worker = workerFactory.newWorker(Constants.PAYMENT_TASK_QUEUE, workerOptions);
        worker.registerWorkflowImplementationTypes(PaymentWorkflowImpl.class);
        worker.registerActivitiesImplementations(
                new PaymentProcessingActivityImpl(),
                new FraudCheckActivityImpl(),
                new NotificationActivityImpl()
        );

        // Must start worker factory to activate polling
        workerFactory.start();
        return worker;
    }

}
