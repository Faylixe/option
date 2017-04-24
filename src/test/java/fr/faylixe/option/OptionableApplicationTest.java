package fr.faylixe.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link OptionableApplication} class.
 * 
 * @author fv
 */
public final class OptionableApplicationTest {

	/**
	 * Simple {@link OptionableApplication} implementation for testing.
	 * 
	 * @author fv
	 */
	private static class MyOptionableApplication extends OptionableApplication {

		/** Integer parameter testing. **/
		@Optionable
		private int i;

		/** Float parameter testing. **/
		@Optionable
		private float f;

		/** Long parameter testing. **/
		@Optionable
		private long l;
		
		/** Double parameter testing. **/
		@Optionable
		private double d;

		/** String parameter testing. **/
		@Optionable
		private String s;

		/** {@inheritDoc} **/
		@Override
		public void run() {
			// Do nothing.
		}

		/** {@inheritDoc} **/
		@Override
		protected String getUsage() {
			return USAGE;
		}

	}

	/** Test application usage description string. **/
	private static final String USAGE = "Foo";

	/** Integer value to be tested. **/
	private static final int INTEGER = 1;

	/** Float value to be tested. **/
	private static final float FLOAT = 2.0f;

	/** Long value to be tested. **/
	private static final long LONG = 3L;

	/** Double value to be tested. **/
	private static final double DOUBLE = 4;

	/** Command line sample to use for testing. **/
	private static final String [] ARGS = new String[] {
		"--i", String.valueOf(INTEGER),
		"--f", String.valueOf(FLOAT),
		"--l", String.valueOf(LONG),
		"--d", String.valueOf(DOUBLE),
		"--s", USAGE,
	};

	/** Target application to test. **/
	private MyOptionableApplication application;
	
	/** Test fixture that initializes testing application. **/
	@Before
	public void setUp() {
		application = new MyOptionableApplication();
	}

	/** Validates the target testing application state. **/
	private void verifyApplication() {
		assertEquals(USAGE, application.getUsage());
		assertEquals(INTEGER, application.i);
		assertEquals(FLOAT, application.f, 0.0);
		assertEquals(LONG, application.l);
		assertEquals(DOUBLE, application.d, 0.0);
		assertEquals(USAGE, application.s);
	}

	/** Testing boostraping application. **/
	@Test
	public void testBootstrap() {
		application.bootstrap(ARGS);
		verifyApplication();
		assertFalse(application.isVerbose());
	}
	
	/** Testing bootstraping using verbose flag. **/
	@Test
	public void testVerboseBootstrap() {
		final String [] args = new String[ARGS.length + 1];
		System.arraycopy(ARGS, 0, args, 0, ARGS.length);
		args[ARGS.length] = "--verbose";
		application.bootstrap(args);
		verifyApplication();
		assertTrue(application.isVerbose());
	}

}
