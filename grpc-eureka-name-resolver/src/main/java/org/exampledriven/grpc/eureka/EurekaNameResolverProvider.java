package org.exampledriven.grpc.eureka;

import com.netflix.discovery.EurekaClient;
import io.grpc.Attributes;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import org.exampledriven.grpc.eureka.update.EurekaNameResolverUpdateListener;

import javax.annotation.Nullable;
import java.net.URI;

import static lombok.AccessLevel.PRIVATE;
import static org.exampledriven.grpc.eureka.InstanceInfoExtractor.DEFAULT_INSTANCE_INFO_EXTRACTOR;

@AllArgsConstructor(access = PRIVATE)
@Builder
public class EurekaNameResolverProvider extends NameResolverProvider {

    private static final String DEFAULT_SCHEMA_NAME = "eureka";

    @Builder.Default @NonNull private String schemeName = DEFAULT_SCHEMA_NAME;
    @Builder.Default @NonNull private InstanceInfoExtractor instanceInfoExtractor = DEFAULT_INSTANCE_INFO_EXTRACTOR;
    @NonNull private EurekaClient eurekaClient;
    @NonNull private EurekaNameResolverUpdateListener updateListener;

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 6;
    }

    @Override
    public String getDefaultScheme() {
        return schemeName;
    }

    @Nullable
    @Override
    public NameResolver newNameResolver(URI targetUri, Attributes params) {
        return EurekaNameResolver.builder()
                .eurekaClient(eurekaClient)
                .instanceInfoExtractor(instanceInfoExtractor)
                .serviceName(targetUri.getAuthority())
                .updateListener(updateListener)
                .build();
    }
}
