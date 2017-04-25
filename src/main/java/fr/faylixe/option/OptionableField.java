package fr.faylixe.option;

import java.lang.reflect.Field;

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

	/** Target field that is decorated. **/
	private final Field field;

	/** Long name for the corresponding option. **/
	private final String longName;

	/** Short name for the corresponding option. **/
	private final String shortName;

	/** Field description. **/
	private final String description;

	/** Indicates if this field is required or not. **/
	private final boolean required;	

	/** Indicates if this option is a flag. **/
	private final boolean flag;

	/**
	 * Default constructor.
	 * 
	 * @param field Field decorated by this class.
	 * @param shortName Short name for the corresponding option.
	 * @param longName Long name for the corresponding option. 
	 * @param description Field description.
	 * @param required Indicates if this field is required or not.
	 */
	protected OptionableField(
			final Field field,
			final String shortName,
			final String longName,
			final String description,
			final boolean required) {
		this.field = field;
		this.shortName = shortName;
		this.longName = longName;
		this.description = description;
		this.required = required;
		final Class<?> type = field.getType();
		this.flag = OptionableValueFactories.BOOLEAN.support(type);
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
			throw new IllegalArgumentException("Cannot configure null receiver");
		}
		final Class<?> receiverClass = receiver.getClass();
		final Class<?> targetClass = field.getDeclaringClass();
		if (!targetClass.isAssignableFrom(receiverClass)) {
			throw new IllegalArgumentException("Target receiver does not have target field " + field.getName());
		}		
	}

	/**
	 * Factory method that builds an Option instance.
	 * 
	 * @return This field as an {@link Option}.
	 */
	public Option toOption() {
		return Option
			.builder(shortName)
			.longOpt(longName)
			.desc(description)
			.required(required)
			.type(field.getType())
			.hasArg(!flag)
			.build();
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
					field.set(receiver, object);
				}
			}
		}
	}
	
}
