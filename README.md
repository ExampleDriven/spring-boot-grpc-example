[![Build Status](https://travis-ci.org/ExampleDriven/spring-boot-grcp-example.svg?branch=master)](https://travis-ci.org/ExampleDriven/spring-boot-grcp-example)

## Test URLs

Description | URL 
--- | --- 
GRPC client test compact output | http://localhost:8080/test_grpc?compact=true  
GRPC client test verbose output | http://localhost:8080/test_grpc
REST client test compact output | http://localhost:8080/test_rest/compact
REST client test verbose output | http://localhost:8080/test_rest
 
## How to do performance test 
 - The jmeter directory contains the jmeter test definition
 - use the compact endpoints
 - turn off logging by disabling the appropriate lines application.yaml both for the server and the client 
 

## Useful resources

- https://www.youtube.com/watch?v=xpmFhTMqWhc
- http://www.ustream.tv/recorded/86187859
- https://github.com/LogNet/grpc-spring-boot-starter
- http://www.grpc.io/docs/