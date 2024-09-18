package org.wildfly.taxirides.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.wildfly.taxirides.entities.TaxiRide;

import java.util.List;

@Transactional
public class TaxiRideRepositoryImpl implements TaxiRideRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public TaxiRide findById(Long id) {
        return entityManager.find(TaxiRide.class, id);
    }

    @Override
    public List<TaxiRide> findAll() {
        return entityManager.createQuery("SELECT t FROM TaxiRide t", TaxiRide.class).getResultList();
    }

    @Override
    public TaxiRide save(TaxiRide taxiRide) {
        if (taxiRide.getId() == null) {
            entityManager.persist(taxiRide);
            return taxiRide;
        } else {
            return entityManager.merge(taxiRide);
        }
    }

    @Override
    public void delete(TaxiRide taxiRide) {
        if (entityManager.contains(taxiRide)) {
            entityManager.remove(taxiRide);
        } else {
            entityManager.remove(entityManager.merge(taxiRide));
        }
    }
}