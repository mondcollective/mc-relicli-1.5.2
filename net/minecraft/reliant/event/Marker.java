package net.minecraft.reliant.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Interface for marking methods that need to
 * listen on Events.
 * 
 * @author Nahr
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Marker {

	/**
	 * The event's priority. Medium by default.
	 */
	public Priority eventPriority() 
		default Priority.MEDIUM;
			
}
