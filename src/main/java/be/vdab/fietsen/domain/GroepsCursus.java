package be.vdab.fietsen.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "groepscursussen")
public class GroepsCursus extends Cursus {
    private LocalDate van;
    private LocalDate tot;

    protected GroepsCursus() {
    }

    public GroepsCursus(String naam, LocalDate van, LocalDate tot) {
        super(naam);
        this.van = van;
        this.tot = tot;
    }

    public LocalDate getVan() {
        return van;
    }

    public LocalDate getTot() {
        return tot;
    }
}
