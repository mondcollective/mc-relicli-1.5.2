package net.minecraft.reliant.module.modules;

import org.lwjgl.input.Keyboard;

import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.Priority;
import net.minecraft.reliant.event.events.EventOnEntityDeath;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.module.Module;

public class ModuleRevive extends Module {

	public ModuleRevive() {
		super("Revive", "revive", Keyboard.KEY_NONE, true);
	}

	@Override
	public void onStartup() {
	}
	
	@Marker(eventPriority = Priority.LOW)
	public void onDeath(EventOnEntityDeath eventDeath) {
		// TODO: Make this work
		if (this.getState() && eventDeath.getDeadEntity().equals(this.getPlayer())) {
			this.getPlayer().respawnPlayer();
		}
	}

	@Override
	public int getColor() {
		return 0;
	}

	@Override
	public boolean canRebind() {
		return true;
	}

}
