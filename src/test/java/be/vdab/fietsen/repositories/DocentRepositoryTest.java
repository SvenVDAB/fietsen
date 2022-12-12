package be.vdab.fietsen.repositories;

import be.vdab.fietsen.domain.Geslacht;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@Sql("/insertDocent.sql")
@Import(DocentRepository.class)
public class DocentRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final DocentRepository repository;

    public DocentRepositoryTest(DocentRepository repository) {
        this.repository = repository;
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
}