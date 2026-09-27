package eu.oberon.oss.tools.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ISBN10ValidatorTest {
    static Stream<Arguments> validISBN10Values() {
        return Stream.of(
                Arguments.of("8-9859-2091-X"),
                Arguments.of("3-3512-6585-9"),
                Arguments.of("0-2230-7696-1"),
                Arguments.of("4-7139-5869-7"),
                Arguments.of("0-1900-6113-8"),
                Arguments.of("2-7948-8015-2"),
                Arguments.of("5-5753-2909-7"),
                Arguments.of("1-7053-9224-5"),
                Arguments.of("6-5845-5188-1"),
                Arguments.of("5-2622-0718-9"),
                Arguments.of("9-5829-9904-7"),
                Arguments.of("4-5476-1625-6"),
                Arguments.of("2-8225-1799-1"),
                Arguments.of("7-1476-3193-5"),
                Arguments.of("4-1171-5890-2"),
                Arguments.of("1-1495-2989-X"),
                Arguments.of("8-1744-2464-4"),
                Arguments.of("1-1697-5261-6"),
                Arguments.of("5-7388-0413-9"),
                Arguments.of("7-6672-6965-3")
        );
    }

    @ParameterizedTest
    @MethodSource("validISBN10Values")
    void testValidISBN10Values(String value) {
        assertTrue(CommonValidators.ISBN10.validate(value));
    }

    static Stream<Arguments> invalidISBN10Values() {
        return Stream.of(
                Arguments.of("4-2090-4432-3"),
                Arguments.of("8-5128-0743-9"),
                Arguments.of("1-2284-7217-9"),
                Arguments.of("0-3197-3627-5"),
                Arguments.of("8-0573-8802-X"),
                Arguments.of("4-9215-8489-7"),
                Arguments.of("2-7083-7007-X"),
                Arguments.of("3-1986-1521-5"),
                Arguments.of("6-7643-0632-0"),
                Arguments.of("9-3445-2462-8"),
                Arguments.of("0-2300-2276-8"),
                Arguments.of("1-0952-5853-4"),
                Arguments.of("5-0208-5535-1"),
                Arguments.of("8-6714-4772-0"),
                Arguments.of("4-0951-4036-0"),
                Arguments.of("2-2729-9309-6"),
                Arguments.of("9-5573-5041-9"),
                Arguments.of("6-7597-4209-6"),
                Arguments.of("3-7530-6930-6"),
                Arguments.of("5-3321-2929-9")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidISBN10Values")
    void testInvalidISBN10Values(String value) {
        assertFalse(CommonValidators.ISBN10.validate(value));
    }
}
