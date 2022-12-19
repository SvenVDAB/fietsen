package be.vdab.fietsen.domain;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "campussen")
public class Campus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String naam;
    @Embedded
    private Adres adres;

    @ElementCollection
    @CollectionTable(name = "campussentelefoonnrs",
            joinColumns = @JoinColumn(name = "campusId"))
    @OrderBy("fax")
    private Set<TelefoonNr> telefoonNrs;

    protected Campus() {
    }

    public Campus(String naam, Adres adres) {
        this.naam = naam;
        this.adres = adres;
        this.telefoonNrs = new LinkedHashSet<>();
    }

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public Adres getAdres() {
        return adres;
    }

    public Set<TelefoonNr> getTelefoonNrs() {
        return Collections.unmodifiableSet(telefoonNrs);
    }

    public boolean addTelefoonNr(TelefoonNr telefoonNr) {
        if (telefoonNr.getNummer().trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        return telefoonNrs.add(telefoonNr);
    }

    public boolean removeTelefoonNr(TelefoonNr telefoonNr) {
        return telefoonNrs.remove(telefoonNr);
    }
}