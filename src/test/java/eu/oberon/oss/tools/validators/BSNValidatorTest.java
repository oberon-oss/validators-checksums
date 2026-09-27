package eu.oberon.oss.tools.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BSNValidatorTest {
    public static Stream<Arguments> testValidBSNNumbers() {
        return Stream.of(
                Arguments.of("959975044"),
                Arguments.of("841749103"),
                Arguments.of("143240031"),
                Arguments.of("746023819"),
                Arguments.of("115598236"),
                Arguments.of("533025783"),
                Arguments.of("748572600"),
                Arguments.of("396269503"),
                Arguments.of("330170983"),
                Arguments.of("660391880"),
                Arguments.of("394072996"),
                Arguments.of("387221141"),
                Arguments.of("166843404"),
                Arguments.of("487208882"),
                Arguments.of("315496575"),
                Arguments.of("590332806"),
                Arguments.of("222226444"),
                Arguments.of("236521792"),
                Arguments.of("812689240"),
                Arguments.of("543100029")
        );
    }

    @ParameterizedTest
    @MethodSource
    void testValidBSNNumbers(String bsnString) {
        assertTrue(CommonValidators.BSN.validate(bsnString));
    }

    public static Stream<Arguments> testInvalidBSNNumbers() {
        return Stream.of(
                Arguments.of("659526531"),
                Arguments.of("934871770"),
                Arguments.of("975397201"),
                Arguments.of("672304586"),
                Arguments.of("830881362"),
                Arguments.of("406820891"),
                Arguments.of("138476363"),
                Arguments.of("176674312"),
                Arguments.of("924658890"),
                Arguments.of("171580656"),
                Arguments.of("152608690"),
                Arguments.of("821008529"),
                Arguments.of("979971700"),
                Arguments.of("931240568"),
                Arguments.of("885596893"),
                Arguments.of("872814722"),
                Arguments.of("147963727"),
                Arguments.of("534141939"),
                Arguments.of("176661476"),
                Arguments.of("361337617")
        );
    }

    @ParameterizedTest
    @MethodSource
    void testInvalidBSNNumbers(String bsnString) {
        assertFalse(CommonValidators.BSN.validate(bsnString));
    }
}