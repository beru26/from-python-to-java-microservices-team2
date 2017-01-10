package com.javababok.timecalculator.service;

/**
 * The APIService get the json from Google(maps)
 * and send it to the APIController.
 *
 *
 * @author JavaBabok
 * @version 1.0
 * @since 2017-01-07
 */

import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class APIService {

    private static final String API_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    private static final String API_KEY = "AIzaSyBCZljDDXlcKS3WICsg0hfndUNF2oLevRY";

    public APIService() {}

    private static APIService INSTANCE;

    /**
     * This method investigate if there is already an APIService instance,
     * if not, create new APIService object.
     *
     * @return INSTANCE
     */

    public static APIService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new APIService();
        }
        return INSTANCE;
    }

    /**
     *
     * @param origin The store's original place as sting.
     * @param destination The destination of where the user's address is.
     * @return a builded new URI
     * @throws URISyntaxException
     * @throws IOException
     */

    public String calcTime(String origin, String destination) throws URISyntaxException, IOException {
        URIBuilder builder = new URIBuilder(API_URL);

        builder.addParameter("units", "metric");
        builder.addParameter("origins", origin);
        builder.addParameter("destinations", destination);
        builder.addParameter("key", API_KEY);

        return execute(builder.build());
    }

    /**
     * This method returns a string.
     *
     * @param uri
     * @return uri as string
     * @throws IOException
     */

    private String execute(URI uri) throws IOException {
        return Request.Get(uri).execute().returnContent().asString();
    }
}
