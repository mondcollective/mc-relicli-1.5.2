package net.minecraft.reliant.event;

/**
 * Enum for marking the priority of a Marker.
 * 
 * @author Nahr
 */
public enum Priority {

	HIGHEST,		// Highest priority. Fired first.
	
	HIGH,			// High priority. Fired second.
	
	MEDIUM,			// Medium and default priority. Fired third.
	
	LOW				// Lowest priority. Fired last.
	
}
