package org.exampledriven.grpc.eureka.update;

public interface EurekaNameResolverUpdateListener {

    void register(EurekaNameResolverUpdateHandler updateHandler);
    void unregister(EurekaNameResolverUpdateHandler updateHandler);

}
