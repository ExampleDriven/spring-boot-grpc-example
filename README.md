[![Build Status](https://travis-ci.org/ExampleDriven/spring-boot-grcp-example.svg?branch=master)](https://travis-ci.org/ExampleDriven/spring-boot-grcp-example)
## Overview

Example project to for spring-boot integration wiht gRpc. Additonal to a gRpc client and server it has a traditional Spring MVC rest client using very similar payload. The performance of the two technologies can be compared, JMeter file is included.

## Test URLs

Description | URL 
--- | --- 
GRPC client test compact output | http://localhost:8080/test_grpc?compact=true  
GRPC client test verbose output | http://localhost:8080/test_grpc
REST client test compact output | http://localhost:8080/test_rest/compact
REST client test verbose output | http://localhost:8080/test_rest
 
## How to measure performance  
 - The jmeter directory contains the jmeter test definition
 - use the compact endpoints
 - To eliminate "noise" turn off logging by commenting out the appropriate lines in application.yaml both for the server and the client 
 

## Useful resources

- https://www.youtube.com/watch?v=xpmFhTMqWhc
- http://www.ustream.tv/recorded/86187859
- https://github.com/LogNet/grpc-spring-boot-starter
- http://www.grpc.io/docs/
