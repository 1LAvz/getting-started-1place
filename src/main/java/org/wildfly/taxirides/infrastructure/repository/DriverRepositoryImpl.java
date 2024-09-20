package org.wildfly.taxirides.infrastructure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.wildfly.taxirides.domain.entity.Driver;
import org.wildfly.taxirides.domain.repository.DriverRepository;

import java.util.List;

public class DriverRepositoryImpl implements DriverRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Driver findById(Long id) {
        return entityManager.find(Driver.class, id);
    }

    @Override
    public List<Driver> findAll() {
        return entityManager.createQuery("SELECT d FROM Driver d", Driver.class).getResultList();
    }

    @Override
    public Driver save(Driver driver) {
        if (driver.getId() == null) {
            entityManager.persist(driver);
            return driver;
        } else {
            return entityManager.merge(driver);
        }
    }
}
