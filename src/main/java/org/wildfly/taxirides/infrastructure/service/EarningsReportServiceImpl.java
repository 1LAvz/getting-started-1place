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

        // Subquery to count total passengers per ride
        Subquery<Long> subQueryTotalPassengers = query.subquery(Long.class);
        Root<TaxiRide> subTaxiRide = subQueryTotalPassengers.from(TaxiRide.class);
        Join<TaxiRide, Passenger> subPassengers = subTaxiRide.join("passengers");

        // Group by taxi ride ID and count passengers
        subQueryTotalPassengers.select(cb.count(subPassengers.get("id")))
                .where(cb.equal(subTaxiRide.get("id"), taxiRide.get("id")))
                .groupBy(subTaxiRide.get("id"));

        // Get total passengers under 18 and store in a Map
        List<DriverPassengerCount> passengersUnder18 = getTotalPassengersUnder18();
        Map<Long, Long> passengersUnder18Map = passengersUnder18.stream()
                .collect(Collectors.toMap(DriverPassengerCount::getDriverId, DriverPassengerCount::getTotalPassengers));

        // Build the main query
        System.out.println("Driver ID: " + driver.get("id"));
        query.select(cb.construct(
                        EarningsReportOutput.class,
                        driver.get("id"),
                        driver.get("name"), // Driver Name
                        cb.sum(taxiRide.get("cost")), // Total Earnings
                        cb.count(taxiRide.get("id")), // Total Taxi Rides
                        cb.floor(cb.avg(taxiRide.get("duration"))), // Average Duration
                        cb.sum(subQueryTotalPassengers) // Total Passengers from the subquery
                ))
                .where(
                        cb.between(taxiRide.get("date"), input.getStartDate(), input.getEndDate()), // Date range condition
                        input.getDriverId() != null ? cb.equal(driver.get("id"), input.getDriverId()) : cb.conjunction() // Optional driver filter
                )
                .groupBy(driver.get("id")); // Group by Driver ID

        // Execute the query
        TypedQuery<EarningsReportOutput> typedQuery = entityManager.createQuery(query);
        List<EarningsReportOutput> results = typedQuery.getResultList();

        // Add passengers under 18 count to each result
        for (EarningsReportOutput output : results) {
            Long driverId = output.getDriverId(); // Ensure you have this getter method
            Long countUnder18 = passengersUnder18Map.getOrDefault(driverId, 0L);
            output.setTotalPassengersUnder18(countUnder18); // Set the count
        }

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
                        cb.countDistinct(passenger.get("id")) // Count of distinct passengers
                ))
                .where(cb.le(passenger.get("age"), 18))
                .groupBy(driver.get("id"));

        return entityManager.createQuery(query).getResultList();
    }
}