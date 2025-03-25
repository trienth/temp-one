
# Configure thread for Temporal Worker
Worker dùng các threads để thực hiện 2 nhiệm vụ:
1. Poll Threads: Chuyên thực hiện long-polling để lấy task từ Temporal Server.
2. Worker/Executor Threads: Xử lý logic của Workflow và Activity.

```java
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
```

# Verify Thread Dump when running Worker

## 2 Thread polling Workflow tasks
![Workflow Poller.png](Workflow%20Poller.png)

## 4 Thread polling Activity tasks
![Activity Poller.png](Activity%20Poller.png)

## 20 Thread for Workflow Executor
![Workflow Executor.png](Workflow%20Executor.png)

## 30 Thread for Activity Executor
![Activity Executor.png](Activity%20Executor.png)