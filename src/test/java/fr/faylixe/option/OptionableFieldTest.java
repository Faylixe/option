package fr.faylixe.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import fr.faylixe.option.OptionableContainerTest.MyOptionableContainer;

/**
 * Test case for {@link OptionableField} class.
 * 
 * @author fv
 */
public final class OptionableFieldTest {

	/** Target class field to be tested. **/
	private static final String FIELD_NAME = "s";

	/** Short name. **/
	private static final String SHORT = "s";

	/** Long name. **/
	private static final String LONG = "s";

	/** Option description. **/
	private static final String DESC = "foo";

	/** Target tested field. **/
	private OptionableField field;

	/** Test fixture. **/
	@Before
	public void setUp() throws NoSuchFieldException, SecurityException {
		field = new OptionableField(
				MyOptionableContainer.class.getDeclaredField(FIELD_NAME),
				SHORT,
				LONG,
				DESC,
				true);
		
	}

	/** Test option casting. **/
	@Test
	public void testToOption() {
		final Option option = field.toOption();
		assertEquals(DESC, option.getDescription());
		assertEquals(LONG, option.getLongOpt());
		assertEquals(SHORT, option.getOpt());
		assertTrue(option.isRequired());
	}

	/** Test not valid receiver. **/
	@Test(expected=IllegalArgumentException.class)
	public void testNotValidReceiver() throws IllegalAccessException, IllegalArgumentException {
		field.validate(null, this);
	}
	
	/** Test null receiver. **/
	@Test(expected=IllegalArgumentException.class)
	public void testNullReceiver() throws IllegalAccessException, IllegalArgumentException {
		field.validate(null, null);
	}

	/** Test boolean field validation. **/
	@Test
	public void testFlagValidation() {
		
	}

	/** Test non boolean field validation. **/
	@Test
	public void testFieldValidation() {
		final CommandLineParser parser = new DefaultParser();
		final Options options = new Options().addOption(field.toOption());
		final String [] args = new String[]{"--s", OptionableContainerTest.STRING};
		try {
			final CommandLine command = parser.parse(options, args);
			final MyOptionableContainer container = new MyOptionableContainer();
			field.validate(command, container);
			assertEquals(OptionableContainerTest.STRING, container.s);
		}
		catch (final ParseException | IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
