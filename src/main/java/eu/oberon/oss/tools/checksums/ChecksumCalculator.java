package eu.oberon.oss.tools.checksums;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * The {@code ChecksumCalculator} interface defines a generic contract for performing checksum calculation on input data.
 * <p>
 * The process consists of the following steps:
 * <ul>
 * <li>preprocessing</li>
 * <li>conversion</li>
 * <li>validation</li>
 * <li>checksum computation</li>
 * </ul>
 *
 * @param <S> The source input type before preprocessing and conversion.
 * @param <T> The target type after preprocessing and conversion, on which checksum validation occurs.
 * @param <V> The type of the computed checksum result.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public interface ChecksumCalculator<S, T, V> {

    /**
     * Returns the name of the checksum calculator.
     *
     * @return the name of the calculator as a {@code String}.
     *
     * @since 1.0.0
     */
    String getCalculatorName();

    /**
     * Pre-Processes the input data.
     * <p>
     * Typical use can be adding data to make the input data to conform to the expected format.
     *
     * @return The result of the
     */
    UnaryOperator<S> preProcessor();

    /**
     * Performs conversion form the source type {@code <S>} into the target type {@code <T>}.
     *
     * @return The result of the conversion operation.
     *
     * @since 1.0.0
     */
    Function<S, T> converter();

    /**
     * Validates the input data AFTER the conversion operation (#converter).
     *
     * @return A predicate that checks if the input data is valid.
     *
     * @since 1.0.0
     */
    Predicate<T> inputDataValidator();

    /**
     * Validates the checksum of the input data AFTER the conversion operation (#converter).
     *
     * @return A predicate that checks if the checksum is valid.
     *
     * @since 1.0.0
     */
    Function<T, V> checksumCalculator();
}
