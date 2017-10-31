package org.exampledriven.grpc.eureka;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import lombok.Builder;
import lombok.NonNull;
import org.exampledriven.grpc.eureka.update.EurekaNameResolverUpdateHandler;
import org.exampledriven.grpc.eureka.update.EurekaNameResolverUpdateListener;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
public class EurekaNameResolver extends NameResolver implements EurekaNameResolverUpdateHandler {
    /*
        Single thread for updates - single queue capacity with discard on overflow - just prevents concurrent updates
        and ensures we never have a backlog building up for updates.
    */
    private final Executor updateExecutor = new ThreadPoolExecutor(1, 1, 0L, MILLISECONDS, new LinkedBlockingQueue<>(1), new DiscardPolicy());
    private volatile boolean shutdown = false;

    @NonNull private final EurekaClient eurekaClient;
    @NonNull private final String serviceName;
    @NonNull private final InstanceInfoExtractor instanceInfoExtractor;
    @NonNull private final EurekaNameResolverUpdateListener updateListener;
    private volatile Listener listener;

    @Builder
    private EurekaNameResolver(EurekaClient eurekaClient, String serviceName, InstanceInfoExtractor instanceInfoExtractor, EurekaNameResolverUpdateListener updateListener) {
        this.eurekaClient = eurekaClient;
        this.serviceName = serviceName;
        this.instanceInfoExtractor = instanceInfoExtractor;
        this.updateListener = updateListener;
    }


    @Override
    public String getServiceAuthority() {
        return serviceName;
    }

    @Override
    public void start(Listener listener) {
        this.listener = listener;
        this.update();
        registerUpdates();
    }

    @Override
    public void refresh() {
        this.update();
    }

    @Override
    public void update() {
        updateExecutor.execute(() -> {
            final List<EquivalentAddressGroup> addressGroupList = getAddresses();
            listener.onAddresses(addressGroupList, Attributes.EMPTY);
        });
    }

    private List<EquivalentAddressGroup> getAddresses() {
        return Optional.ofNullable(eurekaClient.getApplication(serviceName))
                .map(application -> application.getInstances()
                        .stream()
                        .filter(instanceInfoExtractor.getFilter())
                        .map(this::mapper)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    private void registerUpdates(){
        updateListener.register(this);
    }

    private void unregisterUpdates(){
        updateListener.unregister(this);
    }

    protected EquivalentAddressGroup mapper(InstanceInfo instanceInfo){
        final String hostName = instanceInfoExtractor.getHostExtractor().apply(instanceInfo);
        final int port = instanceInfoExtractor.getPortExtractor().apply(instanceInfo);
        final SocketAddress socketAddress = new InetSocketAddress(hostName, port);
        return new EquivalentAddressGroup(socketAddress);
    }

    @Override
    public void shutdown() {
        this.shutdown = true;
        unregisterUpdates();
    }
}
