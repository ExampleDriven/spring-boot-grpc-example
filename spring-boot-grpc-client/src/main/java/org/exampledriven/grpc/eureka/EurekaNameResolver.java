package org.exampledriven.grpc.eureka;

import io.grpc.Attributes;
import io.grpc.NameResolver;
import io.grpc.ResolvedServerInfo;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EurekaNameResolver extends NameResolver {

    private final DiscoveryClient discoveryClient;
    private final String serviceName;
    private final String portMetaData;

    //TODO use this instead of spring specific discoveryClient
    private URI eurekaURI;

    public EurekaNameResolver(URI eurekaURI, DiscoveryClient discoveryClient, String serviceName, String portMetaData) {
        this.eurekaURI = eurekaURI;
        this.discoveryClient = discoveryClient;
        this.serviceName = serviceName;
        this.portMetaData = portMetaData;
    }

    @Override
    public String getServiceAuthority() {
        return eurekaURI.getAuthority();
    }

    @Override
    public void start(Listener listener) {

        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);

        List<ResolvedServerInfo> collect = instances.stream().map(
            serviceInstance -> {
                int port;
                if (portMetaData != null) {
                    String s = serviceInstance.getMetadata().get(portMetaData);
                    port = Integer.parseInt(serviceInstance.getMetadata().get(portMetaData));
                } else {
                    port = serviceInstance.getPort();
                }
                return new ResolvedServerInfo(new InetSocketAddress(serviceInstance.getHost(), port), Attributes.EMPTY);
            }
        ).collect(Collectors.toList());

        listener.onUpdate(Collections.singletonList(collect), Attributes.EMPTY);

    }

    @Override
    public void shutdown() {

    }
}
