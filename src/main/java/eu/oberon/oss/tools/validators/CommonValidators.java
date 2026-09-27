package eu.oberon.oss.tools.validators;

import eu.oberon.oss.tools.checksums.ChecksumCalculator;

import java.util.function.BiPredicate;

import static eu.oberon.oss.tools.checksums.AbstractChecksumCalculator.getCalculator;

/**
 * An enumeration of common validators for string-based values, implementing the {@link Validator} interface.
 * <p>
 * This enum provides predefined validation logic for specific formats such as BSN, IBAN, ISBN-10, and ISBN-13. Each validator uses a checksum validation
 * algorithm defined via a {@link BiPredicate} and performs validation using a {@link ChecksumCalculator}.
 * <p>
 * The validators included are:
 * <ul>
 * <li> BSN: Validates using the modulus 11 check.</li>
 * <li> IBAN: Validates that the remainder after checksum calculation equals 1.</li>
 * <li> ISBN-10: Validates using the ISBN-10 checksum rules with a specific check digit calculation.</li>
 * <li> ISBN-13: Validates using the ISBN-13 checksum rules with a specific check digit calculation.</li>
 * </ul>
 * This enum is immutable and thread-safe.
 *
 * @author TigerLilly64
 * @see Validator
 * @see ChecksumCalculator
 * @since 1.0.0
 */
public enum CommonValidators implements Validator<String> {
    /**
     * A validator for the Dutch "Burgerservicenummer" (BSN), which is a citizen service number.
     *
     * @since 1.0.0
     */
    BSN((remainder, _) -> remainder % 11 == 0),
    /**
     * A validator for the International Bank Account Number (IBAN), which is a standardized format for identifying bank accounts across national borders.
     *
     * @since 1.0.0
     */
    IBAN((remainder, _) -> remainder == 1),
    /**
     * A validator for the International Standard Book Number (ISBN), which is a unique numeric commercial book identifier.
     * <p>
     * This validator is intended for the 10-digit ISBN format.
     *
     * @since 1.0.0
     */
    ISBN10((remainder, value) -> {
        int calculatedCheckDigit = (11 - (remainder % 11)) % 11;
        String lastDigit = value.substring(value.length() - 1);
        int actualCheckDigit = lastDigit.contentEquals("X") ? 10 : Integer.parseInt(lastDigit);
        return calculatedCheckDigit == actualCheckDigit;
    }),
    /**
     * A validator for the International Standard Book Number (ISBN), which is a unique numeric commercial book identifier.
     * <p>
     * This validator is intended for the 13-digit ISBN format.
     *
     * @since 1.0.0
     */
    ISBN13((remainder, value) -> {
        int calculatedCheckDigit = (10 - (remainder % 10)) % 10;
        int actualCheckDigit = Integer.parseInt(value.substring(value.length() - 1));
        return calculatedCheckDigit == actualCheckDigit;
    });

    private final BiPredicate<Integer, String> checksumValidator;

    CommonValidators(BiPredicate<Integer, String> checksumValidator) {
        this.checksumValidator = checksumValidator;
    }

    @Override
    public boolean validate(String value) {
        ChecksumCalculator<String, String, Integer> calculator = getCalculator(name());
        if (calculator == null) {
            throw new IllegalStateException("No ChecksumCalculator registered for: " + name());
        }

        return new DefaultValidator(calculator, checksumValidator).validate(value);
    }
}
