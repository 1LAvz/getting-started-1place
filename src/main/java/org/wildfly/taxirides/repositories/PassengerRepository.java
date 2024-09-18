package org.wildfly.taxirides.repositories;

import org.wildfly.taxirides.entities.Passenger;

import java.util.List;

public interface PassengerRepository {
    Passenger findById(Long id);

    List<Passenger> findAll();

    List<Passenger> findByIds(List<Long> ids);

    Passenger save(Passenger passenger);

    void delete(Passenger passenger);
}
