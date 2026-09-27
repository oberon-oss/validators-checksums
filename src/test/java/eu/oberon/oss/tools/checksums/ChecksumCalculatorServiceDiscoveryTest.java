package eu.oberon.oss.tools.checksums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import static org.assertj.core.api.Assertions.assertThat;

class ChecksumCalculatorServiceDiscoveryTest {

    @Test
    @DisplayName("Should discover ChecksumCalculator implementations via ServiceLoader")
    void shouldDiscoverChecksumCalculatorServices() {
        @SuppressWarnings("rawtypes")
        ServiceLoader<ChecksumCalculator> loader = ServiceLoader.load(ChecksumCalculator.class);

        List<ChecksumCalculator<?, ?, ?>> calculators = new ArrayList<>();
        for (ChecksumCalculator<?, ?, ?> calculator : loader) {
            calculators.add(calculator);
        }

        assertThat(calculators)
                .isNotEmpty()
                .anyMatch(BSNChecksumCalculator.class::isInstance)
                .anyMatch(ISBN10ChecksumCalculator.class::isInstance);

        ChecksumCalculator<?, ?, ?> bsnCalculator = calculators.stream()
                .filter(c -> "BSN".equals(c.getCalculatorName()))
                .findFirst()
                .orElse(null);

        assertThat(bsnCalculator)
                .isNotNull()
                .isInstanceOf(BSNChecksumCalculator.class);

        ChecksumCalculator<?, ?, ?> isbn10Calculator = calculators.stream()
                .filter(c -> "ISBN10".equals(c.getCalculatorName()))
                .findFirst()
                .orElse(null);

        assertThat(isbn10Calculator)
                .isNotNull()
                .isInstanceOf(ISBN10ChecksumCalculator.class);
    }

    @Test
    @DisplayName("Should retrieve ChecksumCalculator by calculator name via AbstractChecksumCalculator")
    void shouldRetrieveCalculatorByNameFromAbstractClass() {
        ChecksumCalculator<?, ?, ?> bsnCalculator = AbstractChecksumCalculator.getCalculator("BSN");
        assertThat(bsnCalculator)
                .isNotNull()
                .isInstanceOf(BSNChecksumCalculator.class);
        assertThat(bsnCalculator.getCalculatorName()).isEqualTo("BSN");

        ChecksumCalculator<?, ?, ?> bsnCaseInsensitive = AbstractChecksumCalculator.getCalculator("bsn");
        assertThat(bsnCaseInsensitive)
                .isNotNull()
                .isInstanceOf(BSNChecksumCalculator.class);

        ChecksumCalculator<?, ?, ?> isbn10Calculator = AbstractChecksumCalculator.getCalculator("ISBN10");
        assertThat(isbn10Calculator)
                .isNotNull()
                .isInstanceOf(ISBN10ChecksumCalculator.class);
        assertThat(isbn10Calculator.getCalculatorName()).isEqualTo("ISBN10");

        ChecksumCalculator<?, ?, ?> isbn10CaseInsensitive = AbstractChecksumCalculator.getCalculator("isbn10");
        assertThat(isbn10CaseInsensitive)
                .isNotNull()
                .isInstanceOf(ISBN10ChecksumCalculator.class);

        ChecksumCalculator<?, ?, ?> viaAlias = AbstractChecksumCalculator.getChecksumCalculator("BSN");
        assertThat(viaAlias).isNotNull();

        ChecksumCalculator<?, ?, ?> viaInstance = AbstractChecksumCalculator.getInstance("BSN");
        assertThat(viaInstance).isNotNull();

        assertThat(AbstractChecksumCalculator.getCalculator("NON_EXISTENT")).isNull();
        assertThat(AbstractChecksumCalculator.getCalculator(null)).isNull();
    }

    @Test
    @DisplayName("Should reload calculators when reload is called explicitly")
    void shouldReloadCalculatorsExplicitly() {
        AbstractChecksumCalculator.reload();
        ChecksumCalculator<?, ?, ?> bsnCalculator = AbstractChecksumCalculator.getCalculator("BSN");
        assertThat(bsnCalculator)
                .isNotNull()
                .isInstanceOf(BSNChecksumCalculator.class);
    }
}
