package org.wildfly.taxirides.core;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.wildfly.taxirides.api.controller.DriverController;
import org.wildfly.taxirides.api.controller.PassengerController;
import org.wildfly.taxirides.api.controller.TaxiRideController;
import org.wildfly.taxirides.api.exceptionhandler.ApiExceptionHandler;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class GettingStartedApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(ApiExceptionHandler.class);
        classes.add(DriverController.class);
        classes.add(PassengerController.class);
        classes.add(TaxiRideController.class);
        classes.add(GettingStartedApplication.class);
        // Add other resource classes here
        return classes;
    }
}
