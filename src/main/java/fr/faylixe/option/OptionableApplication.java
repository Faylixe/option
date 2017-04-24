package fr.faylixe.option;

import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

/**
 * Abstract {@link IApplication} implementation that handles option
 * parsing through {@link Optionable} annotation over application
 * field.
 * 
 * @author fv
 */
public abstract class OptionableApplication {

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
	 * 
	 * @param args
	 * @throws Exception
	 */
	public final void bootstrap(final String [] args) throws Exception {
		final List<OptionableField> fields = OptionableField.create(getClass());
		final Options options = new Options();
		fields
			.stream()
			.map(OptionableField::toOption)
			.forEach(options::addOption);
		final HelpFormatter formatter = new HelpFormatter();
		final CommandLineParser parser = new BasicParser();
		try {
			final CommandLine command = parser.parse(options, args);
			for (final OptionableField field : fields) {
				field.validate(command, this);
			}
		}
		catch (final Exception e) {
			System.err.println("An error occurs while parsing command line parameter : " + e.getMessage());
			if (verbose) {
				e.printStackTrace();
			}
			formatter.printHelp(getUsage(), options);
		}
		start();
	}

	/**
	 * Application usage getter.
	 * 
	 * @return Application usage description.
	 */
	protected abstract String getUsage();

	/**
	 * Concrete delegate application method.
	 * This method aim to be called once all
	 * {@link OptionableField} has been validated
	 * and filled with required value.
	 * 
	 * @throws Exception If any error occurs during execution.
	 */
	protected abstract void start() throws Exception;

}
