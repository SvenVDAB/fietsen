package be.vdab.fietsen.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("I")
public class IndividueleCursus extends Cursus {
    private int duurtijd;

    protected IndividueleCursus() {
    }

    public IndividueleCursus(String naam, int duurtijd) {
        super(naam);
        this.duurtijd = duurtijd;
    }

    public int getDuurtijd() {
        return duurtijd;
    }
}
