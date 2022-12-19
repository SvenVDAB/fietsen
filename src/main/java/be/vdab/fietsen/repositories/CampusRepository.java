package be.vdab.fietsen.repositories;

import be.vdab.fietsen.domain.Campus;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CampusRepository {
    private final EntityManager manager;

    public CampusRepository(EntityManager manager) {
        this.manager = manager;
    }

    public Optional<Campus> findById(long id) {
        return Optional.ofNullable(manager.find(Campus.class, id));
    }

    public void create(Campus campus) {
        manager.persist(campus);
    }
}
