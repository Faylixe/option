package fr.faylixe.option;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * An {@link OptionCache} handles VM overall
 * option name.
 * 
 * @author fv
 */
public final class OptionCache {

	/** Error message for field name not available exception. **/
	private static final String FIELD_NOT_AVAILABLE = "Field name %s is not available as option name.";

	/** Error message for original name not available exception. **/
	private static final String ORIGINAL_NOT_AVAILABLE = "Original option name %s is not available.";

	/** Error message for too high name length with short option. **/
	private static final String LENGTH_TOO_HIGH = "Length too high for short option";

	/** Set of long option registered. **/
	private final Set<String> longs;

	/** Integer that acts as a binary vector for short name selection. **/
	private int shorts;

	/**
	 * Default constructor.
	 */
	public OptionCache() {
		this.longs = new HashSet<>();
	}

	/**
	 * Indicates if the given <tt>name</tt> is valid as a long option.
	 * 
	 * @param name Name of the long option to check.
	 * @return <tt>true</tt> if the given <tt>name</tt> is available as long option, <tt>false</tt> otherwise.
	 */
	public boolean isLongOptionAvailable(final String name) {
		return !longs.contains(name);
	}

	/**
	 * Retrieves the index of the short option
	 * from the given <tt>name</tt>.
	 * 
	 * @param name Name of the option to get index for.
	 * @return Index of the given <tt>name</tt>.
	 * @throws IllegalArgumentException If the given name is not valid.
	 */
	private int getIndex(final String name) {
		if (name.isEmpty()) {
			throw new IllegalArgumentException(LENGTH_TOO_HIGH);
		}
		final int option = name.toLowerCase().charAt(0);
		return option - (int) 'a';
	}

	/**
	 * Indicates if the given <tt>name</tt> is valid as a short option.
	 * 
	 * @param name Name of the short option to check.
	 * @return <tt>true</tt> if the given <tt>name</tt> is available as short option, <tt>false</tt> otherwise.
	 */
	public boolean isShortOptionAvailable(final String name) {
		return (shorts & (1 << getIndex(name))) == 0;
	}

	/**
	 * Adds the given short option to the internal
	 * short option vector.
	 * 
	 * @param name Name of the short option to add.
	 */
	private void addShortOption(final String name) {
		shorts ^= (1 << getIndex(name));
	}

	/**
	 * Factory method that transforms the given <tt>field</tt>
	 * into a given {@link OptionableField} assuming the given
	 * <tt>field</tt> is annotated as {@link Optionable}.
	 * 
	 * @param field Field to transform.
	 * @return Created {@link OptionableField} instance.
	 */
	protected OptionableField toOptionableField(final Field field) {
		final Optionable optionable = field.getAnnotation(Optionable.class);
		final String shortName = getShortOption(field, optionable.shortName());
		final String longName = getLongOption(field, optionable.longName());
		return new OptionableField(
				field,
				shortName,
				longName,
				optionable.description(),
				optionable.required());
	}

	/**
	 * Selects the option name for the given field regarding
	 * of the provided user given name. The name will be
	 * added to the internal cache.
	 * 
	 * @param field Optionable field to get long name for.
	 * @param original User given name through field annotation.
	 * @param predicate Predicates that check is a given name is available.
	 * @param consumer Consumer that handles selected name.
	 * @return Field name or <tt>original</tt> name.
	 * @throws IllegalStateException If no name can be used.
	 */
	private static String getOption(
			final Field field,
			final String original,
			final Predicate<String> predicate,
			final Consumer<String> consumer) {
		if (!original.isEmpty()) {
			if (predicate.test(original)) {
				consumer.accept(original);
				return original;
			}
			throw new IllegalStateException(String.format(ORIGINAL_NOT_AVAILABLE, original));
		}
		final String name = field.getName();
		if (!predicate.test(name)) {
			throw new IllegalStateException(String.format(FIELD_NOT_AVAILABLE, name));
		}
		consumer.accept(name);
		return name;
	}

	/**
	 * Selects the long option name for the given
	 * field regarding of the provided user given
	 * name. The name will be added to the internal cache.
	 * 
	 * @param field Optionable field to get long name for.
	 * @param original User given name through field annotation.
	 * @return Field name or <tt>original</tt> name.
	 * @throws IllegalStateException If no name can be used.
	 */
	public String getLongOption(final Field field, final String original) {
		return getOption(field, original, this::isLongOptionAvailable, longs::add);
	}

	/**
	 * Selects the short option name for the given
	 * field regarding of the provided user given
	 * name. The name will be added to the internal cache.
	 * 
	 * @param field Optionable field to get short name for.
	 * @param original User given name through field annotation.
	 * @return Field name or <tt>original</tt> name.
	 * @throws IllegalStateException If no name can be used.
	 */
	public String getShortOption(final Field field, final String original) {
		return getOption(field, original, this::isShortOptionAvailable, this::addShortOption).substring(0, 1);
	}

}
