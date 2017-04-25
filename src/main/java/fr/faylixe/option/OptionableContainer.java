package fr.faylixe.option;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Abstract class that handles option parsing through
 * {@link Optionable} annotation over class attributes
 * fields.
 * 
 * @author fv
 */
public class OptionableContainer {

	/** Container usage description. **/
	private String usage;

	/**
	 * Default constructor.
	 */
	protected OptionableContainer() {
		this.usage = "";
	}

	/**
	 * Usage setter.
	 * 
	 * @param usage Container usage description.
	 */
	public final void setUsage(final String usage) {
		this.usage = usage;
	}

	/**
	 * This is where the magic goes, annotated field are converted to option
	 * and given command line parameters are evaluated to fill application's
	 * attributes.
	 * 
	 * @param args Command line parameters issued from main method.
	 * @return <tt>true</tt> if the bootstrapping went well, <tt>false</tt> otherwise.
	 */
	public boolean bootstrap(final String [] args) {
		final OptionableFieldFactory factory = new OptionableFieldFactory();
		final List<OptionableField> fields = factory.create(getClass());
		final Options options = new Options();
		fields
			.stream()
			.map(OptionableField::toOption)
			.forEach(options::addOption);
		final HelpFormatter formatter = new HelpFormatter();
		final CommandLineParser parser = new DefaultParser();
		try {
			final CommandLine command = parser.parse(options, args);
			for (final OptionableField field : fields) {
				field.validate(command, this);
			}
			return true;
		}
		catch (final ParseException | IllegalAccessException | IllegalArgumentException e) {
			System.err.println("An error occurs while parsing command line parameter : " + e.getMessage());
			formatter.printHelp(usage, options);
		}
		return false;
	}

}
