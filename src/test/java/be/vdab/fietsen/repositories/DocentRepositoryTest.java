package be.vdab.fietsen.repositories;

import be.vdab.fietsen.domain.*;
import be.vdab.fietsen.projections.AantalDocentenPerWedde;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@Sql({"/insertCampus.sql", "/insertverantwoordelijkheid.sql",
        "/insertDocent.sql", "/insertDocentVerantwoordelijkheid.sql"})
@Import(DocentRepository.class)
public class DocentRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final DocentRepository repository;
    private final EntityManager manager;
    private static final String DOCENTEN = "docenten";
    private static final String DOCENTEN_BIJNAMEN = "docentenbijnamen";

    private static final String DOCENTEN_VERANTWOORDELIJKHEDEN = "docentenverantwoordelijkheden";
    private Docent docent;
    private Campus campus;

    public DocentRepositoryTest(DocentRepository repository, EntityManager manager) {
        this.repository = repository;
        this.manager = manager;
    }

    @BeforeEach
    void beforeEach() {
        campus = new Campus("test", new Adres("test", "test", "test", "test"));
        docent = new Docent("test", "test", BigDecimal.TEN, "test@test.be", Geslacht.MAN, campus);
    }

    private long idVanTestMan() {
        return jdbcTemplate.queryForObject(
                "select id from docenten where voornaam = 'testM'", Long.class
        );
    }

    private long idVanTestVrouw() {
        return jdbcTemplate.queryForObject(
                "select id from docenten where voornaam = 'testV'", Long.class
        );
    }

    @Test
    void findById() {
        assertThat(repository.findById(idVanTestMan()))
                .hasValueSatisfying(
                        docent -> assertThat(docent.getVoornaam()).isEqualTo("testM")
                );
    }

    @Test
    void findByOnbestaandeId() {
        assertThat(repository.findById(-1)).isEmpty();
    }

    @Test
    void man() {
        assertThat(repository.findById(idVanTestMan()))
                .hasValueSatisfying(
                        docent -> assertThat(docent.getGeslacht()).isEqualTo(Geslacht.MAN)
                );
    }

    @Test
    void vrouw() {
        assertThat(repository.findById(idVanTestVrouw()))
                .hasValueSatisfying(
                        docent -> assertThat(docent.getGeslacht()).isEqualTo(Geslacht.VROUW)
                );
    }

    @Test
    void create() {
        manager.persist(campus);
        System.out.println(docent.hashCode()); // 0
        repository.create(docent);
        System.out.println(docent.hashCode()); // 865
        manager.flush();
        assertThat(docent.getId()).isPositive();
        assertThat(countRowsInTableWhere(DOCENTEN,
                "id = " + docent.getId() + " and campusId = " + campus.getId())).isOne();

        campus.getDocenten().forEach(docent1 -> System.out.println(docent1.hashCode()));
        assertThat(campus
                .getDocenten()
                .contains(docent))
                .isTrue();
    }

    @Test
    void delete() {
        var id = idVanTestMan();
        repository.delete(id);
        manager.flush();
        assertThat(countRowsInTableWhere(DOCENTEN, "id =" + id)).isZero();
    }

    @Test
    void findAll() {
        assertThat(repository.findAll())
                .hasSize((countRowsInTable(DOCENTEN)))
                .extracting((Docent::getWedde))
                .isSorted();
    }

    @Test
    void findByWeddeBetween() {
        var duizend = BigDecimal.valueOf(1000);
        var tweeduizend = BigDecimal.valueOf(2000);
        var docenten = repository.findByWeddeBetween(duizend, tweeduizend);
        manager.clear();
        assertThat(docenten)
                .hasSize(countRowsInTableWhere(DOCENTEN, "wedde between 1000 and 2000"))
                .allSatisfy(
                        docent -> assertThat(docent.getWedde()).isBetween(duizend, tweeduizend)
                );
        assertThat(docenten)
                .extracting(Docent::getCampus)
                .extracting(Campus::getNaam)
                .isNotNull();
    }

    @Test
    void findEmailAdressen() {
        assertThat(repository.findEmailAdressen())
                .hasSize(countRowsInTable(DOCENTEN))
                .allSatisfy(emailadres -> assertThat(emailadres).contains("@"));
    }

    @Test
    void findByIdsEnEmailAdressen() {
        assertThat(repository.findIdsEnEmailAdressen())
                .hasSize(countRowsInTable(DOCENTEN));
    }

    @Test
    void findGrootsteWedde() {
        assertThat(repository.findGrootsteWedde())
                .isEqualByComparingTo(jdbcTemplate
                        .queryForObject("select max(wedde) from docenten", BigDecimal.class));
    }

    @Test
    void findAantalDocentenPerWedde() {
        var duizend = BigDecimal.valueOf(1000);
        assertThat(repository.findAantalDocentenPerWedde())
                .hasSize(jdbcTemplate.queryForObject("select count(distinct wedde) from docenten", Integer.class))
                .filteredOn(
                        aantalPerWedde -> aantalPerWedde.wedde().compareTo(duizend) == 0
                )
                .singleElement()
                .extracting(AantalDocentenPerWedde::aantal)
                .isEqualTo((long) countRowsInTableWhere(DOCENTEN, "wedde = 1000"));
    }

    @Test
    void algemeneOpslag() {
        assertThat(repository.algemeneOpslag(BigDecimal.TEN))
                .isEqualTo(countRowsInTable(DOCENTEN));
        assertThat(countRowsInTableWhere(DOCENTEN,
                "wedde = 1100 and id = " + idVanTestMan()))
                .isOne();
    }

    @Test
    void bijnamenLezen() {
        assertThat(repository.findById(idVanTestMan()))
                .hasValueSatisfying(
                        docent ->
                                assertThat(docent.getBijnamen())
                                        .containsOnly("test")
                );
    }

    @Test
    void bijnaamToevoegen() {
        manager.persist(campus);
        repository.create(docent);
        docent.addBijnaam("test");
        manager.flush();
        assertThat(countRowsInTableWhere(DOCENTEN_BIJNAMEN,
                "bijnaam = 'test' and docentid = " + docent.getId())).isOne();
    }

    @Test
    void campusLazyLoaded() {
        assertThat(repository.findById(idVanTestMan()))
                .hasValueSatisfying(
                        docent -> assertThat(docent.getCampus().getNaam()).isEqualTo("test")
                );
    }

    @Test
    void verantwoordelijkhedenLezen() {
        assertThat(repository.findById(idVanTestMan()))
                .hasValueSatisfying(docent -> assertThat(docent.getVerantwoordelijkheden())
                        .containsOnly(new Verantwoordelijkheid("test")));
    }

    @Test
    void verantwoordelijkheidToevoegen() {
        var verantwoordelijkheid = new Verantwoordelijkheid("test2");
        manager.persist(verantwoordelijkheid);
        manager.persist(campus);
        repository.create(docent);
        docent.add(verantwoordelijkheid);
        manager.flush();
        assertThat(countRowsInTableWhere(DOCENTEN_VERANTWOORDELIJKHEDEN, "docentId = " + docent.getId()
                + " and verantwoordelijkheidId = " + verantwoordelijkheid.getId())).isOne();
    }
}
