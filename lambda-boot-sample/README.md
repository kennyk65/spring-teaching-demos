A Spring Boot application that can be deployed and run as an AWS Lambda function.

The first invocation ordinarily encounters a delay as the Spring application context must be loaded.  Subsequent invocations (within an AWS-determined time frame, minutes?) re-use the same application context and run far more quickly.

