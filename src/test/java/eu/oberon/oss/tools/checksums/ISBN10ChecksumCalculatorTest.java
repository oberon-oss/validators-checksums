package eu.oberon.oss.tools.checksums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ISBN10ChecksumCalculatorTest {

    private final ISBN10ChecksumCalculator calculator = new ISBN10ChecksumCalculator();

    public static Stream<Arguments> validISBN10Numbers() {
        return Stream.of(
                Arguments.of("8-7829-3608-0", 330),
                Arguments.of("0-5581-3469-6", 214),
                Arguments.of("8-4319-7135-5", 259),
                Arguments.of("0-4234-5492-7", 169),
                Arguments.of("2-2720-2302-9", 134),
                Arguments.of("6-4886-3304-X", 287),
                Arguments.of("0-7139-4317-3", 195),
                Arguments.of("7-3888-1299-7", 323),
                Arguments.of("0-6473-1182-8", 190),
                Arguments.of("3-0875-8922-5", 259),
                Arguments.of("2-5623-7233-6", 203),
                Arguments.of("4-9689-0590-4", 326),
                Arguments.of("2-3693-7468-3", 261),
                Arguments.of("6-1209-7851-8", 223),
                Arguments.of("5-4739-3360-2", 262),
                Arguments.of("8-2918-3530-6", 269),
                Arguments.of("3-4891-9248-6", 280),
                Arguments.of("7-4134-1637-9", 211),
                Arguments.of("7-5164-1610-X", 221),
                Arguments.of("8-0804-3387-9", 233)
        );
    }

    @Test
    @DisplayName("Should return correct calculator name")
    void shouldReturnCorrectCalculatorName() {
        assertThat(calculator.getCalculatorName()).isEqualTo("ISBN10");
    }

    @Test
    @DisplayName("Should pre-process input by removing non-digits")
    void shouldPreProcessInput() {
        assertThat(calculator.preProcessor().apply("0-306-40615")).isEqualTo("030640615");
        assertThat(calculator.preProcessor().apply("0 306 40615")).isEqualTo("030640615");
    }

    @ParameterizedTest
    @MethodSource("validISBN10Numbers")
    @DisplayName("Checking 20 valid ISBN 10 numbers")
    void testValidISBN10Numbers(String input, int expected) {
        String intermediate;

        intermediate = calculator.preProcessor().apply(input);
        intermediate = calculator.converter().apply(intermediate);
        Integer result = calculator.checksumCalculator().apply(intermediate);
        assertEquals(expected, result);
    }

}
