package org.exampledriven.grpc.eureka;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.shared.Application;
import io.grpc.Attributes;
import io.grpc.NameResolver;
import io.grpc.ResolvedServerInfo;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EurekaNameResolver extends NameResolver {

    private final String serviceName;
    private final String portMetaData;
    private final EurekaClient eurekaClient;

    public EurekaNameResolver(EurekaClientConfig clientConfig, URI targetUri, String portMetaData) {
        this.portMetaData = portMetaData;
        serviceName = targetUri.getAuthority();

        MyDataCenterInstanceConfig instanceConfig = new MyDataCenterInstanceConfig();

        ApplicationInfoManager applicationInfoManager = initializeApplicationInfoManager(instanceConfig);

        eurekaClient = new DiscoveryClient(applicationInfoManager, clientConfig);
    }

    private static synchronized ApplicationInfoManager initializeApplicationInfoManager(EurekaInstanceConfig instanceConfig) {
        InstanceInfo instanceInfo = new EurekaConfigBasedInstanceInfoProvider(instanceConfig).get();
        return new ApplicationInfoManager(instanceConfig, instanceInfo);
    }

    @Override
    public String getServiceAuthority() {
        return serviceName;
    }

    @Override
    public void start(Listener listener) {

        update(listener);

    }

    private void update(Listener listener) {
        Application application = eurekaClient.getApplication(serviceName);

        List<ResolvedServerInfo> resolvedServerInfos = application.getInstances().stream().map(instanceInfo -> {
            int port;
            if (portMetaData != null) {
                String s = instanceInfo.getMetadata().get(portMetaData);
                port = Integer.parseInt(instanceInfo.getMetadata().get(portMetaData));
            } else {
                port = instanceInfo.getPort();
            }
            return new ResolvedServerInfo(new InetSocketAddress(instanceInfo.getHostName(), port), Attributes.EMPTY);
        }).collect(Collectors.toList());

        listener.onUpdate(Collections.singletonList(resolvedServerInfos), Attributes.EMPTY);
    }

    @Override
    public void shutdown() {

    }
}
