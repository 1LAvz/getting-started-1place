package org.wildfly.taxirides.config;

import java.util.logging.Logger;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class Resources {
    @Produces
    @PersistenceContext
    private EntityManager em;

    @Produces
    public Logger produceLog(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

}