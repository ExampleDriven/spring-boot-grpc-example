package org.exampledriven.grpc.eureka.update;

import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;

public class SpringCloudEurekaUpdateListener implements EurekaNameResolverUpdateListener, ApplicationListener<HeartbeatEvent> {

    private final ApplicationEventMulticaster eventMulticaster;

    private volatile EurekaNameResolverUpdateHandler updateHandler;

    public SpringCloudEurekaUpdateListener(ApplicationEventMulticaster eventMulticaster) {
        this.eventMulticaster = eventMulticaster;
    }

    @Override
    public void register(EurekaNameResolverUpdateHandler updateHandler) {
        this.updateHandler = updateHandler;
        eventMulticaster.addApplicationListener(this);
    }

    @Override
    public void unregister(EurekaNameResolverUpdateHandler updateHandler) {
        this.updateHandler = null;
        eventMulticaster.removeApplicationListener(this);
    }

    @Override
    public void onApplicationEvent(HeartbeatEvent heartbeatEvent) {
        if(updateHandler != null){
            updateHandler.update();
        }
    }
}
