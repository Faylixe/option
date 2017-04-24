package fr.faylixe.option;

import org.junit.Test;

/**
 * Test case for {@link OptionableFieldFactory} class.
 * 
 * @author fv
 */
public final class OptionableFieldFactoryTest {

	private static final String USAGE = "Foo";

	/** Testing error with duplicate optionable field initial letter. **/
	@Test(expected=IllegalStateException.class)
	public void testDuplicateFieldShortName() {
		final OptionableFieldFactory factory = new OptionableFieldFactory();
		factory.create(new OptionableApplication(USAGE) {
			/** Conflict with verbose : Field name error. **/
			@Optionable
			private String veto;
			@Override
			public void run() {
				// Do nothing.
			}
		}.getClass());
	}

	/** Testing error with duplicate optionable field name. **/
	@Test(expected=IllegalStateException.class)
	public void testDuplicateFieldLongName() {
		final OptionableFieldFactory factory = new OptionableFieldFactory();
		factory.create(new OptionableApplication(USAGE) {
			/** Conflict with verbose : Field name error. **/
			@Optionable
			private String verbose;
			@Override
			public void run() {
				// Do nothing.
			}
		}.getClass());
	}

	/** Testing error with duplicate optionable short name. **/
	@Test(expected=IllegalStateException.class)
	public void testDuplicateShortName() {
		final OptionableFieldFactory factory = new OptionableFieldFactory();
		factory.create(new OptionableApplication(USAGE) {
			/** Conflict with verbose : Short name error. **/
			@Optionable(shortName="v")
			private String foo;
			@Override
			public void run() {
				// Do nothing.
			}
		}.getClass());
	}

	/** Testing error with duplicate optionable long name. **/
	@Test(expected=IllegalStateException.class)
	public void testDuplicateLongName() {
		final OptionableFieldFactory factory = new OptionableFieldFactory();
		factory.create(new OptionableApplication(USAGE) {
			/** Conflict with verbose : Long name error. **/
			@Optionable(longName="verbose")
			private String foo;
			@Override
			public void run() {
				// Do nothing.
			}
		}.getClass());
	}

}
