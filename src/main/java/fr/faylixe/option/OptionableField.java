package fr.faylixe.option;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

/**
 * Decoration over {@link Field} class which is used
 * in order to perform automatic attribute filling
 * using command line parameter.
 * 
 * @author fv
 */
public final class OptionableField {

	/** Error message for non optional parameter not provided. **/
	private static final String ARGUMENT_REQUIRED = "Argument %s is required";

	/** Target field that is decorated. **/
	private final Field field;

	/** Field option. **/
	private final Optionable optionable;

	/** Long name for the corresponding option. **/
	private final String longName;

	/** Short name for the corresponding option. **/
	private final String shortName;

	/** Indicates if this option is a flag. **/
	private final boolean flag;

	/**
	 * Default constructor.
	 * 
	 * @param field Field decorated by this class.
	 */
	private OptionableField(final Field field) {
		this.field = field;
		this.optionable = field.getAnnotation(Optionable.class);
		final OptionCache cache = OptionCache.getInstance();
		this.shortName = cache.getShortOption(field, optionable.shortName());
		this.longName = cache.getLongOption(field, optionable.longName());
		final Class<?> type = field.getType();
		final OptionableValueFactories factory = OptionableValueFactories.BOOLEAN;
		this.flag = factory.support(type);
	}

	/**
	 * Validates the receiver object ensuring
	 * it is not <tt>null</tt> and belong to the
	 * target field declaring class.
	 * 
	 * @param receiver Receiver object to validate.
	 * @throws IllegalArgumentException If the given <tt>receiver</tt> is not valid.
	 */
	private void validateReceiver(final Object receiver) {
		if (receiver == null) {
			throw new IllegalArgumentException(""); // TODO : Add error message.
		}
		final Class<?> receiverClass = receiver.getClass();
		final Class<?> targetClass = field.getDeclaringClass();
		// TODO : Check for inheritance.
		if (!targetClass.isAssignableFrom(receiverClass)) {
			throw new IllegalArgumentException(""); // TODO : Add error message.
		}		
	}

	/**
	 * Factory method that builds an Option instance.
	 * 
	 * @return This field as an {@link Option}.
	 */
	public Option toOption() {
		return new Option(shortName, longName, !flag, optionable.description());
	}

	/**
	 * Validates this field over the given <tt>receiver</tt>
	 * using the given parsed <tt>command</tt>.
	 * 
	 * @param command {@link CommandLine} evaluated.
	 * @param receiver Target field receiver class.
	 * @throws IllegalAccessException If setting the field value is not permitted.
	 */
	public void validate(final CommandLine command, final Object receiver) throws IllegalAccessException {
		validateReceiver(receiver);
		if (command.hasOption(longName)) {
			field.setAccessible(true);
			if (flag) {
				field.set(receiver, true);
			}
			else {
				final String value = command.getOptionValue(longName);
				final Object object = OptionableValueFactories.getOptionableValue(field, value);
				if (object != null) {
					field.set(receiver, object); // TODO : Ensure autoboxing is done for primitive here.
				}
			}
		}
		if (optionable.required()) {
			throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED, longName));
		}
	}

	/**
	 * Static factory that creates a list of {@link OptionableField}
	 * for a given <tt>application</tt> class.
	 * 
	 * @param application Target class where field should be optionalized.
	 * @return Created option list.
	 */
	public static List<OptionableField> create(final Class<?> application) {
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
				options.add(new OptionableField(field));
			}
		}
		return options;
	}
	
}
