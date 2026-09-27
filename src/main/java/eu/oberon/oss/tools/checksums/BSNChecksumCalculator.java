package eu.oberon.oss.tools.checksums;

import java.util.regex.Pattern;


/**
 * The {@code BSNChecksumCalculator} is a checksum calculation utility specifically designed for verifying the Dutch BSN (Burgerservicenummer), a citizen
 * service number.
 * <p>
 * It extends the {@code AbstractChecksumCalculator} to provide a concrete implementation of the checksum logic for BSNs, with predefined preprocessing,
 * validation, and calculation steps.
 * <p>
 * The checksum validation follows the modulus 11 algorithm, which validates a 9-digit number against specific weighted sums, ensuring the input number conforms
 * to BSN standards.
 * <p>
 * Key features of this calculator:
 * <ul>
 * <li> Removes non-digit characters from the input before processing.</li>
 * <li> Pads numeric input with a leading zero if it contains fewer than nine digits.</li>
 * <li> Validates input against a 9-digit numeric format before checksum computation.</li>
 * </ul>
 * <p>
 * Constraints:
 * <ul>
 * <li> The BSN must be exactly 9 digits long after preprocessing.</li>
 * <li> Any non-numeric or improperly formatted input will fail validation.</li>
 * </ul>
 * <p>
 * This implementation leverages the following configuration:
 * <ul>
 * <li> Preprocessor: Strips non-numeric characters.</li>
 * <li> Converter: Ensures the result is a 9-digit string by padding as needed.</li>
 * <li> Validator: Confirms the final string matches the 9-digit numeric pattern.</li>
 * <li> Checksum Calculator: Applies the modulus 11 algorithm with a predefined weight table.</li>
 * </ul>
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public class BSNChecksumCalculator extends AbstractChecksumCalculator<String, String, Integer> {
    private static final int[] WEIGHT_TABLE = new int[]{9, 8, 7, 6, 5, 4, 3, 2, -1};
    private static final Pattern INPUT_VALIDATOR_PATTERN = Pattern.compile("\\d{9}");

    /**
     * Constructs a new instance of the {@code BSNChecksumCalculator}.
     *
     * @since 1.0.0
     */
    public BSNChecksumCalculator() {
        super("BSN",
                s -> s.replaceAll("\\D", ""),
                s -> s.length() == 9 ? s : "0" + s,
                s -> INPUT_VALIDATOR_PATTERN.matcher(s).matches(),
                s -> {
                    int sum = 0;
                    for (int i = 0; i < s.length(); i++) {
                        sum += Character.getNumericValue(s.charAt(i)) * WEIGHT_TABLE[i];
                    }
                    return sum;
                }
        );
    }

}
