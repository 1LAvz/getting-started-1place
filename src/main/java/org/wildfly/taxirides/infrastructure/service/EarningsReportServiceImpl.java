package org.wildfly.taxirides.infrastructure.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.wildfly.taxirides.api.dto.input.EarningsReportInput;
import org.wildfly.taxirides.api.dto.output.DriverPassengerCount;
import org.wildfly.taxirides.api.dto.output.EarningsReportOutput;
import org.wildfly.taxirides.domain.entity.Driver;
import org.wildfly.taxirides.domain.entity.Passenger;
import org.wildfly.taxirides.domain.entity.TaxiRide;
import org.wildfly.taxirides.domain.service.EarningsReportService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//TODO REFACT THIS METHOD FOR BETTER CLARITY AND PERFORMANCE
public class EarningsReportServiceImpl implements EarningsReportService {
    @PersistenceContext
    private EntityManager entityManager;

    public List<EarningsReportOutput> reportEarnings(EarningsReportInput input) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EarningsReportOutput> query = cb.createQuery(EarningsReportOutput.class);
        Root<TaxiRide> taxiRide = query.from(TaxiRide.class);
        Join<TaxiRide, Driver> driver = taxiRide.join("driver");

        Subquery<Long> subQueryTotalPassengers = query.subquery(Long.class);
        Root<TaxiRide> subTaxiRide = subQueryTotalPassengers.from(TaxiRide.class);
        Join<TaxiRide, Passenger> subPassengers = subTaxiRide.join("passengers");

        subQueryTotalPassengers.select(cb.count(subPassengers.get("id")))
                .where(cb.equal(subTaxiRide.get("id"), taxiRide.get("id")))
                .groupBy(subTaxiRide.get("id"));

        query.select(cb.construct(
                        EarningsReportOutput.class,
                        driver.get("id"),
                        driver.get("name"),
                        cb.sum(taxiRide.get("cost")),
                        cb.count(taxiRide.get("id")),
                        cb.floor(cb.avg(taxiRide.get("duration"))),
                        cb.sum(subQueryTotalPassengers)
                ))
                .where(
                        cb.between(taxiRide.get("date"), input.getStartDate(), input.getEndDate()),
                        input.getDriverId() != null ? cb.equal(driver.get("id"), input.getDriverId()) : cb.conjunction()
                )
                .groupBy(driver.get("id"));

        TypedQuery<EarningsReportOutput> typedQuery = entityManager.createQuery(query);
        List<EarningsReportOutput> results = typedQuery.getResultList();

        return fetchTotalPassengersUnder18ForEachDriver(results);
    }

    private List<EarningsReportOutput> fetchTotalPassengersUnder18ForEachDriver(List<EarningsReportOutput> results) {
        List<DriverPassengerCount> passengersUnder18 = getTotalPassengersUnder18();
        Map<Long, Long> passengersUnder18Map = passengersUnder18.stream()
                .collect(Collectors.toMap(DriverPassengerCount::getDriverId, DriverPassengerCount::getTotalPassengers));

        results.forEach(output -> {
            Long countUnder18 = passengersUnder18Map.getOrDefault(output.getDriverId(), 0L);
            output.setTotalPassengersUnder18(countUnder18);
        });

        return results;
    }

    public List<DriverPassengerCount> getTotalPassengersUnder18() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DriverPassengerCount> query = cb.createQuery(DriverPassengerCount.class);

        Root<Passenger> passenger = query.from(Passenger.class);

        Join<Passenger, TaxiRide> taxiRide = passenger.join("taxiRides");
        Join<TaxiRide, Driver> driver = taxiRide.join("driver");

        query.select(cb.construct(
                        DriverPassengerCount.class,
                        driver.get("id"),
                        cb.countDistinct(passenger.get("id"))
                ))
                .where(cb.le(passenger.get("age"), 18))
                .groupBy(driver.get("id"));

        return entityManager.createQuery(query).getResultList();
    }
}