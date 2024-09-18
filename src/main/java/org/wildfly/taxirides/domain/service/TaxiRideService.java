package org.wildfly.taxirides.domain.service;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.wildfly.taxirides.api.dto.input.CreateTaxiRideDTO;
import org.wildfly.taxirides.domain.entity.Driver;
import org.wildfly.taxirides.domain.entity.Passenger;
import org.wildfly.taxirides.domain.entity.TaxiRide;
import org.wildfly.taxirides.domain.repository.intarface.DriverRepository;
import org.wildfly.taxirides.domain.repository.intarface.PassengerRepository;
import org.wildfly.taxirides.domain.repository.intarface.TaxiRideRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TaxiRideService {

    @Inject
    private EntityManager entityManager;

    @Inject
    private TaxiRideRepository taxiRideRepository;

    @Inject
    private DriverRepository driverRepository;

    @Inject
    private PassengerRepository passengerRepository;

    @Transactional
    public TaxiRide addTaxiRide(CreateTaxiRideDTO input) throws Exception {
        Driver driver = driverRepository.findById(input.getDriverId());
        if (driver == null) {
            throw new Exception("Driver not found");
        }

        List<Passenger> passengers = findPassengers(input.getPassengerIds());

        TaxiRide taxiRide = TaxiRide.builder()
                .cost(input.getCost())
                .duration(input.getDuration())
                .date(input.getDate())
                .driver(driver)
                .passengers(passengers)
                .build();
        taxiRide.setDriver(driver);
        taxiRide.setPassengers(passengers);

        return taxiRideRepository.save(taxiRide);
    }

    private List<Passenger> findPassengers(List<Long> passengersIds) throws Exception {
        List<Passenger> passengers = passengerRepository.findByIds(passengersIds);
        validatePassengers(passengers, passengersIds);

        return passengers;
    }

    public void validatePassengers(List<Passenger> passengers, List<Long> passengersIds) throws Exception {
        if (passengers.isEmpty()) {
            throw new Exception("At least one passenger is required");
        }

        // Extract the list of IDs that were found in the repository
        Set<Long> foundPassengerIds = passengers.stream()
                .map(Passenger::getId) // Assuming Passenger has a getId() method
                .collect(Collectors.toSet());

        // Compare input IDs with found IDs to determine if any are missing
        Set<Long> inputPassengerIds = new HashSet<>(passengersIds); // IDs provided in the input
        inputPassengerIds.removeAll(foundPassengerIds); // This will now only contain the missing IDs

        // If there are any missing IDs, return an error
        if (!inputPassengerIds.isEmpty()) {
            throw new EntityNotFoundException("Passengers not found for IDs: " + inputPassengerIds);
        }
    }

    @Transactional
    public void updateTaxiRide(Long id, CreateTaxiRideDTO taxiRideDTO) throws Exception {
        // Fetch the TaxiRide using NamedQuery
        TaxiRide taxiRide = entityManager.createNamedQuery("TaxiRide.findById", TaxiRide.class)
                .setParameter("id", id)
                .getSingleResult();

        // Update fields
        taxiRide.setCost(taxiRideDTO.getCost());
        taxiRide.setDuration(taxiRideDTO.getDuration());
        taxiRide.setDate(taxiRideDTO.getDate());

        // Fetch driver using JPA Criteria Query
        Driver driver = driverRepository.findById(taxiRideDTO.getDriverId());
        taxiRide.setDriver(driver);

        // Fetch passengers by IDs
        List<Passenger> passengers = passengerRepository.findByIds(taxiRideDTO.getPassengerIds());
        validatePassengers(passengers, taxiRideDTO.getPassengerIds());
        taxiRide.setPassengers(passengers);

        entityManager.merge(taxiRide);
    }


//    public List<TaxiRide> listTaxiRidesWithCriteria(LocalDateTime startDate, LocalDateTime endDate,
//                                                    BigDecimal minCost, BigDecimal maxCost,
//                                                    Integer minDuration, Integer maxDuration,
//                                                    Long driverId, Long passengerId, Integer passengerUnderAge) {
//
//        // Create the CriteriaBuilder and CriteriaQuery
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<TaxiRide> query = cb.createQuery(TaxiRide.class);
//        Root<TaxiRide> taxiRide = query.from(TaxiRide.class);
//        List<Predicate> predicates = new ArrayList<>();
//
//        // Filter by start date
//        if (startDate != null) {
//            predicates.add(cb.greaterThanOrEqualTo(taxiRide.get("date"), startDate));
//        }
//
//        // Filter by end date
//        if (endDate != null) {
//            predicates.add(cb.lessThanOrEqualTo(taxiRide.get("date"), endDate));
//        }
//
//        // Filter by minimum cost
//        if (minCost != null) {
//            predicates.add(cb.greaterThanOrEqualTo(taxiRide.get("cost"), minCost));
//        }
//
//        // Filter by maximum cost
//        if (maxCost != null) {
//            predicates.add(cb.lessThanOrEqualTo(taxiRide.get("cost"), maxCost));
//        }
//
//        // Filter by minimum duration
//        if (minDuration != null) {
//            predicates.add(cb.greaterThanOrEqualTo(taxiRide.get("duration"), minDuration));
//        }
//
//        // Filter by maximum duration
//        if (maxDuration != null) {
//            predicates.add(cb.lessThanOrEqualTo(taxiRide.get("duration"), maxDuration));
//        }
//
//        // Filter by driver
//        if (driverId != null) {
//            predicates.add(cb.equal(taxiRide.get("driver").get("id"), driverId));
//        }
//
//        // Filter by passenger
//        if (passengerId != null) {
//            Join<TaxiRide, Passenger> passengers = taxiRide.join("passengers");
//            predicates.add(cb.equal(passengers.get("id"), passengerId));
//        }
//
//        // Filter by passengers under a certain age
//        if (passengerUnderAge != null) {
//            Join<TaxiRide, Passenger> passengers = taxiRide.join("passengers");
//            predicates.add(cb.lessThan(passengers.get("age"), passengerUnderAge));
//        }
//
//        // Apply the predicates
//        query.where(cb.and(predicates.toArray(new Predicate[0])));
//
//        return entityManager.createQuery(query).getResultList();
//    }

    // Other CRUD operations...
}
