package be.vdab.fietsen.domain;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class TelefoonNr {
    private String nummer;
    private boolean fax;
    private String opmerking;

    protected TelefoonNr() {
    }

    public TelefoonNr(String nummer, boolean fax, String opmerking) {
        this.nummer = nummer;
        this.fax = fax;
        this.opmerking = opmerking;
    }

    public String getNummer() {
        return nummer;
    }

    public boolean isFax() {
        return fax;
    }

    public String getOpmerking() {
        return opmerking;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof TelefoonNr telefoonNr && nummer.equalsIgnoreCase(telefoonNr.nummer);
    }

    @Override
    public int hashCode() {
        return nummer.toUpperCase().hashCode();
    }
}
