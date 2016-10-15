package org.exampledriven.grpc.eureka;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import io.grpc.Attributes;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import javax.annotation.Nullable;
import java.net.URI;

/**
 * Created by Peter_Szanto on 10/13/2016.
 */
public class EurekaNameResolverProvider extends NameResolverProvider {

    protected static final String EUREKA = "eureka";
    private final DiscoveryClient discoveryClient;
    private final String serviceName;
    private final String portMetaData;

    public EurekaNameResolverProvider(DiscoveryClient discoveryClient, String serviceName, String portMetaData) {
        this.discoveryClient = discoveryClient;
        this.serviceName = serviceName;
        this.portMetaData = portMetaData;

//        // create the client
//        ApplicationInfoManager applicationInfoManager = initializeApplicationInfoManager(new MyDataCenterInstanceConfig());
//        EurekaClient client = initializeEurekaClient(applicationInfoManager, new DefaultEurekaClientConfig());
    }

//    private static synchronized EurekaClient initializeEurekaClient(ApplicationInfoManager applicationInfoManager, EurekaClientConfig clientConfig) {
//        if (eurekaClient == null) {
//            eurekaClient = new DiscoveryClient(applicationInfoManager, clientConfig);
//        }
//
//        return eurekaClient;
//    }

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 6;
    }

    @Nullable
    @Override
    public NameResolver newNameResolver(URI eurekaURI, Attributes params) {
        return new EurekaNameResolver(eurekaURI, discoveryClient, serviceName, portMetaData);
    }

    @Override
    public String getDefaultScheme() {
        return EUREKA;
    }
}
