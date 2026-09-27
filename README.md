## Build status:

[![Quality gate status](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_validators-checksums&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=oberon-oss_validators-checksums)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_validators-checksums&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=oberon-oss_validators-checksums)

[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_validators-checksums&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=oberon-oss_validators-checksums)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_validators-checksums&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=oberon-oss_validators-checksums)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_validators-checksums&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=oberon-oss_validators-checksums)

[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_validators-checksums&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=oberon-oss_validators-checksums)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_validators-checksums&metric=bugs)](https://sonarcloud.io/summary/new_code?id=oberon-oss_validators-checksums)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_validators-checksums&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=oberon-oss_validators-checksums)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_validators-checksums&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=oberon-oss_validators-checksums)

[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_validators-checksums&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=oberon-oss_validators-checksums)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=oberon-oss_validators-checksums&metric=coverage)](https://sonarcloud.io/summary/new_code?id=oberon-oss_validators-checksums)

# Checksum Validators (`validators-checksums`)

A lightweight, extensible Java library for computing checksums and validating checksum-based identifiers (such as BSN, IBAN, ISBN-10, and ISBN-13).

---

## Installation

Add the dependency to your `pom.xml`:

```xml

<dependency>
    <groupId>eu.oberon-oss.tools</groupId>
    <artifactId>validators-checksums</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

---

## Core Architecture & Key Classes

The library is designed around modular components that separate preprocessing, data validation, checksum calculation, and validity determination:

* **`Validator<T>`**: Generic interface representing a validation contract with a single method: `boolean validate(T value)`.
* **`ChecksumCalculator<S, T, V>`**: Functional interface defining the 4-stage calculation pipeline:
    * `preProcessor()` (`UnaryOperator<S>`): Cleans and normalizes input before conversion (e.g., stripping spaces or formatting).
    * `converter()` (`Function<S, T>`): Transforms the raw source type `<S>` into target type `<T>`.
    * `inputDataValidator()` (`Predicate<T>`): Validates the converted input format before calculating checksum.
    * `checksumCalculator()` (`Function<T, V>`): Computes the checksum result of type `<V>`.
* **`AbstractChecksumCalculator<S, T, V>`**: Base implementation of `ChecksumCalculator` providing automatic service discovery (`ServiceLoader`) and static
  lookup methods (`getCalculator()`, `getInstance()`, `reload()`).
* **`DefaultValidator`**: Standard implementation of `Validator<String>` combining a `ChecksumCalculator<String, String, Integer>` with a
  `BiPredicate<Integer, String>` to determine checksum correctness.
* **`CommonValidators`**: Predefined enum implementing `Validator<String>` for out-of-the-box validation of common identifier formats.

---

## Quick Start: Using Predefined Validators

The easiest way to validate standard identifiers is using `CommonValidators`:

```java
import eu.oberon.oss.tools.validators.CommonValidators;

// Dutch BSN (Burgerservicenummer)
boolean isBsnValid = CommonValidators.BSN.validate("959975044");

        // IBAN
        boolean isIbanValid = CommonValidators.IBAN.validate("NL91ABNA0417164300");

        // ISBN-10
        boolean isIsbn10Valid = CommonValidators.ISBN10.validate("0-306-40615-2");

        // ISBN-13
        boolean isIsbn13Valid = CommonValidators.ISBN13.validate("978-0-306-40615-7");
```

---

## Available Calculators and Validators

| Identifier  | Calculator Class           | Registered Name | Validation Rule                                                                                                |
|:------------|:---------------------------|:----------------|:---------------------------------------------------------------------------------------------------------------|
| **BSN**     | `BSNChecksumCalculator`    | `"BSN"`         | 9-digit Dutch BSN using the 11-proof (modulus 11) algorithm with weight vector `[9, 8, 7, 6, 5, 4, 3, 2, -1]`. |
| **IBAN**    | `IBANChecksumCalculator`   | `"IBAN"`        | ISO 13616 / MOD-97-10 check with country-specific length verification via `IBANCodeTable`.                     |
| **ISBN-10** | `ISBN10ChecksumCalculator` | `"ISBN10"`      | 10-digit ISBN weighted sum with check digit calculation (supports trailing `X` as 10).                         |
| **ISBN-13** | `ISBN13ChecksumCalculator` | `"ISBN13"`      | 13-digit ISBN starting with `978` or `979`, using alternating 1/3 weighted sum over the first 12 digits.       |

---

## Advanced Usage & Library Extension

### 1. Retrieving Calculators via Service Discovery

Calculators registered as services can be retrieved by name using `AbstractChecksumCalculator`:

```java
import eu.oberon.oss.tools.checksums.AbstractChecksumCalculator;
import eu.oberon.oss.tools.checksums.ChecksumCalculator;

// Lookup is case-insensitive
ChecksumCalculator<String, String, Integer> calculator = AbstractChecksumCalculator.getCalculator("BSN");

        // Direct pipeline execution
        String preprocessed = calculator.preProcessor().apply("959-975-044");
        String converted = calculator.converter().apply(preprocessed);

        void example() {
            if (calculator.inputDataValidator().test(converted)) {
                Integer checksum = calculator.checksumCalculator().apply(converted);
            }
        }
```

### 2. Composing Custom Validators with `DefaultValidator`

You can combine any `ChecksumCalculator` with custom verification logic using `DefaultValidator`:

```java
import eu.oberon.oss.tools.checksums.AbstractChecksumCalculator;
import eu.oberon.oss.tools.checksums.ChecksumCalculator;
import eu.oberon.oss.tools.validators.DefaultValidator;
import eu.oberon.oss.tools.validators.Validator;

ChecksumCalculator<String, String, Integer> calculator =
        AbstractChecksumCalculator.getCalculator("BSN");

// Create custom validator with tailored predicate
Validator<String> customValidator = new DefaultValidator(
        calculator,
        (remainder, convertedValue) -> remainder % 11 == 0
);

boolean isValid = customValidator.validate("959975044");
```

### 3. Implementing a Custom Checksum Calculator

Extend `AbstractChecksumCalculator` to define your own checksum pipeline:

```java
package com.example;

import eu.oberon.oss.tools.checksums.AbstractChecksumCalculator;

import java.util.regex.Pattern;

public class CustomIdChecksumCalculator extends AbstractChecksumCalculator<String, String, Integer> {
    private static final Pattern PATTERN = Pattern.compile("^[0-9]{8}$");

    public CustomIdChecksumCalculator() {
        super(
                "CUSTOM_ID",                                      // Calculator name
                input -> input == null ? "" : input.trim(),       // Preprocessor
                input -> input.replace("-", ""),               // Converter
                input -> PATTERN.matcher(input).matches(),        // Input validator
                input -> {                                        // Checksum calculation
                    int sum = 0;
                    for (int i = 0; i < input.length(); i++) {
                        sum += Character.getNumericValue(input.charAt(i)) * (i + 1);
                    }
                    return sum;
                }
        );
    }
}
```

#### Registering via Service Provider Interface (SPI)

To make your calculator discoverable by `AbstractChecksumCalculator.getCalculator("CUSTOM_ID")`, register it in
`META-INF/services/eu.oberon.oss.tools.checksums.ChecksumCalculator`:

```
com.example.CustomIdChecksumCalculator
```

If calculators are loaded dynamically at runtime, call `AbstractChecksumCalculator.reload()` to refresh the registry.

---

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.
