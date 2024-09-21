package org.wildfly.taxirides.domain.repository;

import org.wildfly.taxirides.domain.entity.Passenger;

import java.util.List;
import java.util.Set;

public interface PassengerRepository {
    Passenger findById(Long id);

    List<Passenger> findAll();

    List<Passenger> findByIds(Set<Long> ids);

    Passenger save(Passenger passenger);
}
