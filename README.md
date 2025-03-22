## Create Spring Boot Payment service
Use Temporal Spring Starter 1.27.0
Create Workflow with 3 separate ActivityInterface
Configure WorkflowServiceStubs, WorkflowClient, WorkerFactory, Worker bean
Create API to start workflow

## Notes
* Connect to Temporal server running locally at `localhost:7233`
* Must start WorkerFactory to active polling
* Workflow use single task queue "PAYMENT_TASK_QUEUE"
