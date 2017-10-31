package org.exampledriven.grpc;

import com.netflix.discovery.EurekaClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.util.RoundRobinLoadBalancerFactory;
import org.exampledriven.grpc.eureka.EurekaNameResolverProvider;
import org.exampledriven.grpc.eureka.InstanceInfoExtractor;
import org.exampledriven.grpc.eureka.update.EurekaNameResolverUpdateListener;
import org.exampledriven.grpc.eureka.update.SpringCloudEurekaUpdateListener;
import org.exampledriven.grpc.services.BookServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
public class GrpcConfiguration {

    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private EurekaNameResolverProvider eurekaNameResolverProvider;

    @Autowired
    private ApplicationEventMulticaster eventMulticaster;

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder
                .forTarget("eureka://grpc-server")
                .nameResolverFactory(eurekaNameResolverProvider)
                .loadBalancerFactory(RoundRobinLoadBalancerFactory.getInstance())
                .usePlaintext(true)
                .build();
    }

    @Bean
    public BookServiceGrpc.BookServiceBlockingStub echoServer() {
        return BookServiceGrpc.newBlockingStub(managedChannel());
    }

    @Bean
    public EurekaNameResolverProvider eurekaNameResolverProvider() {
        return EurekaNameResolverProvider.builder()
                .eurekaClient(eurekaClient)
                .updateListener(updateListener())
                .instanceInfoExtractor(instanceInfoExtractor())
                .build();
    }

    @Bean
    public EurekaNameResolverUpdateListener updateListener(){
        return new SpringCloudEurekaUpdateListener(eventMulticaster);
    }

    @Bean
    public InstanceInfoExtractor instanceInfoExtractor() {
        return InstanceInfoExtractor.builder()
                .portExtractor(instanceInfo -> {
                    if (instanceInfo.getMetadata().containsKey("grpc.port")) {
                        return Integer.parseInt(instanceInfo.getMetadata().get("grpc.port"));
                    } else {
                        return InstanceInfoExtractor.DEFAULT_PORT_EXTRACTOR.apply(instanceInfo);
                    }
                })
                .build();
    }
}
