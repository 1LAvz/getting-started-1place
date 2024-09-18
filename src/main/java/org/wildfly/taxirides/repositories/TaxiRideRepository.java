package org.wildfly.taxirides.repositories;

import org.wildfly.taxirides.entities.TaxiRide;

import java.util.List;

public interface TaxiRideRepository {
    TaxiRide findById(Long id);

    List<TaxiRide> findAll();

    TaxiRide save(TaxiRide taxiRide);

    void delete(TaxiRide taxiRide);
}
