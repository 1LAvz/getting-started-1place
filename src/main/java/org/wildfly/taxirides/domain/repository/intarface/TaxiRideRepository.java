package org.wildfly.taxirides.domain.repository.intarface;

import org.wildfly.taxirides.api.dto.input.TaxiRideFilterInput;
import org.wildfly.taxirides.domain.entity.TaxiRide;

import java.util.List;
import java.util.Optional;

public interface TaxiRideRepository {
    TaxiRide findById(Long id);

    TaxiRide save(TaxiRide taxiRide);

    void delete(TaxiRide taxiRide);

    List<TaxiRide> findTaxiRides(TaxiRideFilterInput input);
}
