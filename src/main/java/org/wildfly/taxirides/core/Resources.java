package org.wildfly.taxirides.core;

//import java.util.logging.Logger;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Resources {
    @Produces
    @PersistenceContext
    private EntityManager em;

//    @Produces
//    public Logger produceLog(InjectionPoint injectionPoint) {
//        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
//    }

    @Produces
    public Logger produceLog(InjectionPoint injectionPoint) {
        // Use SLF4J LoggerFactory to produce a logger
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

}