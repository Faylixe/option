package fr.faylixe.option;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

/**
 * Abstract {@link IApplication} implementation that handles option
 * parsing through {@link Optionable} annotation over application
 * field.
 * 
 * @author fv
 */
public abstract class OptionableApplication implements Runnable {

	/** Boolean flag that indicates if program should log verbosely. **/
	@Optionable(description="Indicates if the program output should be verbose.")
	private boolean verbose;

	/**
	 * Indicates if verbose mode is activated.
	 * 
	 * @return <tt>true</tt> if verbose option has been provided, <tt>false</tt> otherwise.
	 */
	protected final boolean isVerbose() {
		return verbose;
	}

	/**
	 * This is where the magic goes, annotated field are converted to option
	 * and given command line parameters are evaluated to fill application's
	 * attributes.
	 * 
	 * @param args Command line parameters issued from main method.
	 */
	public final void bootstrap(final String [] args) {
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
		}
		catch (final Throwable e) {
			System.err.println("An error occurs while parsing command line parameter : " + e.getMessage());
			formatter.printHelp(getUsage(), options);
		}
		run();
	}

	/**
	 * Application usage getter.
	 * 
	 * @return Application usage description.
	 */
	protected abstract String getUsage();

}
