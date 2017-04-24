package fr.faylixe.option;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that defines a field that can
 * be optionalized, namely, filled through
 * command line option.
 * 
 * @author fv
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Optionable {

	/** @return Short name of this option. **/
	String shortName() default "";
	
	/** @return Long name of this option. **/
	String longName() default "";

	/** @return Description of this option. **/
	String description() default "Description not provided.";

	/** @return <tt>true</tt> if option is optional, <tt>false</tt> otherwise. **/
	boolean required() default false;

}
