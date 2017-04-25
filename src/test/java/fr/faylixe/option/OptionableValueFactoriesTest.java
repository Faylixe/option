package fr.faylixe.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;

import org.junit.Test;

/**
 * Test case for {@link OptionableValueFactories} class.
 * 
 * @author fv
 */
public final class OptionableValueFactoriesTest {

	/**
	 * Testing class for getting test fields.
	 * 
	 * @author fv
	 */
	private static class TestClass {
		
		/** Not supported type. **/
		@SuppressWarnings("unused")
		private Object object;

		/** Integer value. **/
		@SuppressWarnings("unused")
		private int i;

	}

	/** Test for support() method. **/
	@Test
	public void testSupport() {
		assertTrue(OptionableValueFactories.BOOLEAN.support(Boolean.class));
		assertTrue(OptionableValueFactories.INTEGER.support(Integer.class));
		assertTrue(OptionableValueFactories.LONG.support(Long.class));
		assertTrue(OptionableValueFactories.FLOAT.support(Float.class));
		assertTrue(OptionableValueFactories.DOUBLE.support(Double.class));
		assertTrue(OptionableValueFactories.STRING.support(String.class));
		assertFalse(OptionableValueFactories.BOOLEAN.support(Integer.class));
		assertFalse(OptionableValueFactories.INTEGER.support(Boolean.class));
		assertFalse(OptionableValueFactories.LONG.support(Integer.class));
		assertFalse(OptionableValueFactories.FLOAT.support(Integer.class));
		assertFalse(OptionableValueFactories.DOUBLE.support(Integer.class));
		assertFalse(OptionableValueFactories.STRING.support(Integer.class));
	}

	/** Test error when not supported type is used. **/
	@Test(expected=IllegalArgumentException.class)
	public void testNotSupported() {
		try {
			final Field field = TestClass.class.getDeclaredField("object");
			OptionableValueFactories.getOptionableValue(field, null);
		}
		catch (final NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/** Test simple convertion. **/
	@Test
	public void testSupported() {
		try {
			final Field field = TestClass.class.getDeclaredField("i");
			final Object object = OptionableValueFactories.getOptionableValue(field, "42");
			assertTrue(object instanceof Integer);
			assertEquals((Integer) 42, (Integer) object);
		}
		catch (final NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
