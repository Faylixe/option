package fr.faylixe.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link OptionableFieldFactory} class.
 * 
 * @author fv
 */
public final class OptionableFieldFactoryTest {

	/** Default application usage. **/
	private static final String USAGE = "Foo";

	/** Target testing factory. **/
	private OptionableFieldFactory factory;

	/** Test fixture. **/
	@Before
	public void setUp() {
		factory = new OptionableFieldFactory();
	}

	/** Testing error with duplicate optionable field initial letter. **/
	@Test(expected=IllegalStateException.class)
	public void testDuplicateFieldShortName() {
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

	/** Testing short / long option availability. **/
	@Test
	public void testAvailability() {
		factory.create(new OptionableApplication(USAGE) {
			@Override
			public void run() {
				// Do nothing.
			}
		}.getClass());
		assertFalse(factory.isLongOptionAvailable("verbose"));
		assertFalse(factory.isShortOptionAvailable("v"));
		assertTrue(factory.isLongOptionAvailable("foo"));
		for (int i = 0; i < 26; i++) {
			final char opt = (char)('a' + i);
			if (opt != 'v') {
				assertTrue(factory.isShortOptionAvailable(String.valueOf(opt)));
			}
		}
	}

	/** Working application test. **/
	@Test
	public void testCreate() {
		final List<OptionableField> fields = factory.create(
				new OptionableApplication(USAGE) {
					@Optionable
					private String s;
					@Override
					public void run() {
					}
				}.getClass());
		assertEquals(2, fields.size());
	}

}
