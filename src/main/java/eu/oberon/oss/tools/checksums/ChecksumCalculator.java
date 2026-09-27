package eu.oberon.oss.tools.checksums;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

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
