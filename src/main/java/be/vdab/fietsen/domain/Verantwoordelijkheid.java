package be.vdab.fietsen.domain;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "verantwoordelijkheden")
public class Verantwoordelijkheid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String naam;

    @ManyToMany
    @JoinTable(
            name = "docentenverantwoordelijkheden",
            joinColumns = @JoinColumn(name = "verantwoordelijkheidId"),
            inverseJoinColumns = @JoinColumn(name = "docentId")
    )
    private Set<Docent> docenten = new LinkedHashSet<>();

    protected Verantwoordelijkheid() {
    }

    public Verantwoordelijkheid(String naam) {
        this.naam = naam;
    }

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public Set<Docent> getDocenten() {
        return Collections.unmodifiableSet(docenten);
    }

    public boolean add(Docent docent) {
        var toegevoegd = docenten.add(docent);
        if (! docent.getVerantwoordelijkheden().contains(this)) {
            docent.add(this);
        }
        return toegevoegd;
    }

    public boolean remove(Docent docent) {
        var verwijderd = docenten.remove(docent);
        if (docent.getVerantwoordelijkheden().contains(this)) {
            docent.remove(this);
        }
        return verwijderd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Verantwoordelijkheid that))
            return false;
        return naam.equals(that.naam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(naam);
    }
}
