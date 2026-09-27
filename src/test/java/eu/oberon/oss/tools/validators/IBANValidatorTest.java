package eu.oberon.oss.tools.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class IBANValidatorTest {

    public static Stream<Arguments> simpleTest() {
        return Stream.of(
                Arguments.of("DE97 8589 8316 4072 0257 45"),
                Arguments.of("DE92 2603 9560 6075 7866 79"),
                Arguments.of("DE76 8038 1453 5735 5753 41"),
                Arguments.of("GB76 0300 1360 6945 0518 14"),
                Arguments.of("GB83 1318 0030 8979 0756 92"),
                Arguments.of("GB16 6194 7815 0903 6303 92"),
                Arguments.of("FR28 9386 3543 6564 2385 0742 852"),
                Arguments.of("FR72 2192 9294 1252 7158 3827 820"),
                Arguments.of("FR59 7878 2704 1297 0027 4366 458"),
                Arguments.of("IT91 9649 7802 6990 9747 2553 468"),
                Arguments.of("IT18 7058 7020 5564 9349 4337 566"),
                Arguments.of("IT73 6991 1900 5614 8563 7121 762"),
                Arguments.of("ES25 3169 3892 0295 2680 7260"),
                Arguments.of("ES26 4806 0769 5000 7024 4574"),
                Arguments.of("ES89 3271 7237 7866 0760 6355"),
                Arguments.of("NL94 6634 4181 6283 33"),
                Arguments.of("NL53 3263 4199 0482 22"),
                Arguments.of("NL53 6085 4570 0349 51"),
                Arguments.of("BE86 4225 9782 3721"),
                Arguments.of("BE42 8014 9629 9866"),
                Arguments.of("BE26 2697 1476 3212"),
                Arguments.of("CH84 1304 4739 4322 5182 2"),
                Arguments.of("CH62 7064 8927 1465 2652 7"),
                Arguments.of("CH78 5617 4483 4131 6453 9"),
                Arguments.of("AT12 0378 8068 1130 5163"),
                Arguments.of("AT15 8071 6778 0436 3571"),
                Arguments.of("AT98 1543 4878 1101 8658"),
                Arguments.of("PL80 6991 8070 2121 9959 4394 2737"),
                Arguments.of("PL47 2357 2766 7345 9727 3148 6065"),
                Arguments.of("PL08 2791 7702 9658 9169 6823 1929"),
                Arguments.of("SE68 8146 8052 8140 7622 9337"),
                Arguments.of("SE97 6681 5788 0623 9712 1401"),
                Arguments.of("SE59 7756 3233 6943 9273 7624"),
                Arguments.of("DK27 8331 7884 7122 10"),
                Arguments.of("DK09 6126 3318 8747 97"),
                Arguments.of("DK76 1261 6474 8975 60"),
                Arguments.of("NO76 1027 1562 539"),
                Arguments.of("NO15 5577 8978 807"),
                Arguments.of("NO57 1079 3160 108"),
                Arguments.of("FI26 7026 1347 8886 30"),
                Arguments.of("FI07 9439 5589 7355 47"),
                Arguments.of("FI38 9196 6102 5157 65")
        );
    }

    @ParameterizedTest
    @MethodSource
    void simpleTest(String ibanString) {
        CommonValidators.IBAN.validate(ibanString);
    }
}