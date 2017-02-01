# lambda-boot-sample
A sample Spring Boot application that can be deployed and run as an AWS Lambda function.

The first invocation ordinarily encounters a delay as the Spring application context must be loaded.  Subsequent invocations (within an AWS-determined time frame, minutes?) re-use the same application context and run far more quickly.

# How to use
* Build using mvn clean install
* Create an AWS account.  Connect to a region where Lambda is offerred.
* Create a new Lambda function
** Java 8 (or greater)
** Handler: org.springframework.cloud.serverless.aws.ApplicationLambdaEntry
** Memory: 512mb should be enough 
** Timeout:  15 seconds or greater - this accounts for the initial load of the JRE and the application context. 
* Save and run.

