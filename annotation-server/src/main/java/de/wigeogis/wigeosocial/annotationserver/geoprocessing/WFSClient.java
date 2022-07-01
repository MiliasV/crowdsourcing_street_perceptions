package de.wigeogis.wigeosocial.annotationserver.geoprocessing;

import com.google.common.io.CharSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringSubstitutor;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.LineIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.locationtech.jts.geom.*;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import org.geotools.referencing.CRS;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;

@Service
public class WFSClient {

    @Value("${cyclomedia.wfs.url}")
    private String url;

    @Value("${cyclomedia.username}")
    private String username;

    @Value("${cyclomedia.password}")
    private String password;


    public List<Map<String, Object>> getFeatures(MultiPolygon path, MultiLineString multiline) throws Exception {

        Point lastPoint = null;

        Map connectionParameters = new HashMap();
        connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", url);
        connectionParameters.put("WFSDataStoreFactory:USERNAME", username);
        connectionParameters.put("WFSDataStoreFactory:PASSWORD", password);
        connectionParameters.put("WFSDataStoreFactory:TIMEOUT", 60000);
        connectionParameters.put("WFSDataStoreFactory:USEDEFAULTSRS", false);


        // Step 2 - connection
        DataStore data = DataStoreFinder.getDataStore(connectionParameters);

        // Step 3 - discovery
        String typeNames[] = data.getTypeNames();
        String typeName = typeNames[0];

        // Step 4 - target
        FeatureSource<SimpleFeatureType, SimpleFeature> source = data.getFeatureSource(typeName);

        CRSAuthorityFactory factory = CRS.getAuthorityFactory(true);
        CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:3857");

        Configuration configuration = new org.geotools.filter.v1_1.OGCConfiguration();
        Parser parser = new Parser( configuration );
        ClassLoader classLoader = new WFSClient().getClass().getClassLoader();
        File file = new File(classLoader.getResource("filters/filter.xml").getFile());

        Envelope envelope = path.getEnvelopeInternal();
        String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        final Map<String, Double> replacementStrings = new HashMap<String, Double>(){{
            put("LEFT", envelope.getMinX());
            put("BOTTOM", envelope.getMinY());
            put("RIGHT", envelope.getMaxX());
            put("TOP", envelope.getMaxY());
        }};
        StringSubstitutor sub = new StringSubstitutor(replacementStrings , "##", "##");
        String result = sub.replace(content);

        InputStream inputStream = CharSource.wrap(result).asByteSource(StandardCharsets.UTF_8).openStream();
        Filter filter = (Filter) parser.parse( inputStream );


        Query query = new Query(typeName, filter, new String[]{});
        query.setCoordinateSystem(crs);
        FeatureCollection<SimpleFeatureType, SimpleFeature> features = source.getFeatures(query);

        FeatureIterator<SimpleFeature> iterator = features.features();
        List<Map<String, Object>> recordings = new ArrayList<>();

        try {
            int index = 1;
            while (iterator.hasNext()) {
                Feature feature = iterator.next();
                if(feature.getProperty("imageId") != null &&
                        feature.getProperty("location") != null &&
                        feature.getProperty("recorderDirection") != null) {
                    Map<String, Object> record = new HashMap<>();
                    Point point = (Point) feature.getProperty("location").getValue();
                    if(point != null && path.contains(point) && point.isWithinDistance(multiline, 10)) {
                        if(lastPoint == null || point.distance(lastPoint) > 10.0) {
                            record.put("index", index);
                            record.put("imageId", feature.getProperty("imageId").getValue().toString());
                            record.put("interval", feature.getProperty("recordedAt").getValue());
                            record.put("lng", point.getX());
                            record.put("lat", point.getY());
                            record.put("direction", feature.getProperty("recorderDirection").getValue());
                            record.put("height", Double.parseDouble(feature.getProperty("height").getValue().toString()));
                            record.put("groundLevelOffset", Double.parseDouble(feature.getProperty("groundLevelOffset").getValue().toString()));
                            recordings.add(record);
                            lastPoint = point;
                            index++;
                        }
                    }
                }
            }
        } finally {
            iterator.close();
        }

        return recordings;
    }
}
