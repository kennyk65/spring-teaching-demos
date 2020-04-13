This is an example Spring Boot / Spring Cloud / Spring Cloud AWS application which demonstrates how to send and receive messages to and from Amazon SQS.

To run:  Download this code, perform a Maven import into Eclipse or IntelliJ.  Build and run.  Open a browser to http://localhost:8080/ - this will send a message to a queue and separately receive a message from the queue, logging all output.

You will also need to have an AWS Account, and AWS credentials (Access Key / Secret Key) and default region selected.  The easist way to do this on any non-AWS hosted computer is to install the AWS CLI and run the "aws configure" command.  When running on an AWS EC2 instance this command is not necessary; it is preferred to associate an IAM role with the instance.

You will need to create an SQS Queue in your preferred region.  Standard Queue is fine.  Update the code with the name of the queue.
