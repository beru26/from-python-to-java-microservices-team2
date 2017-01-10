package com.javababok.timecalculator.controller;

import com.javababok.timecalculator.service.APIService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Response;
import spark.Request;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

/**
 * The APIController gets the json from Google and return the shipping time in Ms
 * and the status of the shipping if that is possible to the destination.
 *
 * @author JavaBabok
 * @version  1.0
 * @since 2017-01-07
 */


public class APIController {

    private static APIService apiService;
    // ORIGIN string store the webshop location.
    // TODO: Change origin location, what you want!
    private static final String ORIGIN = "Amsterdam";

    public APIController(APIService apiService){
        APIController.apiService = apiService;
    }

    public JSONObject location (Request request, Response response) throws IOException, URISyntaxException, JSONException {
        return this.getTimeInMs(request.params(":destination"));
    }

    /**
     * This method creates the final json with the output data.
     *
     * @param destination
     * @return json JSONObject with time and status values
     * @throws IOException
     * @throws URISyntaxException
     * @throws JSONException
     */

    public JSONObject getTimeInMs(String destination) throws IOException, URISyntaxException, JSONException {
        JSONObject json = new JSONObject();
        String status = checkStatus(destination);
        // If something went wrong, the time will be 0.
        if(!status.equals("OK")) {
            json.put("time", 0);
            json.put("status", status);
            return json;
        }
        // Here start the work with the jon from Google Maps.
        JSONObject routeDetails = getRouteDetails(destination);
        Integer timeInSec = (Integer) ((JSONObject) routeDetails.get("duration")).get("value");
        // Convert second to millisecond.
        json.put("time", TimeUnit.SECONDS.toMillis(timeInSec));
        json.put("status", status);
        return json;
    }

    /**
     * This method checks the status and expand it, if not OK (or something else).
     *
     * @param destination
     * @return status or error message
     * @throws IOException
     * @throws URISyntaxException
     * @throws JSONException
     */

    public String checkStatus(String destination) throws IOException, URISyntaxException, JSONException {
        JSONObject routeDetails = getRouteDetails(destination);
        String status = routeDetails.get("status").toString();
        switch (status) {
            case "ZERO_RESULTS":
                return "ZERO_RESULTS ERROR: Oversea location!";

            case "NOT_FOUND":
                return "NOT_FOUND ERROR: Place doesn't exist!";
            default:
                return status;
        }
    }

    /**
     * This method gets the json from Google and return it.
     *
     * @param destination
     * @return JSONObject
     * @throws IOException
     * @throws URISyntaxException
     * @throws JSONException
     */

    public JSONObject getRouteDetails(String destination) throws IOException, URISyntaxException, JSONException {
        JSONObject jsonObject = new JSONObject(apiService.calcTime(ORIGIN, destination));
        JSONObject routeDetailsList = (JSONObject) ((JSONArray) jsonObject.get("rows")).get(0);
        return (JSONObject) ((JSONArray) routeDetailsList.get("elements")).get(0);
    }

}
