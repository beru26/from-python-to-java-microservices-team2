package com.javababok.timecalculator;

/**
 * Mapping all route for the API and link with the APIController.
 *
 * @author JavaBabok
 * @version 1.0
 * @since 2017-01-07
 */

import com.javababok.timecalculator.controller.APIController;
import com.javababok.timecalculator.service.APIService;

import static spark.Spark.port;
import static spark.Spark.get;

public class TimeGeneratorService {
    private APIController controller;

    public static void main(String[] args) {
        port(60003);

        TimeGeneratorService application = new TimeGeneratorService();
        application.controller = new APIController(APIService.getInstance());

        // --- MAPPING ---
        //for e.g.: http://0.0.0.0:60003/api/timecalculator/Budapest
        get("/api/timecalculator/:destination", application.controller::location);
    }

}
