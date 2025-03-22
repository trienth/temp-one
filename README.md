## Create Spring Boot Payment service
- Use Temporal Spring Starter 1.27.0
- Create Workflow with 3 separate ActivityInterface
- Configure WorkflowServiceStubs, WorkflowClient, WorkerFactory, Worker bean
- Create API to start workflow

## Notes
* Connect to Temporal server running locally at `localhost:7233`
* Must start WorkerFactory to `active polling`
* Workflow use single task queue `PAYMENT_TASK_QUEUE`

## Verify
``` shell
curl --location --request POST 'http://localhost:8080/api/payments/start?paymentId=ded3fe14-1065-4bdf-833a-beadeaab16b4' \
--header 'Content-Type: application/json'
```