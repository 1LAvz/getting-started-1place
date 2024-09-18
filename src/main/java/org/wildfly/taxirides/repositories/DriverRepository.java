package org.wildfly.taxirides.repositories;

import org.wildfly.taxirides.entities.Driver;
import java.util.List;

public interface DriverRepository {

    Driver findById(Long id);

    List<Driver> findAll();

    Driver save(Driver driver);

    void delete(Driver driver);
}