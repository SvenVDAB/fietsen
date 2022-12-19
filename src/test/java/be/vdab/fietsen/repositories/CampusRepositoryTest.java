package be.vdab.fietsen.repositories;

import be.vdab.fietsen.domain.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@Import(CampusRepository.class)
@Sql("/insertCampus.sql")
public class CampusRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String CAMPUSSEN = "campussen";

    private static final String CAMPUSSENTELEFOONNRS = "campussentelefoonnrs";
    private final CampusRepository repository;
    private final EntityManager manager;
    private Campus campus;

    public CampusRepositoryTest(CampusRepository repository, EntityManager manager) {
        this.repository = repository;
        this.manager = manager;
    }

    private long idVanTestCampus() {
        return jdbcTemplate.queryForObject(
                "select id from campussen where naam = 'test'", Long.class
        );
    }

    @BeforeEach
    void beforeEach() {
        campus = new Campus("testSven", new Adres("test", "test", "test", "test"));

    }

    @Test
    void findById() {
        assertThat(repository.findById(idVanTestCampus()))
                .hasValueSatisfying(
                        campus ->
                        {
                            assertThat(campus.getNaam()).isEqualTo("test");
                            assertThat(campus.getAdres().getGemeente()).isEqualTo("test");
                        }
                );
    }

    @Test
    void findByOnbestaandeId() {
        assertThat(repository.findById(-1)).isEmpty();
    }

    @Test
    void create() {
        var campus = new Campus("test", new Adres("test", "test", "test", "test"));
        repository.create(campus);
        assertThat(countRowsInTableWhere(CAMPUSSEN, "id = " + campus.getId())).isOne();
    }

    @Test
    void telefoonNrsLezen() {
        assertThat(repository.findById(idVanTestCampus()))
                .hasValueSatisfying(campus -> assertThat(campus.getTelefoonNrs())
                        .containsOnly(new TelefoonNr("0486960547", false, "test")
                        )
                );
    }

    @Test
    void telefoonNrsToevoegen() {
        repository.create(campus);
        campus.addTelefoonNr(new TelefoonNr("006", false, "testSven"));
        manager.flush();
        assertThat(countRowsInTableWhere(CAMPUSSENTELEFOONNRS,
                "nummer = '006' and campusid = " + campus.getId())).isOne();
    }
}
