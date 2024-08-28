#!/bin/bash
yum -y update
yum install java-17-amazon-corretto -y
cd /tmp
wget https://kk-uploads-oregon.s3.amazonaws.com/spring-cloud-aws-test-17.war -q
mv *.war app.war
java -jar app.war --server.port=80