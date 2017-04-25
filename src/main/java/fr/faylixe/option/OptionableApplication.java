package fr.faylixe.option;

/**
 * Abstract application class based on {@link OptionableContainer}.
 * 
 * @author fv
 */
public abstract class OptionableApplication extends OptionableContainer implements Runnable {

	/** Boolean flag that indicates if program should log verbosely. **/
	@Optionable(description="Indicates if the program output should be verbose.")
	private boolean verbose;

	/**
	 * Default constructor.
	 * 
	 * @param usage Application usage.
	 */
	protected OptionableApplication(final String usage) {
		setUsage(usage);
	}

	/**
	 * Indicates if verbose mode is activated.
	 * 
	 * @return <tt>true</tt> if verbose option has been provided, <tt>false</tt> otherwise.
	 */
	protected final boolean isVerbose() {
		return verbose;
	}

	/** {@inheritDoc} **/
	@Override
	public boolean bootstrap(final String[] args) {
		final boolean isBoostrapped = super.bootstrap(args);
		if (isBoostrapped) {
			run();
		}
		return isBoostrapped;
	}

}
