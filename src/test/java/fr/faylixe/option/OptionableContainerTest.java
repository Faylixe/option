package fr.faylixe.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link OptionableContainer} class.
 * 
 * @author fv
 */
public final class OptionableContainerTest {

	/** String value to be tested. **/
	protected static final String STRING = "Foo";

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
		"--s", STRING,
	};

	/**
	 * Simple {@link OptionableContainer} implementation for testing.
	 * 
	 * @author fv
	 */
	protected static class MyOptionableContainer extends OptionableContainer {

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
		@Optionable(required=true)
		protected String s;

	}

	/** Target testing container. **/
	private MyOptionableContainer container;

	/** Test fixture. **/
	@Before
	public void setUp() {
		container = new MyOptionableContainer();
	}

	/** Test bootstrapping container. **/
	@Test
	public void testValidBootstrap() {
		assertTrue(container.bootstrap(ARGS));
		assertEquals(INTEGER, container.i);
		assertEquals(FLOAT, container.f, 0.0);
		assertEquals(LONG, container.l);
		assertEquals(DOUBLE, container.d, 0.0);
		assertEquals(STRING, container.s);
	}

	/** Test bootstrapping with missing parameter. **/
	public void testNotValidBootstrap() {
		assertFalse(container.bootstrap(new String[]{}));
	}

}
