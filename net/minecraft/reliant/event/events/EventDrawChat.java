package net.minecraft.reliant.event.events;

import net.minecraft.reliant.event.EventCancellable;

public class EventDrawChat extends EventCancellable {

	private int updateCounter;
	
	public EventDrawChat(int counter) {
		this.updateCounter = counter;
	}
	
	public int getUpdateCounter() {
		return this.updateCounter;
	}
	
}
