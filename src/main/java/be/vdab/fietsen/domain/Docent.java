package be.vdab.fietsen.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "docenten")
@NamedEntityGraph(name = Docent.MET_CAMPUS, attributeNodes = @NamedAttributeNode("campus"))
public class Docent {
    public static final String MET_CAMPUS = "Docent.metCampus";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String voornaam;
    private String familienaam;
    private BigDecimal wedde;
    private String emailAdres;
    @Enumerated(EnumType.STRING)
    private Geslacht geslacht;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campusId")
    private Campus campus;
    @ElementCollection
    @CollectionTable(name = "docentenbijnamen",
            joinColumns = @JoinColumn(name = "docentId"))
    @Column(name = "bijnaam")
    private Set<String> bijnamen;

    @ManyToMany(mappedBy = "docenten")
    private Set<Verantwoordelijkheid> verantwoordelijkheden = new LinkedHashSet<>();

    @Version
    private Timestamp versie;

    protected Docent() {
    }

    public Docent(String voornaam, String familienaam,
                  BigDecimal wedde, String emailAdres,
                  Geslacht geslacht, Campus campus) {
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.wedde = wedde;
        this.emailAdres = emailAdres;
        this.geslacht = geslacht;
        this.bijnamen = new LinkedHashSet<>();
        setCampus(campus);
    }

    public long getId() {
        return id;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getFamilienaam() {
        return familienaam;
    }

    public BigDecimal getWedde() {
        return wedde;
    }

    public String getEmailAdres() {
        return emailAdres;
    }

    public Geslacht getGeslacht() {
        return geslacht;
    }

    public Set<String> getBijnamen() {
        //return bijnamen;
        return Collections.unmodifiableSet(bijnamen);
    }

    public Campus getCampus() {
        return campus;
    }

    public Set<Verantwoordelijkheid> getVerantwoordelijkheden() {
        return Collections.unmodifiableSet(verantwoordelijkheden);
    }

    public void setCampus(Campus campus) {
        if(!campus.getDocenten().contains(this)) {
            campus.add(this);
        }
        this.campus = campus;
    }

    public void opslag(BigDecimal percentage) {
        if (percentage.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException();
        }
        var factor = BigDecimal.ONE
                .add(percentage.divide(BigDecimal.valueOf(100)));
        wedde = wedde.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    public boolean addBijnaam(String bijnaam) {
        if (bijnaam.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        return bijnamen.add(bijnaam);
    }

    public boolean removeBijnaam(String bijnaam) {
        return bijnamen.remove(bijnaam);
    }


    public boolean add(Verantwoordelijkheid verantwoordelijkheid) {
        var toegevoegd = verantwoordelijkheden.add(verantwoordelijkheid);
        if (! verantwoordelijkheid.getDocenten().contains(this)) {
            verantwoordelijkheid.add(this);
        }
        return toegevoegd;
    }
    public boolean remove(Verantwoordelijkheid verantwoordelijkheid) {
        var verwijderd = verantwoordelijkheden.remove(verantwoordelijkheid);
        if (verantwoordelijkheid.getDocenten().contains(this)) {
            verantwoordelijkheid.remove(this);
        }
        return verwijderd;
    }

    @Override public boolean equals(Object object) {
        return object instanceof Docent docent && emailAdres.equalsIgnoreCase(docent.emailAdres);
    }
    @Override
    public int hashCode() {
        return emailAdres == null ? 0 : emailAdres.toLowerCase().hashCode();
    }
}
