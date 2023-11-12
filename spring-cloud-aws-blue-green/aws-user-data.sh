#!/bin/bash
yum -y update
yum install java-17-amazon-corretto -y
yum install https://s3.amazonaws.com/ec2-downloads-windows/SSMAgent/latest/linux_amd64/amazon-ssm-agent.rpm -y
start amazon-ssm-agent
cd /tmp
wget https://kk-uploads-oregon.s3.amazonaws.com/spring-cloud-aws-environment-demo-17.jar -q
mv *.jar app.jar
java -jar app.jar --server.port=80