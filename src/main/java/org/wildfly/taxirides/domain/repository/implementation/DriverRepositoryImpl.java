package org.wildfly.taxirides.domain.repository.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.wildfly.taxirides.domain.entity.Driver;
import org.wildfly.taxirides.domain.repository.intarface.DriverRepository;

import java.util.List;

@Transactional
public class DriverRepositoryImpl implements DriverRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Driver findById(Long id) {
        return entityManager.find(Driver.class, id);
    }

//    public Driver findById(Long driverId) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Driver> query = cb.createQuery(Driver.class);
//        Root<Driver> driver = query.from(Driver.class);
//        query.select(driver).where(cb.equal(driver.get("id"), driverId));
//
//        return entityManager.createQuery(query).getSingleResult();
//    }

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

    @Override
    public void delete(Driver driver) {
        if (entityManager.contains(driver)) {
            entityManager.remove(driver);
        } else {
            entityManager.remove(entityManager.merge(driver));
        }
    }
}
