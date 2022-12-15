package be.vdab.fietsen.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("G")
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
