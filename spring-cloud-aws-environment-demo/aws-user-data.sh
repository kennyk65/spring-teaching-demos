#!/bin/bash
yum update -y
wget https://s3-us-west-2.amazonaws.com/kk-site/spring-cloud-aws-environment-demo-1.war
java -jar -Dserver.port=80 spring-cloud-aws-environment-demo-1.war
