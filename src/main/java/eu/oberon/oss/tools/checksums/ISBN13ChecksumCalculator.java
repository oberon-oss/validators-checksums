package eu.oberon.oss.tools.checksums;

import java.util.regex.Pattern;

/**
 * A concrete implementation of {@link AbstractChecksumCalculator} that provides a checksum calculation mechanism for ISBN-13 identifiers.
 * <p>
 * This calculator validates, processes, and computes the checksum of a 13-digit ISBN based on the standard ISBN-13 checksum algorithm. The ISBN must start with
 * the prefixes "978" or "979" followed by 10 additional numeric characters.
 * <p>
 * The checksum calculation is performed using a weighted sum of the first 12 digits, where digits in odd positions are multiplied by 1 and digits in even
 * positions are multiplied by 3.
 * <p>
 * This class uses the following configurations:
 * <ul>
 *   <li>Calculator Name: "ISBN13"</li>
 *   <li>Pre-Processor: Removes all non-numeric characters from the input.</li>
 *   <li>Input Validator: Verifies that the processed input is a valid 13-character ISBN pattern.</li>
 *   <li>Checksum Calculator: Computes the weighted sum checksum for the first 12 digits.</li>
 * </ul>
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public class ISBN13ChecksumCalculator extends AbstractChecksumCalculator<String, String, Integer> {
    private static final Pattern INPUT_VALIDATOR_PATTERN = Pattern.compile("^(978|979)\\d{10}$");

    /**
     * Constructs an ISBN13ChecksumCalculator instance.
     *
     * @since 1.0.0
     */
    public ISBN13ChecksumCalculator() {
        super("ISBN13",
                s -> s.replaceAll("\\D", ""),
                s -> s,
                s -> INPUT_VALIDATOR_PATTERN.matcher(s).matches(),
                s -> {
                    int sum = 0;
                    for (int i = 0; i < 12; i++) {
                        int digit = Character.getNumericValue(s.charAt(i));
                        sum += (i % 2 == 0) ? digit : digit * 3;
                    }
                    return sum;
                }
        );
    }
}
