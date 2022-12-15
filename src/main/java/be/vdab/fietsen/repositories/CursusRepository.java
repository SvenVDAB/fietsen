package be.vdab.fietsen.repositories;

import be.vdab.fietsen.domain.Cursus;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CursusRepository {
    private final EntityManager manager; // constructor met parameter

    public CursusRepository(EntityManager manager) {
        this.manager = manager;
    }

    public Optional<Cursus> findById(long id) {
        return Optional.ofNullable(manager.find(Cursus.class, id));
    }

    public void create(Cursus cursus) {
        manager.persist(cursus);
    }
}
