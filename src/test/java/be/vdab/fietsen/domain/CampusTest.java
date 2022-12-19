package be.vdab.fietsen.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CampusTest {
    private Campus campus1;

    @BeforeEach
    void beforeEach() {
        campus1 = new Campus("test", new Adres("test", "test", "test", "test"));
    }

    @Test
    void eenNieuweCampusHeeftGeenTelefoonnummers() {
        assertThat(campus1.getTelefoonNrs()).isEmpty();
    }

    @Test
    void telefoonnummerToevoegen() {
        assertThat(campus1.addTelefoonNr(new TelefoonNr("003", false, "test"))).isTrue();
        assertThat(campus1.getTelefoonNrs()).containsOnly(new TelefoonNr("003", false, "test"));
    }

    @Test
    void tweeKeerHetzelfdeTelefoonnummerMislukt() {
        campus1.addTelefoonNr(new TelefoonNr("003", true, "test"));
        assertThat(campus1.addTelefoonNr(new TelefoonNr("003", false, "test2")))
                .isFalse();
        assertThat(campus1.getTelefoonNrs())
                .containsOnly(new TelefoonNr("003", true, "test"));
    }

    @Test
    void nullAlsTelefoonnummerMislukt() {
        assertThatNullPointerException().isThrownBy(() -> campus1.addTelefoonNr(null));
    }

    @Test
    void eenLeegTelefoonnummerMislukt() {
        assertThatIllegalArgumentException().isThrownBy(() ->
                campus1.addTelefoonNr(new TelefoonNr("", true, "test")));
    }

    @Test
    void eenTelefoonnummerMetEnkelSpatiesMislukt() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> campus1.addTelefoonNr(new TelefoonNr("", true, "test"))
        );
    }

    @Test
    void bijnaamVerwijderen() {
        campus1.addTelefoonNr(new TelefoonNr("test", false, "test"));
        assertThat(campus1.removeTelefoonNr(new TelefoonNr("test", false, "test"))).isTrue();
        assertThat(campus1.getTelefoonNrs()).isEmpty();
    }

    @Test
    void eenBijnaamVerwijderenDieJeNietToevoegdeMislukt() {
        campus1.addTelefoonNr(new TelefoonNr("005", false, "test"));
        assertThat(campus1.removeTelefoonNr(new TelefoonNr("007", false, "test"))).isFalse();
        assertThat(campus1.getTelefoonNrs()).containsOnly(new TelefoonNr("005", false, "test"));
    }
}






