package eu.oberon.oss.tools.checksums;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Serves as an abstract base class for defining the structure and core logic of checksum calculators.
 * <p>
 * This class facilitates the implementation of various checksum algorithms by handling common patterns, such as preprocessing, conversion, validation, and
 * checksum computation.
 *
 * @param <S> the source input type, representing the format of data initially provided to the calculator
 * @param <T> the converted target type, representing the format of data after preprocessing and conversion
 * @param <V> the checksum result type, representing the output produced by the checksum computation
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public abstract class AbstractChecksumCalculator<S, T, V> implements ChecksumCalculator<S, T, V> {
    private static class CalculatorHolder {
        private static final Map<String, ChecksumCalculator<?, ?, ?>> CALCULATORS = new ConcurrentHashMap<>();

        static {
            loadCalculators();
        }

        private static void loadCalculators() {
            //noinspection rawtypes
            ServiceLoader<ChecksumCalculator> loader = ServiceLoader.load(
                    ChecksumCalculator.class,
                    AbstractChecksumCalculator.class.getClassLoader()
            );
            for (ChecksumCalculator<?, ?, ?> calculator : loader) {
                CALCULATORS.put(calculator.getCalculatorName().toLowerCase(), calculator);
            }
        }
    }

    static {
        CalculatorHolder.CALCULATORS.clear();
        CalculatorHolder.loadCalculators();
    }

    /**
     * Reloads all {@link ChecksumCalculator} service providers discovered via {@link ServiceLoader}.
     *
     * @since 1.0.0
     */
    public static void reload() {
        @SuppressWarnings("rawtypes")
        ServiceLoader<ChecksumCalculator> loader = ServiceLoader.load(ChecksumCalculator.class);
        CalculatorHolder.CALCULATORS.clear();
        for (ChecksumCalculator<?, ?, ?> calculator : loader) {
            CalculatorHolder.CALCULATORS.put(calculator.getCalculatorName().toLowerCase(), calculator);
        }
    }

    private final String calculatorName;
    private final UnaryOperator<S> preProcessor;
    private final Function<S, T> converter;
    private final Predicate<T> inputValidator;
    private final Function<T, V> checksumCalculator;

    /**
     * Constructs an instance of {@code AbstractChecksumCalculator} with custom components for preprocessing, conversion, validation, and checksum calculation.
     *
     * @param calculatorName     the name of the checksum calculator.
     * @param preProcessor       a {@link UnaryOperator} to preprocess the input data before conversion.
     * @param converter          a {@link Function} to convert the source type {@code <S>} to the target type {@code <T>}.
     * @param validator          a {@link Predicate} to validate the converted input of type {@code <T>}.
     * @param checksumCalculator a {@link Function} to compute the checksum from the validated input of type {@code <T>}.
     *
     * @since 1.0.0
     */
    protected AbstractChecksumCalculator(String calculatorName,
                                         UnaryOperator<S> preProcessor,
                                         Function<S, T> converter,
                                         Predicate<T> validator,
                                         Function<T, V> checksumCalculator) {
        this.calculatorName = calculatorName;
        this.preProcessor = preProcessor;
        this.converter = converter;
        this.inputValidator = validator;
        this.checksumCalculator = checksumCalculator;
    }

    /**
     * Retrieves a {@link ChecksumCalculator} instance by its calculator name.
     *
     * @param <S>            the source input type
     * @param <T>            the converted target type
     * @param <V>            the checksum result type
     * @param calculatorName the name of the calculator to retrieve
     *
     * @return the {@link ChecksumCalculator} matching the name, or {@code null} if not found
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public static <S, T, V> ChecksumCalculator<S, T, V> getCalculator(String calculatorName) {
        if (calculatorName == null) {
            return null;
        }
        return (ChecksumCalculator<S, T, V>) CalculatorHolder.CALCULATORS.get(calculatorName.toLowerCase());
    }

    /**
     * Retrieves a {@link ChecksumCalculator} instance by its calculator name.
     *
     * @param <S>            the source input type
     * @param <T>            the converted target type
     * @param <V>            the checksum result type
     * @param calculatorName the name of the calculator to retrieve
     *
     * @return the {@link ChecksumCalculator} matching the name, or {@code null} if not found
     *
     * @since 1.0.0
     */
    public static <S, T, V> ChecksumCalculator<S, T, V> getChecksumCalculator(String calculatorName) {
        return getCalculator(calculatorName);
    }

    /**
     * Retrieves a {@link ChecksumCalculator} instance by its calculator name.
     *
     * @param <S>            the source input type
     * @param <T>            the converted target type
     * @param <V>            the checksum result type
     * @param calculatorName the name of the calculator to retrieve
     *
     * @return the {@link ChecksumCalculator} matching the name, or {@code null} if not found
     *
     * @since 1.0.0
     */
    public static <S, T, V> ChecksumCalculator<S, T, V> getInstance(String calculatorName) {
        return getCalculator(calculatorName);
    }

    @Override
    public String getCalculatorName() {
        return calculatorName;
    }

    @Override
    public UnaryOperator<S> preProcessor() {
        return preProcessor;
    }

    @Override
    public Function<S, T> converter() {
        return converter;
    }

    @Override
    public Predicate<T> inputDataValidator() {
        return inputValidator;
    }

    @Override
    public Function<T, V> checksumCalculator() {
        return checksumCalculator;
    }

}
