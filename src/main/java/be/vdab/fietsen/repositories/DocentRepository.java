package be.vdab.fietsen.repositories;

import be.vdab.fietsen.domain.Docent;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class DocentRepository {
    private final EntityManager manager;

    public DocentRepository(EntityManager manager) {
        this.manager = manager;
    }
    public Optional<Docent> findById(long id) {
        return Optional.ofNullable(manager.find(Docent.class, id));
    }
}
