package be.vdab.fietsen.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "cursussen")
public abstract class Cursus {
    @Id
    @Column(columnDefinition = "binary(16)")
    private UUID id;
    private String naam;

    protected Cursus() {
    }

    public Cursus(String naam) {
        this.naam = naam;
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }
}
