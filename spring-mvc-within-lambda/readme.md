

Sometimes its nice to be able to run a Spring Boot web application in a serverless environment on the cloud, such as AWS Lambda with API Gateway.

After all, Python/Flask and NodeJS/Express developers have been used to doing this for years, why not Spring Boot?

AWS Lambda provides a on-demand execution environment (a.k.a. "serverless") for functions written in Java, .NET, Python, Go, NodeJS, and anything else supported by a custom runtime.  When are function is needed (triggered), the Lambda service loads our function and its configuration into memory and executes it.  We pay only for the time and memory used when our code is actually running.  The API Gateway provides a public or private endpoint that we can reach over HTTP/S, and can easily route requests to a Lambda.

Why run Spring Boot on Lambda?  Imagine you have a REST endpoints or a traditional Spring MVC application with a spiky usage pattern - heavy usage at times, but days or weeks of idle time.  We could easily run this workload on EC2 instances, Elastic Beanstalk environments, ECS service/task, or EKS service/pods.  These each require us to pay by the time interval (seconds or hours) that our application is running, even when it is idle.  Even a t2.micro costs $1 to $2 per week to cover the idle periods, and we must fumble with autoscaling to handle the increases in load.

Hosting on AWS Lambda with API Gateway costs essentially $0 if there is no usage, yet can scale to amazing levels without effort.  The advantage is I can have a public (or private) facing web site / REST endpoints which I only pay for when they are executed.  (Of course, not everything is an advantage, keep reading for details.)

Spring was not made to run in such an environment.  It was designed to go through a relatively slow, heavy startup process to load the ApplicationContext, containing all of your dependency-injected objects.  This ApplicationContext would remain in memory for the life of the application, able to instantly respond to requests.  A few seconds of startup time is immaterial for a server-based applications which can run for months, but Lambda can potentially treat each request as a completely new load (cold start), resulting in agonizing performance.

Lambda was not made to run a stateful workload like this.  It was designed for stateless code which starts up fast, serves a request, and goes away.  A Spring ApplicationContext is enormously heavy state when viewed from the serverless perspective.  

But there are lots of potential reasons to retain state between invocations, and Lambda facilitates this via an Execution Context.  Simply put, after the Lambda service loads a function into memory (Firecracker VM) , it keeps it loaded for an intederminate period of time in anticipation of additional invocations.  Eventually it discards it, but while it is loaded Spring's ApplicationContext remains in memory.  This means the inconvenient startup cost of Spring is born on the initial invocation only (Lambda cold start).



created with spring boot initializer with web and thymeleaf included.


didn't like war or shaded jar.  Got class not found exceptions from classloader.

had to copy profiles into maven pom to create zip instead.  These also expected a 'assemblies' xml file to say how to make the zip.  I think lambdas will take a jar just fine, but this was complaining when running locally



can't find my resource at /, but no problem with /hello

main thing that makes this work is aws-serverless-java-container-spring.  


Common sense instructions at https://github.com/awslabs/aws-serverless-java-container/wiki/Quick-start---Spring-Boot2.

Originally I downgraded boot from   <version>2.2.6.RELEASE</version> to  <version>1.5.22.RELEASE</version>  to prevent  	//java.lang.NoClassDefFoundError: org/springframework/boot/context/embedded/EmbeddedServletContainer.  I think the AWS libraries are all oriented towards Spring Boot 1.x.  But then I found they had a boot 2 version.

Using shaded maven jar ran fine on local.

Run locally with:    sam local start-api

Package with:  sam package --template-file template.yml --s3-bucket kk-uploads-oregon --output-template-file out.yml

Deploy with:  sam deploy --template-file /Developer/workspace-aws-advanced-developer/spring-mvc-within-lambda2/out.yml --stack-name spring-in-lambda --capabilities CAPABILITY_IAM
