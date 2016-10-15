package org.exampledriven.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.internal.DnsNameResolverProvider;
import io.grpc.util.RoundRobinLoadBalancerFactory;
import org.exampledriven.grpc.eureka.EurekaNameResolverProvider;
import org.exampledriven.grpc.services.Book;
import org.exampledriven.grpc.services.BookList;
import org.exampledriven.grpc.services.BookServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class BookServiceGrpcClient {

    private Logger logger = LoggerFactory.getLogger(BookServiceGrpcClient.class);

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    DiscoveryClient discoveryClient;

    private BookServiceGrpc.BookServiceBlockingStub bookServiceBlockingStub;

    public BookList createBooks(List<Book> bookList) {

        BookList.Builder builder = BookList.newBuilder();
        bookList.forEach(builder::addBook);
        BookList request = builder.build();

        logger.debug("Request " + request);
        BookList response = bookServiceBlockingStub.createBooks(request);
        logger.debug("Response " + response);

        return response;

    }

    @PostConstruct
    private void initializeClient() {
//        List<ServiceInstance> instances = discoveryClient.getInstances("grpc-server");
//
//        StringBuilder eurekaServiceList = new StringBuilder();
//        instances.forEach(serviceInstance -> {
//            if (eurekaServiceList.length() > 0) {
//                eurekaServiceList.append(",");
//            }
//            eurekaServiceList.append("dns:///" + serviceInstance.getHost() + ":" + serviceInstance.getPort());
//            logger.debug("Registered client" + serviceInstance.getHost() + ":" + serviceInstance.getPort());
//
//        });

//        ServiceInstance serviceInstance = loadBalancerClient.choose("grpc-server");
//        String host = serviceInstance.getHost();

        ManagedChannel channel = ManagedChannelBuilder
                //.forAddress(host, 6565)
                .forTarget("eureka://127.0.0.1:8761")
                .nameResolverFactory(new EurekaNameResolverProvider(discoveryClient, "grpc-server", "grpc.port"))
                .loadBalancerFactory(RoundRobinLoadBalancerFactory.getInstance())
                .usePlaintext(true)
                .build();


        bookServiceBlockingStub = BookServiceGrpc.newBlockingStub(channel);
    }

}
