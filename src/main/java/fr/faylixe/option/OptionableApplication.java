package fr.faylixe.option;

/**
 * 
 * @author fv
 */
public abstract class OptionableApplication extends OptionableContainer implements Runnable {

	/** Boolean flag that indicates if program should log verbosely. **/
	@Optionable(description="Indicates if the program output should be verbose.")
	private boolean verbose;

	/**
	 * 
	 * @param usage
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
	public void bootstrap(String[] args) {
		super.bootstrap(args);
		run();
	}

}
