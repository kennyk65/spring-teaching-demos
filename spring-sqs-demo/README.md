This is an example Spring Boot / Spring Cloud / Spring Cloud AWS application which demonstrates how to send and receive messages to and from Amazon SQS.

To run:  Download this code, perform a Maven import into Eclipse or IntelliJ.  Build and run.  You will also need to have an AWS Account, and AWS credentials (Access Key / Secret Key) and default region selected.  The easist way to do this is to install the AWS CLI and run the "aws configure" command.  This code will automatically resolve credentials when running on an EC2 instance if an IAM Role is associated with the instance.

You will need to create an SQS Queue in your preferred region.  Standard Queue is fine.  Update the code with the name of the queue.
