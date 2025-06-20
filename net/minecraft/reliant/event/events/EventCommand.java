package net.minecraft.reliant.event.events;

import net.minecraft.reliant.event.Event;

public class EventCommand extends Event {

	public String message;
	private boolean isValid = false;
	
	public EventCommand(String message) {
		this.message = message.replaceFirst(".", "");
	}
	
	/**
	 * @return
	 * 		String array of the "args".
	 */
	public String[] getArgs() {
		return this.message.split(" ");
	}
	
	/**
	 * Sets the validity of the Command.
	 * 
	 * @param flag
	 */
	public void setValid(boolean flag) {
		this.isValid = flag;
	}
	
	/**
	 * @return
	 * 		Is the Command valid
	 */
	public boolean isCommandValid() {
		return this.isValid;
	}
	
}
