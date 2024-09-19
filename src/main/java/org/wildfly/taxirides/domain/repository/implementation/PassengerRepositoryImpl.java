package org.wildfly.taxirides.domain.repository.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.wildfly.taxirides.domain.entity.Passenger;
import org.wildfly.taxirides.domain.repository.intarface.PassengerRepository;

import java.util.List;

@Transactional
public class PassengerRepositoryImpl implements PassengerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Passenger findById(Long id) {
        return entityManager.find(Passenger.class, id);
    }

    @Override
    public List<Passenger> findAll() {
        return entityManager.createQuery("SELECT p FROM Passenger p", Passenger.class).getResultList();
    }

    @Override
    public List<Passenger> findByIds(List<Long> ids) {
        return entityManager.createQuery("SELECT p FROM Passenger p WHERE p.id IN :ids", Passenger.class)
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