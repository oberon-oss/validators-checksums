package eu.oberon.oss.tools.checksums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class IBANChecksumCalculatorTest {

    private final IBANChecksumCalculator calculator = new IBANChecksumCalculator();

    public static Stream<Arguments> validIBANNumbers() {
        return Stream.of(
                Arguments.of("DE97 8589 8316 4072 0257 45", "858983164072025745131497", 1),
                Arguments.of("DE92 2603 9560 6075 7866 79", "260395606075786679131492", 1),
                Arguments.of("DE76 8038 1453 5735 5753 41", "803814535735575341131476", 1),
                Arguments.of("GB76 0300 1360 6945 0518 14", "030013606945051814161176", 1),
                Arguments.of("FR28 9386 3543 6564 2385 0742 852", "93863543656423850742852152728", 1),
                Arguments.of("IT91 9649 7802 6990 9747 2553 468", "96497802699097472553468182991", 1),
                Arguments.of("ES25 3169 3892 0295 2680 7260", "31693892029526807260142825", 1),
                Arguments.of("NL94 6634 4181 6283 33", "66344181628333232194", 1),
                Arguments.of("BE86 4225 9782 3721", "422597823721111486", 1),
                Arguments.of("CH84 1304 4739 4322 5182 2", "13044739432251822121784", 1),
                Arguments.of("AT12 0378 8068 1130 5163", "0378806811305163102912", 1),
                Arguments.of("PL80 6991 8070 2121 9959 4394 2737", "699180702121995943942737252180", 1),
                Arguments.of("SE68 8146 8052 8140 7622 9337", "81468052814076229337281468", 1),
                Arguments.of("DK27 8331 7884 7122 10", "83317884712210132027", 1),
                Arguments.of("NO76 1027 1562 539", "10271562539232476", 1),
                Arguments.of("FI26 7026 1347 8886 30", "70261347888630151826", 1)
        );
    }

    @Test
    @DisplayName("Should return correct calculator name")
    void shouldReturnCorrectCalculatorName() {
        assertThat(calculator.getCalculatorName()).isEqualTo("IBAN");
    }

    @Test
    @DisplayName("Should pre-process null input by returning empty string")
    void shouldPreProcessNullInput() {
        assertThat(calculator.preProcessor().apply(null)).isEmpty();
    }

    @Test
    @DisplayName("Should pre-process empty or whitespace input by returning empty string")
    void shouldPreProcessEmptyOrWhitespaceInput() {
        assertThat(calculator.preProcessor().apply("")).isEmpty();
        assertThat(calculator.preProcessor().apply("   ")).isEmpty();
        assertThat(calculator.preProcessor().apply("\t \n \r ")).isEmpty();
    }

    @Test
    @DisplayName("Should pre-process input by removing whitespace and converting to uppercase")
    void shouldPreProcessInputRemovingWhitespaceAndConvertingToUppercase() {
        assertThat(calculator.preProcessor().apply("nl94 6634 4181 6283 33")).isEqualTo("NL9466344181628333");
        assertThat(calculator.preProcessor().apply("  de97\t8589 \n 8316 4072 0257 45  ")).isEqualTo("DE97858983164072025745");
        assertThat(calculator.preProcessor().apply("gb76030013606945051814")).isEqualTo("GB76030013606945051814");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "A",
            "NL",
            "NL9"
    })
    @DisplayName("Should return empty string when converting input of length less than 4")
    void shouldReturnEmptyStringWhenInputLengthLessThanFour(String shortInput) {
        assertThat(calculator.converter().apply(shortInput)).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "123456789012345678",           // Starts with digits instead of country code
            "N19466344181628333",           // Country code contains a digit
            "1N9466344181628333",           // Country code contains a digit
            "@A9466344181628333",           // Country code contains special characters
            "NLAA66344181628333",           // Check digits contain letters
            "NL1A66344181628333",           // Check digits contain a letter
            "NLA166344181628333",           // Check digits contain a letter
            "NL94",                         // Exactly 4 characters without BBAN
            "NL94-6634-4181-6283",          // Contains hyphens
            "NL94 6634 4181 6283",          // Contains spaces
            "NL94!6344181628333",           // Contains punctuation
            "nl9466344181628333"            // Lowercase characters in converter input
    })
    @DisplayName("Should return empty string when converting input that does not match IBAN regex format")
    void shouldReturnEmptyStringWhenInputDoesNotMatchIbanFormat(String invalidFormatInput) {
        assertThat(calculator.converter().apply(invalidFormatInput)).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "NL946634418162833",            // NL expects 18, length 17 (too short)
            "NL94663441816283330",          // NL expects 18, length 19 (too long)
            "DE9785898316407202574",        // DE expects 22, length 21 (too short)
            "DE978589831640720257450",      // DE expects 22, length 23 (too long)
            "BE8642259782372",              // BE expects 16, length 15 (too short)
            "BE864225978237210",            // BE expects 16, length 17 (too long)
            "FR289386354365642385074285",   // FR expects 27, length 26 (too short)
            "FR28938635436564238507428520"  // FR expects 27, length 28 (too long)
    })
    @DisplayName("Should return empty string when country IBAN length does not match expected length")
    void shouldReturnEmptyStringWhenLengthDoesNotMatchExpectedLength(String mismatchedLengthInput) {
        assertThat(calculator.converter().apply(mismatchedLengthInput)).isEmpty();
    }

    @Test
    @DisplayName("Should throw NullPointerException when converting unknown country code")
    void shouldThrowWhenCountryCodeIsUnknown() {
        Function<String, String> converter = calculator.converter();
        assertThatThrownBy(() -> converter.apply("ZZ94123456789012")).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should convert valid IBAN into numeric string with rearranged first four characters")
    void shouldConvertValidIBANCorrectly() {
        // NL9466344181628333 -> "66344181628333" + "NL94" -> "66344181628333" + "23" + "21" + "94"
        assertThat(calculator.converter().apply("NL9466344181628333"))
                .isEqualTo("66344181628333232194");

        // DE97858983164072025745 -> "858983164072025745" + "DE97" -> "858983164072025745" + "13" + "14" + "97"
        assertThat(calculator.converter().apply("DE97858983164072025745"))
                .isEqualTo("858983164072025745131497");

        // BE86422597823721 -> "422597823721" + "BE86" -> "422597823721" + "11" + "14" + "86"
        assertThat(calculator.converter().apply("BE86422597823721"))
                .isEqualTo("422597823721111486");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",
            "   ",
            "123A456",
            "-12345",
            "12.34",
            "abc"
    })
    @DisplayName("Should return false when input data is null, empty, or non-numeric")
    void shouldRejectInvalidInputData(String invalidInput) {
        assertFalse(calculator.inputDataValidator().test(invalidInput));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "0",
            "1",
            "97",
            "123456789",
            "66344181628333232194",
            "858983164072025745131497"
    })
    @DisplayName("Should return true when input data is a valid numeric string")
    void shouldAcceptValidNumericInputData(String numericInput) {
        assertTrue(calculator.inputDataValidator().test(numericInput));
    }

    @Test
    @DisplayName("Should calculate modulus 97 checksum correctly")
    void shouldCalculateChecksumCorrectly() {
        assertEquals(0, calculator.checksumCalculator().apply("0"));
        assertEquals(0, calculator.checksumCalculator().apply("97"));
        assertEquals(1, calculator.checksumCalculator().apply("98"));
        assertEquals(1, calculator.checksumCalculator().apply("66344181628333232194"));
        assertEquals(1, calculator.checksumCalculator().apply("858983164072025745131497"));
        assertEquals(1, calculator.checksumCalculator().apply("422597823721111486"));
    }

    @ParameterizedTest
    @MethodSource("validIBANNumbers")
    @DisplayName("Should validate and compute checksum equal to 1 for valid IBANs across different countries")
    void testValidIBANPipeline(String rawIban, String expectedConverted, int expectedRemainder) {
        String preProcessed = calculator.preProcessor().apply(rawIban);
        String converted = calculator.converter().apply(preProcessed);

        assertEquals(expectedConverted, converted);
        assertTrue(calculator.inputDataValidator().test(converted));

        int remainder = calculator.checksumCalculator().apply(converted);
        assertEquals(expectedRemainder, remainder);
    }
}
