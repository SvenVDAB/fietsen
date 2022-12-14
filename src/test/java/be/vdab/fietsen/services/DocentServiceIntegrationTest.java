package be.vdab.fietsen.services;

import be.vdab.fietsen.repositories.DocentRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@Import({ DocentService.class, DocentRepository.class})
@Sql({"/insertCampus.sql", "/insertDocent.sql"})
public class DocentServiceIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final static String DOCENTEN = "docenten";
    private final DocentService service;
    private final EntityManager manager;

    public DocentServiceIntegrationTest(DocentService service, EntityManager manager) {
        this.service = service;
        this.manager = manager;
    }

    private long idVanTestMan() {
        return jdbcTemplate.queryForObject(
                "select id from docenten where voornaam = 'testM'", Long.class
        );
    }

    @Test
    void opslag() {
        var id = idVanTestMan();
        service.opslag(id, BigDecimal.TEN);
        manager.flush();
        assertThat(countRowsInTableWhere(DOCENTEN, "wedde = 1100 and id = " + id)).isOne();
    }
}
