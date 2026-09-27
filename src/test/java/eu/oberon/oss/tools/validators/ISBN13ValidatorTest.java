package eu.oberon.oss.tools.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ISBN13ValidatorTest {
    static Stream<Arguments> validISBN13Values() {
        return Stream.of(
                Arguments.of("979-364-941108-5"),
                Arguments.of("979-222-669969-4"),
                Arguments.of("978-749-971917-0"),
                Arguments.of("979-106-307471-8"),
                Arguments.of("978-491-368869-2"),
                Arguments.of("979-371-656724-0"),
                Arguments.of("978-566-933722-3"),
                Arguments.of("979-008-022641-4"),
                Arguments.of("978-377-624055-9"),
                Arguments.of("978-262-718059-4"),
                Arguments.of("978-703-610162-8"),
                Arguments.of("979-021-329909-4"),
                Arguments.of("978-016-934483-2"),
                Arguments.of("979-475-877609-6"),
                Arguments.of("978-864-230708-4"),
                Arguments.of("979-033-085494-7"),
                Arguments.of("979-897-775065-3"),
                Arguments.of("978-706-610447-7"),
                Arguments.of("978-838-841244-8"),
                Arguments.of("978-240-495251-2")
        );
    }

    @ParameterizedTest
    @MethodSource("validISBN13Values")
    void testValidISBN10Values(String value) {
        assertTrue(CommonValidators.ISBN13.validate(value));
    }

    static Stream<Arguments> invalidISBN13Values() {
        return Stream.of(
                Arguments.of("971-150-731447-1"),
                Arguments.of("979-170-258733-0"),
                Arguments.of("979-127-647974-8"),
                Arguments.of("979-169-460860-1"),
                Arguments.of("979-108-541367-9"),
                Arguments.of("978-159-198489-3"),
                Arguments.of("978-156-100749-5"),
                Arguments.of("978-190-148370-9"),
                Arguments.of("979-127-452586-6"),
                Arguments.of("978-163-151150-1"),
                Arguments.of("979-189-970003-6"),
                Arguments.of("978-134-491010-2"),
                Arguments.of("979-169-549718-8"),
                Arguments.of("979-161-437117-5"),
                Arguments.of("978-111-054245-3"),
                Arguments.of("979-168-469342-6"),
                Arguments.of("978-117-740389-8"),
                Arguments.of("978-117-569419-0"),
                Arguments.of("978-166-500786-4"),
                Arguments.of("978-183-132209-2")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidISBN13Values")
    void testInvalidISBN13Values(String value) {
        assertFalse(CommonValidators.ISBN13.validate(value));
    }
}
