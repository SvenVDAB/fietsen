package be.vdab.fietsen.repositories;

import be.vdab.fietsen.domain.Docent;
import be.vdab.fietsen.domain.Geslacht;
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
@Sql("/insertDocent.sql")
@Import(DocentRepository.class)
public class DocentRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final DocentRepository repository;
    private final EntityManager manager;
    private static final String DOCENTEN = "docenten";
    private Docent docent;

    public DocentRepositoryTest(DocentRepository repository, EntityManager manager) {
        this.repository = repository;
        this.manager = manager;
    }

    @BeforeEach
    void beforeEach() {
        docent = new Docent("test", "test", BigDecimal.TEN, "test@test.be", Geslacht.MAN);
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
        repository.create(docent);
        assertThat(docent.getId()).isPositive();
        assertThat(countRowsInTableWhere(DOCENTEN, "id = " + docent.getId())).isOne();
    }

    @Test
    void delete() {
        var id = idVanTestMan();
        repository.delete(id);
        manager.flush();
        assertThat(countRowsInTableWhere(DOCENTEN, "id =" + id)).isZero();
    }
}
