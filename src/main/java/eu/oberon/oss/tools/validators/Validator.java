package eu.oberon.oss.tools.validators;

/**
 * Represents a generic validation interface for checking the validity of objects of a specific type. This interface can be implemented to provide custom
 * validation logic for various data types.
 *
 * @param <T> the type of the objects that this validator will validate.
 *
 * @author TigerLilly64
 * @since 1.0.0
 */
public interface Validator<T> {
    /**
     * Validates the given value against the validation rules implemented by this validator.
     *
     * @param value the value to be validated.
     *
     * @return true if the value is valid according to the validation rules, false otherwise.
     *
     * @since 1.0.0
     */
    boolean validate(T value);
}
