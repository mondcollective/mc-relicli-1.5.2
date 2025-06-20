package net.minecraft.reliant.event.events;

import net.minecraft.reliant.event.Event;

public class EventKey extends Event {

	private int key = 0;
	
	public EventKey(int keyEvent) {
		this.key = keyEvent;
	}
	
	public int getKeyEvent() {
		return this.key;
	}
	
}
