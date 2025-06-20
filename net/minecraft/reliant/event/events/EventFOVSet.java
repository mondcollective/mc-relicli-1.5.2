package net.minecraft.reliant.event.events;

import net.minecraft.reliant.event.EventCancellable;

public class EventFOVSet extends EventCancellable {

	private float multiplier;
	
	public EventFOVSet(float multiplier) {
		this.multiplier = multiplier;
	}
	
	public float getFOVMultiplier() {
		return this.multiplier;
	}
	
	public void setFOVMultiplier(float newMultiplier) {
		this.multiplier = newMultiplier;
	}
	
	public void cancelFOV(float multiplier) {
		this.setCancelled(true);
		this.setFOVMultiplier(multiplier);
	}
	
}
