package org.wildfly.taxirides.infrastructure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.wildfly.taxirides.api.dto.input.TaxiRideFilterInput;
import org.wildfly.taxirides.domain.entity.Driver;
import org.wildfly.taxirides.domain.entity.Passenger;
import org.wildfly.taxirides.domain.entity.TaxiRide;
import org.wildfly.taxirides.domain.repository.TaxiRideRepository;

import java.util.List;

public class TaxiRideRepositoryImpl implements TaxiRideRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public TaxiRide findById(Long id) {
        return entityManager.find(TaxiRide.class, id);
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

    @Override
    public List<TaxiRide> findTaxiRides(TaxiRideFilterInput input) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TaxiRide> query = cb.createQuery(TaxiRide.class);
        Root<TaxiRide> taxiRide = query.from(TaxiRide.class);

        Join<TaxiRide, Driver> driverJoin = taxiRide.join("driver", JoinType.INNER);
        Join<TaxiRide, Passenger> passengersJoin = taxiRide.join("passengers", JoinType.INNER);

        Predicate criteria = cb.conjunction();

        if (input.getStartDate() != null) {
            criteria = cb.and(criteria, cb.greaterThanOrEqualTo(taxiRide.get("date"), input.getStartDate()));
        }
        if (input.getEndDate() != null) {
            criteria = cb.and(criteria, cb.lessThanOrEqualTo(taxiRide.get("date"), input.getEndDate()));
        }
        if (input.getMinCost() != null) {
            criteria = cb.and(criteria, cb.greaterThanOrEqualTo(taxiRide.get("cost"), input.getMinCost()));
        }
        if (input.getMaxCost() != null) {
            criteria = cb.and(criteria, cb.lessThanOrEqualTo(taxiRide.get("cost"), input.getMaxCost()));
        }
        if (input.getMinDuration() != null) {
            criteria = cb.and(criteria, cb.greaterThanOrEqualTo(taxiRide.get("duration"), input.getMinDuration()));
        }
        if (input.getMaxDuration() != null) {
            criteria = cb.and(criteria, cb.lessThanOrEqualTo(taxiRide.get("duration"), input.getMaxDuration()));
        }
        if (input.getDriverId() != null) {
            criteria = cb.and(criteria, cb.equal(driverJoin.get("id"), input.getDriverId()));
        }
        if (input.getPassengerId() != null) {
            criteria = cb.and(criteria, cb.equal(passengersJoin.get("id"), input.getPassengerId()));
        }
        if (input.getPassengerAge() != null) {
            criteria = cb.and(criteria, cb.lessThan(passengersJoin.get("age"), input.getPassengerAge()));
        }

        query.select(taxiRide).where(criteria).distinct(true);

        return entityManager.createQuery(query).getResultList();
    }
}