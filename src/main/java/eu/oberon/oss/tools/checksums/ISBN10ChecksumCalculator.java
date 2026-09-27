package eu.oberon.oss.tools.checksums;

import java.util.regex.Pattern;

/**
 * A specialized implementation of {@link AbstractChecksumCalculator} for calculating ISBN-10 checksums.
 * <p>
 * The {@code ISBN10ChecksumCalculator} performs the following operations:
 * <ul>
 * <li> Pre-processes the input by removing all non-numeric and non-'X' characters.</li>
 * <li> Validates the processed input to ensure it conforms to the ISBN-10 format (9 numeric characters followed by a numeric digit or 'X').</li>
 * <li> Calculates the checksum for the given ISBN-10 numeric input per the weighted sum formula.</li>
 * </ul>
 * <p>
 * Once the checksum is calculated, this data can be further used for validation purposes.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public class ISBN10ChecksumCalculator extends AbstractChecksumCalculator<String, String, Integer> {

    private static final int[] WEIGHT_TABLE = new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final Pattern INPUT_VALIDATOR_PATTERN = Pattern.compile("\\d{9}([\\dX])");

    /**
     * Constructs a new instance of the {@code ISBN10ChecksumCalculator}.
     *
     * @since 1.0.0
     */
    public ISBN10ChecksumCalculator() {
        super("ISBN10",
                s -> s.replaceAll("[^0-9X]", ""),
                s -> s,
                s -> INPUT_VALIDATOR_PATTERN.matcher(s).matches(),
                s -> {
                    int sum = 0;
                    for (int i = 0; i < WEIGHT_TABLE.length; i++) {
                        sum += Character.getNumericValue(s.charAt(i)) * WEIGHT_TABLE[i];
                    }
                    return sum;
                }
        );
    }
}
