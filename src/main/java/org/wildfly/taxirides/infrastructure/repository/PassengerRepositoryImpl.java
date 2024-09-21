package org.wildfly.taxirides.infrastructure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.wildfly.taxirides.domain.entity.Passenger;
import org.wildfly.taxirides.domain.repository.PassengerRepository;

import java.util.List;

public class PassengerRepositoryImpl implements PassengerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Passenger findById(Long id) {
        return entityManager.find(Passenger.class, id);
    }

    @Override
    public List<Passenger> findAll() {
        return entityManager.createNamedQuery("Passenger.findAll", Passenger.class).getResultList();
    }

    @Override
    public List<Passenger> findByIds(List<Long> ids) {
        return entityManager.createNamedQuery("Passenger.findByIds", Passenger.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public Passenger save(Passenger passenger) {
        if (passenger.getId() == null) {
            entityManager.persist(passenger);
            return passenger;
        } else {
            return entityManager.merge(passenger);
        }
    }
}