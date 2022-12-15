package be.vdab.fietsen.domain;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "cursussen")
@DiscriminatorColumn(name = "soort")
public abstract class Cursus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String naam;

    protected Cursus() {
    }

    public Cursus(String naam) {
        this.naam = naam;
    }

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }
}