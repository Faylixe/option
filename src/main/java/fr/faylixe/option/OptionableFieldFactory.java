package fr.faylixe.option;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * An {@link OptionableFieldFactory} handles {@link OptionableField}
 * creation and prevents from naming conflict.
 * 
 * @author fv
 */
public final class OptionableFieldFactory {

	/** Error message for field name not available exception. **/
	private static final String FIELD_NOT_AVAILABLE = "Field name %s is not available as option name.";

	/** Error message for original name not available exception. **/
	private static final String ORIGINAL_NOT_AVAILABLE = "Original option name %s is not available.";

	/** Set of long option registered. **/
	private final Set<String> longs;

	/** Set of short option registered. **/
	private final Set<String> shorts;

	/**
	 * Default constructor.
	 */
	public OptionableFieldFactory() {
		this.longs = new HashSet<>();
		this.shorts = new HashSet<>();
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
	 * Indicates if the given <tt>name</tt> is valid as a short option.
	 * 
	 * @param name Name of the short option to check.
	 * @return <tt>true</tt> if the given <tt>name</tt> is available as short option, <tt>false</tt> otherwise.
	 */
	public boolean isShortOptionAvailable(final String name) {
		return !shorts.contains(name);
	}

	/**
	 * Factory method that transforms the given <tt>field</tt>
	 * into a given {@link OptionableField} assuming the given
	 * <tt>field</tt> is annotated as {@link Optionable}.
	 * 
	 * @param field Field to transform.
	 * @return Created {@link OptionableField} instance.
	 */
	private OptionableField toOptionableField(final Field field) {
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
	 * Selects the long option name for the given
	 * field regarding of the provided user given
	 * name. The name will be added to the internal cache.
	 * 
	 * @param field Optionable field to get long name for.
	 * @param original User given name through field annotation.
	 * @return Field name or <tt>original</tt> name.
	 * @throws IllegalStateException If no name can be used.
	 */
	private String getLongOption(final Field field, final String original) {
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
	private String getShortOption(final Field field, final String original) {
		return getOption(field, original, this::isShortOptionAvailable, shorts::add).substring(0, 1);
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
	 * Static factory that creates a list of {@link OptionableField}
	 * for a given <tt>application</tt> class.
	 * 
	 * @param application Target class where field should be optionalized.
	 * @return Created option list.
	 */
	public List<OptionableField> create(final Class<?> application) {
		if (application == null) {
			return Collections.emptyList();
		}
		final List<OptionableField> parents = create(application.getSuperclass());
		final Field [] fields = application.getDeclaredFields();
		final int size = fields.length + parents.size();
		final List<OptionableField> options = new ArrayList<>(size);
		options.addAll(parents);
		for (final Field field : fields) {
			if (field.isAnnotationPresent(Optionable.class)) {
				options.add(toOptionableField(field));
			}
		}
		return options;
	}

}
