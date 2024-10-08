package org.wildfly.taxirides.domain.repository;

import org.wildfly.taxirides.domain.entity.Driver;
import java.util.List;

public interface DriverRepository {

    Driver findById(Long id);

    List<Driver> findAll();

    Driver save(Driver driver);
}