package route_guide;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class RouteGuideServer {

    private static final Logger logger = Logger.getLogger(RouteGuideServer.class.getName());
    private final int port;
    private final Server server;

    public RouteGuideServer(int port) throws IOException {
        this.port = port;
        URL featureFile = RouteGuideUtil.getDefaultFeaturesFile();
        server = ServerBuilder.forPort(port).addService(new RouteGuideService(RouteGuideUtil.parseFeatures(featureFile))).build();
    }

    /* Start serving requests. */
    public void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + port);
        // Add shutDown hook to shut down gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
        }));
    }

    /* Await termination on the main thread since the grpc library uses daemon threads. */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception {
        RouteGuideServer server = new RouteGuideServer(8000);
        server.start();
        server.blockUntilShutdown();
    }
}