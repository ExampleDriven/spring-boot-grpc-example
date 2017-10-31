package org.exampledriven.grpc.eureka.update;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaEvent;
import com.netflix.discovery.EurekaEventListener;

public class DefaultEurekaUpdateListener implements EurekaNameResolverUpdateListener, EurekaEventListener {

    private final EurekaClient eurekaClient;

    private volatile EurekaNameResolverUpdateHandler updateHandler;

    DefaultEurekaUpdateListener(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    @Override
    public void register(EurekaNameResolverUpdateHandler updateHandler) {
        this.updateHandler = updateHandler;
        eurekaClient.registerEventListener(this);

    }

    @Override
    public void unregister(EurekaNameResolverUpdateHandler updateHandler) {
        this.updateHandler = null;
        eurekaClient.unregisterEventListener(this);

    }

    @Override
    public void onEvent(EurekaEvent event) {
        if(updateHandler != null){
            updateHandler.update();
        }
    }
}
