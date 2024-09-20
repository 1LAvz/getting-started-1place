package org.wildfly.taxirides.infrastructure.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.wildfly.taxirides.api.dto.input.EarningsReportInput;
import org.wildfly.taxirides.api.dto.output.EarningsReportOutput;
import org.wildfly.taxirides.domain.entity.Driver;
import org.wildfly.taxirides.domain.entity.Passenger;
import org.wildfly.taxirides.domain.entity.TaxiRide;
import org.wildfly.taxirides.domain.service.EarningsReportService;
import java.util.List;

public class EarningsReportServiceImpl implements EarningsReportService {
    @PersistenceContext
    private EntityManager entityManager;


    //TODO THIS LOGIC IS WRONG, NEED TO CHECK IT AGAIN AND GET IT RIGHT
    public List<EarningsReportOutput> reportEarnings(EarningsReportInput input) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EarningsReportOutput> query = cb.createQuery(EarningsReportOutput.class);

        // Root of TaxiRide
        Root<TaxiRide> taxiRide = query.from(TaxiRide.class);

        // Joins
        Join<TaxiRide, Driver> driverJoin = taxiRide.join("driver");
        Join<TaxiRide, Passenger> passengerJoin = taxiRide.join("passengers");

        // Criteria for filters
        query.select(cb.construct(
                        EarningsReportOutput.class,
                        driverJoin.get("name"),
                        cb.sum(taxiRide.get("cost")),
                        cb.avg(taxiRide.get("duration")),
                        cb.count(cb.selectCase().when(cb.lessThan(passengerJoin.get("age"), 18), 1).otherwise(0)),
                        cb.count(passengerJoin),
                        cb.count(taxiRide)
                ))
                .where(
                        cb.and(
                                cb.greaterThanOrEqualTo(taxiRide.get("date"), input.getStartDate()),
                                cb.lessThanOrEqualTo(taxiRide.get("date"), input.getEndDate()),
                                input.getDriverId() != null ? cb.equal(driverJoin.get("id"), input.getDriverId()) : cb.conjunction()
                        )
                )
                .groupBy(driverJoin.get("id"));

        // Execute query and return results
        return entityManager.createQuery(query).getResultList();
    }
}
