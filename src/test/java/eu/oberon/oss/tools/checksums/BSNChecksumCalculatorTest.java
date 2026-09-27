package eu.oberon.oss.tools.checksums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BSNChecksumCalculatorTest {

    private final BSNChecksumCalculator calculator = new BSNChecksumCalculator();

    @Test
    @DisplayName("Should return correct calculator name")
    void shouldReturnCorrectCalculatorName() {
        assertThat(calculator.getCalculatorName()).isEqualTo("BSN");
    }

    @Test
    @DisplayName("Should pre-process input by removing non-digits")
    void shouldPreProcessInput() {
        assertThat(calculator.preProcessor().apply("123.456-782")).isEqualTo("123456782");
    }

    @Test
    @DisplayName("Should convert 8-digit input by padding with leading zero")
    void shouldConvertEightDigitInput() {
        assertThat(calculator.converter().apply("12345678")).isEqualTo("012345678");
        assertThat(calculator.converter().apply("123456789")).isEqualTo("123456789");
    }

    @Test
    @DisplayName("Should validate 9-digit input data")
    void shouldValidateInputData() {
        assertThat(calculator.inputDataValidator().test("123456782")).isTrue();
        assertThat(calculator.inputDataValidator().test("12345678")).isFalse();
        assertThat(calculator.inputDataValidator().test("1234567890")).isFalse();
        assertThat(calculator.inputDataValidator().test("abcdefghi")).isFalse();
    }

    @Test
    @DisplayName("Should calculate checksum correctly")
    void shouldCalculateChecksum() {
        // Example: 111222333 -> 1*9 + 1*8 + 1*7 + 2*6 + 2*5 + 2*4 + 3*3 + 3*2 + 3*1
        // = 9 + 8 + 7 + 12 + 10 + 8 + 9 + 6 + 3 = 72; 72 % 11 = 6
        assertThat(calculator.checksumCalculator().apply("111222333")).isEqualTo(66);
    }
}
