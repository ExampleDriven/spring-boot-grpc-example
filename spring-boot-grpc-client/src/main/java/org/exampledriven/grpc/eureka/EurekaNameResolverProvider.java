package org.exampledriven.grpc.eureka;

import com.netflix.discovery.EurekaClientConfig;
import io.grpc.Attributes;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;
import javax.annotation.Nullable;
import java.net.URI;

public class EurekaNameResolverProvider extends NameResolverProvider {

    protected static final String EUREKA = "eureka";
    private EurekaClientConfig eurekaClientConfig;
    private final String portMetaData;

    public EurekaNameResolverProvider(EurekaClientConfig eurekaClientConfig) {
        this(eurekaClientConfig, null);
    }

    public EurekaNameResolverProvider(EurekaClientConfig eurekaClientConfig, String portMetaData) {
        this.eurekaClientConfig = eurekaClientConfig;
        this.portMetaData = portMetaData;
    }

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
    public NameResolver newNameResolver(URI targetUri, Attributes params) {
        return new EurekaNameResolver(eurekaClientConfig, targetUri, portMetaData);
    }

    @Override
    public String getDefaultScheme() {
            return EUREKA;
    }
}
