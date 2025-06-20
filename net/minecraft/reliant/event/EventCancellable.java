package net.minecraft.reliant.event;

public class EventCancellable extends Event {

	private boolean isCancelled;
	
	public boolean isEventCancelled() {
		return this.isCancelled;
	}
	
	public void setCancelled(boolean flag) {
		this.isCancelled = flag;
	}
	
}
