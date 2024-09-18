package org.wildfly.taxirides.domain.repository.intarface;

import org.wildfly.taxirides.domain.entity.Passenger;

import java.util.List;

public interface PassengerRepository {
    Passenger findById(Long id);

    List<Passenger> findAll();

    List<Passenger> findByIds(List<Long> ids);

    Passenger save(Passenger passenger);

    void delete(Passenger passenger);
}
