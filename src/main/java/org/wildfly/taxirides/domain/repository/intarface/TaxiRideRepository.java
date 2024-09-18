package org.wildfly.taxirides.domain.repository.intarface;

import org.wildfly.taxirides.domain.entity.TaxiRide;

import java.util.List;

public interface TaxiRideRepository {
    TaxiRide findById(Long id);

    List<TaxiRide> findAll();

    TaxiRide save(TaxiRide taxiRide);

    void delete(TaxiRide taxiRide);
}
