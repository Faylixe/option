package fr.faylixe.option;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Enumeration of factory for checking
 * and handling optionable value.
 * 
 * @author fv
 */
public enum OptionableValueFactories {

	/** Factory for boolean flag, only use for flag testing. **/
	BOOLEAN(is(Boolean.TYPE, Boolean.class), v -> true),

	/** Factory for integer type. **/
	INTEGER(is(Integer.TYPE, Integer.class), Integer::valueOf),
	
	/** Factory for long type. **/
	LONG(is(Long.TYPE, Long.class), Long::valueOf),
	
	/** Factory for float type. **/
	FLOAT(is(Float.TYPE, Float.class), Float::valueOf),
	
	/** Factory for double type. **/
	DOUBLE(is(Double.TYPE, Double.class), Double::valueOf),
	
	/** Factory for String type. **/
	STRING(String.class::equals, v -> v),

	;

	/** Error message for type mismatch. **/
	private static final String NOT_VALID = "No valid type cast found for option %s";

	/** Predicates that test if a given class is supported by this factory. **/
	private final Predicate<Class<?>> predicate;

	/** Function that converts a String value into a valid object type. **/
	private final Function<String, Object> converter;

	/**
	 * Default constructor.
	 * 
	 * @param predicate Predicates that test if a given class is supported by this factory.
	 * @param converter Function that converts a String value into a valid object type.
	 */
	private OptionableValueFactories(
			final Predicate<Class<?>> predicate,
			final Function<String, Object> converter) {
		this.predicate = predicate;
		this.converter = converter;
	}

	/**
	 * Check if the given <tt>receiver</tt> class is supported by this factory.
	 * 
	 * @param receiver Receiver class that should be tested.
	 * @return <tt>true</tt> if this factory support the given class.
	 */
	public boolean support(final Class<?> receiver) {
		return predicate.test(receiver);
	}

	/**
	 * Converts the given <tt>value</tt> into the target object type
	 * of this factory.
	 * 
	 * @param value Value to convert.
	 * @return Converted value in {@link Object} form.
	 */
	private Object convert(final String value) {
		return converter.apply(value);
	}

	/**
	 * Retrieves the valid type if any for the given <tt>field</tt>
	 * and returns the given <tt>value</tt> in the expected {@link Object} form.
	 * 
	 * @param field Field that will receive the value.
	 * @param value Value retrieved from command line.
	 * @return Created object.
	 */
	public static Object getOptionableValue(final Field field, final String value) {
		for (final OptionableValueFactories factory : OptionableValueFactories.values()) {
			final Class<?> type = field.getType();
			if (factory.support(type)) {
				return factory.convert(value);
			}
		}
		throw new IllegalArgumentException(String.format(NOT_VALID, field.getName()));
	}

	/**
	 * Simple predicate factory method that check for the
	 * given set of classes.
	 * 
	 * @param supportedClasses List of classes that are supported by the built predicate.
	 * @return Created predicate.
	 */
	private static Predicate<Class<?>> is(final Class<?> ... supportedClasses) {
		return c -> {
			for (final Class<?> supportedClass : supportedClasses) {
				if (supportedClass.equals(c)) {
					return true;
				}
			}
			return false;
		};
	}

}
