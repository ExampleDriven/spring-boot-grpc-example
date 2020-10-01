package route_guide;

import com.google.protobuf.util.JsonFormat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class RouteGuideUtil {

    private static final double COORD_FACTOR = 1e7;

    /* Gets the latitude for the given point. */
    public static double getLatitude(Point location) {
        return location.getLatitude() / COORD_FACTOR;
    }

    /* Gets the longitude for the given point.*/
    public static double getLongitude(Point location) {
        return location.getLongitude() / COORD_FACTOR;
    }

    /* Indicates whether the given feature exists (i.e. has a valid name) */
    public static boolean exists(Feature feature) {
        return feature != null && !feature.getName().isEmpty();
    }

    /* Gets the default features file from classpath. */
    public static URL getDefaultFeaturesFile() {
        // getResource() method of java Class class is used to return the resources of the module in which this class exists.
        // finds resource relative to the class location, if not found then return NULL
        return RouteGuideServer.class.getResource("route_guide_db.json");
    }

    /* Parses the JSON input file containing the list of features. */
    public static Collection <Feature> parseFeatures(URL file) throws IOException {
        try (InputStream input = file.openStream()) {
            try (Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
                FeatureDatabase.Builder database = FeatureDatabase.newBuilder();
                JsonFormat.parser().merge(reader, database);
                return database.getFeatureList();
            }
        }
    }
}