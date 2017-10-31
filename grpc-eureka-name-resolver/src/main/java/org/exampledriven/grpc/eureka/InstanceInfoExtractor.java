package org.exampledriven.grpc.eureka;

import com.netflix.appinfo.InstanceInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.function.Function;
import java.util.function.Predicate;

import static com.netflix.appinfo.InstanceInfo.InstanceStatus.UP;
import static com.netflix.appinfo.InstanceInfo.PortType.SECURE;
import static lombok.AccessLevel.PRIVATE;

@Data @Builder @AllArgsConstructor(access = PRIVATE)
public class InstanceInfoExtractor {

    public static final InstanceInfoExtractor DEFAULT_INSTANCE_INFO_EXTRACTOR = InstanceInfoExtractor.builder().build();

    public static final Function<InstanceInfo, String> DEFAULT_HOST_EXTRACTOR = InstanceInfo::getHostName;

    public static final Function<InstanceInfo, Integer> DEFAULT_PORT_EXTRACTOR =
            instanceInfo -> instanceInfo.isPortEnabled(SECURE) ? instanceInfo.getSecurePort() : instanceInfo.getPort();

    public static final Predicate<? super InstanceInfo> DEFAULT_FILTER = instanceInfo -> instanceInfo.getStatus() == UP;

    @Builder.Default private Predicate<? super InstanceInfo> filter = DEFAULT_FILTER;
    @Builder.Default private Function<InstanceInfo, String> hostExtractor = DEFAULT_HOST_EXTRACTOR;
    @Builder.Default private Function<InstanceInfo, Integer> portExtractor = DEFAULT_PORT_EXTRACTOR;

}
