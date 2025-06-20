package net.minecraft.reliant.module;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.event.Listener;
import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.events.EventCommand;

public abstract class Command implements Listener {

	private String nameArray[];
	
	public Command(String[] names) {
		this.nameArray = names;
		Reliant.getInstance().getEventManager().addListener(this);
	}
	
	/**
	 * Called when an EventCommand is fired.
	 * 
	 * @param eventCommand
	 * 		An Event parameter
	 */
	@Marker
	public abstract void onCommand(EventCommand eventCommand);
	
	/**
	 * @return
	 * 		String that should be returned if the Command fails
	 */
	public abstract String returnHelp();
	
	/**
	 * Checks if a String is equal to one of the Command's names.
	 * 
	 * @param check
	 * 		The String to check
	 * @return
	 * 		True if <code>check</code> is one of the Command's names
	 */
	public boolean equalsCommand(String check) {
		for (String name : this.getNames()) {
			if (check.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return
	 * 		String array containing the names for the Command
	 */
	public String[] getNames() {
		return this.nameArray;
	}
	
	/**
	 * @return
	 * 		The first entry in the String array of the names of the Command
	 */
	public String getFirstName() {
		return this.nameArray[0];
	}
	
	/**
	 * Prints a string with "[Reliant]: " at the beginning.
	 * 
	 * @param string
	 * 		The String to print
	 */
	public final void print(String string) {
		Reliant.getInstance().printMessage(string);
	}
	
}
