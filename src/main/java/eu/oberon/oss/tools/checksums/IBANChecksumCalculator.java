package eu.oberon.oss.tools.checksums;

import eu.oberon.oss.tools.bank.IBANCodeTable;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A checksum calculator implementation specifically designed to validate IBAN (International Bank Account Number) formats and calculate the corresponding
 * checksum value.
 * <p>
 * This class handles the preprocessing, format validation, and checksum computation required to verify IBAN correctness.
 * <p>
 * The calculator operates by leveraging the abstract functionality provided by its superclass, {@link AbstractChecksumCalculator}, to perform operations in a
 * broadly customizable way for specialized checksum calculation workflows.
 * <p>
 * Key features of this class:
 * <p>
 * 1. IBAN Preprocessing:
 * <ul>
 * <li> Removes any whitespace from the input string and converts it to uppercase.</li>
 * <li> Rearranges the IBAN structure by moving its first four characters (country code and check digits) to the end.</li>
 * <li> Converts alphabetic characters to numbers according to IBAN standards (A=10, B=11, ..., Z=35).</li>
 * </ul>
 * <p>
 * 2. Validation:
 * <ul>
 * <li> Ensures that the IBAN format conforms to the general standard (2 uppercase letters, followed by 2 digits, followed by alphanumeric characters).</li>
 * <li> Validates the country-specific IBAN length using definitions from the {@link IBANCodeTable}.</li>
 * </ul>
 * 3. Checksum Calculation:
 * <ul>
 * <li> Uses the modulus 97 operation to compute the checksum as per the IBAN specification.</li>
 * </ul>
 * Note that any inconsistency in the input (e.g., incorrect format or mismatched length) results in preprocessing returning an empty string, indicating an
 * invalid IBAN.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public class IBANChecksumCalculator extends AbstractChecksumCalculator<String, String, Integer> {
    private static final BigInteger MODULUS = BigInteger.valueOf(97);
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("\\d+");
    private static final Pattern IBAN_FORMAT = Pattern.compile("^[A-Z]{2}\\d{2}[A-Z0-9]+$");


    private static final IBANCodeTable IBAN_CODE_TABLE;

    static {
        try {
            IBAN_CODE_TABLE = IBANCodeTable.getDefaultInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructs an IBANChecksumCalculator instance.
     *
     * @since 1.0.0
     */
    public IBANChecksumCalculator() {
        super("IBAN",
                input -> input == null ? "" : input.replaceAll("\\s+", "").toUpperCase(),
                IBANChecksumCalculator::preProcesserFunction,
                numericString -> numericString != null && !numericString.isEmpty() && NUMERIC_PATTERN.matcher(numericString).matches(),
                numericString -> new BigInteger(numericString).mod(MODULUS).intValue()
        );
    }

    private static String preProcesserFunction(String iban) {
        if (iban.length() < 4 || !IBAN_FORMAT.matcher(iban).matches()) {
            return "";
        }

        // Validate country-specific length if country is known
        String countryCode = iban.substring(0, 2);
        int expectedLength = Objects.requireNonNull(IBAN_CODE_TABLE.findEntry(countryCode)).bankAccountNumberLength();

        if (iban.length() != expectedLength) {
            return "";
        }

        // Rearrange: move first 4 characters (country + check digits) to the end
        String rearranged = iban.substring(4) + iban.substring(0, 4);

        // Convert letters to numbers: A = 10, B = 11, ..., Z = 35
        StringBuilder numeric = new StringBuilder();
        for (char c : rearranged.toCharArray()) {
            if (Character.isLetter(c)) {
                numeric.append(c - 'A' + 10);
            } else {
                numeric.append(c);
            }
        }
        return numeric.toString();
    }
}