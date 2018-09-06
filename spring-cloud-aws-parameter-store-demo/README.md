This project demonstrates how a Spring Boot application, with the spring cloud AWS libraries, can automatically use the AWS Parameter Store as a property source.  That means you can store properties in the AWS parameter store, and this app will pick them up automatically at startup time.

You can run this demo on an AWS EC2 instance or Elastic Beanstalk, or even a container running in ECS, but its probably easiest to just run locally.  To run locally, you only need your AWS credentials established in your .aws/credential file.  If you commonly use the AWS CLI or SDKs, you've already done this.

Next you need to place some sample parameters in the AWS Parameter Store.  You can do this via the CLI using a command like this:

    aws ssm put-parameter --name /properties/my-demo/sampleKey --value sampleValue --type String

...where "sampleKey" is the key to store, and "sampleValue" is the value you want to associate with it.  The prefix of "/properties/my-demo/" is configurable, see the bootstrap.yml file.

OR if you like, you can set the parameters via the AWS management console.  For example go to https://us-west-2.console.aws.amazon.com/systems-manager/parameters/ for the Oregon region - change to the region that you want to store your parameters in, and make sure you run your code within / pointing to the same region.

This is a web application, so when you run it and go to http://localhost:8080, you will see a web page that simply lists the parameters you have stored.

Take a look at the original Blog post from Joris Kuipers - the original author and Spring framework committer - who authored this integration capability:  https://blog.trifork.com/2018/07/20/integrating-the-aws-parameter-store-with-spring-cloud/
