package eu.oberon.oss.tools.validators;

import eu.oberon.oss.tools.checksums.ChecksumCalculator;

import java.util.function.BiPredicate;

/**
 * A final class that implements the {@link Validator} interface for validating {@code String} values using a provided {@link ChecksumCalculator} and a
 * {@link BiPredicate} to determine checksum validity.
 * <p>
 * This class performs validation by:
 * <ul>
 * <li>Preprocessing the input data using the checksum calculator's preprocessor.</li>
 * <li>Converting the preprocessed data using the checksum calculator's converter.</li>
 * <li>Validating the converted data using the checksum calculator's input data validator.</li>
 * <li>Calculating a checksum using the checksum calculator.</li>
 * <li>Determining the validity of the checksum using the provided {@link BiPredicate}.</li>
 * </ul>
 * <p>
 * This implementation is immutable and thread-safe if the provided {@link ChecksumCalculator} and
 * {@link BiPredicate} instances are themselves thread-safe.
 *
 * @see Validator
 * @see ChecksumCalculator
 */
public final class DefaultValidator implements Validator<String> {
    private final ChecksumCalculator<String, String, Integer> checksumCalculator;
    private final BiPredicate<Integer, String> checkSumChecksOutOK;

    /**
     * Constructs a new {@code DefaultValidator} instance with the provided {@link ChecksumCalculator} and {@link BiPredicate}.
     *
     * @param checksumCalculator  the {@link ChecksumCalculator} instance used for processing input data, validating, and calculating checksum values.
     * @param checkSumChecksOutOK a {@link BiPredicate} that determines the validity of the calculated checksum against the expected conditions.
     *
     * @since 1.0.0
     */
    public DefaultValidator(ChecksumCalculator<String, String, Integer> checksumCalculator, BiPredicate<Integer, String> checkSumChecksOutOK) {
        this.checksumCalculator = checksumCalculator;
        this.checkSumChecksOutOK = checkSumChecksOutOK;
    }

    @Override
    public boolean validate(String value) {
        if (value == null) {
            return false;
        }
        String convertedString = checksumCalculator.converter().apply(checksumCalculator.preProcessor().apply(value));
        if (checksumCalculator.inputDataValidator().test(convertedString)) {
            Integer remainder = checksumCalculator.checksumCalculator().apply(convertedString);
            return checkSumChecksOutOK.test(remainder, convertedString);
        }
        return false;
    }
}
