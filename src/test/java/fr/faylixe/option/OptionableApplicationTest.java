package fr.faylixe.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link OptionableContainer} class.
 * 
 * @author fv
 */
public final class OptionableApplicationTest {

	/** Test application usage. **/
	private static final String USAGE = "foo";

	/** Test application parameter value. **/
	private static final String VALUE = "value";

	/** Test arg list injected. **/
	private static final String [] ARGS = new String[]{"--s", VALUE};

	/**
	 * Simple {@link OptionableContainer} implementation for testing.
	 * 
	 * @author fv
	 */
	private static class MyOptionableApplication extends OptionableApplication {

		/** Default constructor. **/
		public MyOptionableApplication() {
			super(USAGE);
		}

		/** String parameter testing. **/
		@Optionable(required=true)
		private String s;

		/** Indicates if run() method has been executed. **/
		private boolean hasRan;

		/** {@inheritDoc} **/
		@Override
		public void run() {
			hasRan = true;
		}

	}


	/** Target testing container. **/
	private MyOptionableApplication application;

	/** Test fixture. **/
	@Before
	public void setUp() {
		application = new MyOptionableApplication();
	}

	/** Test standard application bootstrapping. **/
	@Test
	public void testApplication() {
		assertTrue(application.bootstrap(ARGS));
		assertTrue(application.hasRan);
		assertEquals(VALUE, application.s);
	}

	/** Test error prone application bootstrapping. **/
	@Test
	public void testApplicationError() {
		assertFalse(application.bootstrap(new String[]{}));
		assertFalse(application.hasRan);
	}

}
