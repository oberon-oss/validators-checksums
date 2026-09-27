package eu.oberon.oss.tools.checksums;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ISBN13ChecksumCalculatorTest {

    public static Stream<Arguments> testValidISBN13() {
        return Stream.of(
                Arguments.of("978-190-035948-1", 119),
                Arguments.of("978-370-982494-8", 122),
                Arguments.of("979-249-833159-9", 131),
                Arguments.of("979-567-475098-6", 144),
                Arguments.of("979-573-307779-5", 135),
                Arguments.of("978-831-232560-8", 102),
                Arguments.of("979-522-617726-1", 119),
                Arguments.of("978-720-594797-2", 148),
                Arguments.of("978-362-205377-7", 103),
                Arguments.of("978-448-704873-1", 129),
                Arguments.of("979-919-257294-5", 145),
                Arguments.of("979-904-656687-8", 152),
                Arguments.of("979-011-319063-7", 73),
                Arguments.of("978-630-416733-5", 105),
                Arguments.of("978-485-368411-2", 118),
                Arguments.of("979-852-746852-6", 134),
                Arguments.of("978-306-945195-2", 118),
                Arguments.of("979-427-260719-7", 143),
                Arguments.of("978-719-148156-6", 134),
                Arguments.of("979-772-087528-5", 145)
        );
    }

    private final ChecksumCalculator<String, String, Integer> calculator = new ISBN13ChecksumCalculator();

    @ParameterizedTest
    @MethodSource
    void testValidISBN13(String isbn13, int expectedChecksumResult) {
        String preProcessedString = calculator.preProcessor().apply(isbn13);
        assertTrue(calculator.inputDataValidator().test(preProcessedString));
        Integer calculatedChecksum = calculator.checksumCalculator().apply(preProcessedString);
        assertEquals(expectedChecksumResult, calculatedChecksum);
    }
}