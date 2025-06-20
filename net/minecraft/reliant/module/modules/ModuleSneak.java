package net.minecraft.reliant.module.modules;

import org.lwjgl.input.Keyboard;

import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.event.events.EventUpdate;
import net.minecraft.reliant.module.Module;
import net.minecraft.src.Packet19EntityAction;

public class ModuleSneak extends Module {

	public ModuleSneak() {
		super("Sneak", Keyboard.KEY_P, false);
	}

	@Override
	public void onStartup() {
	}

	@Override
	public void onEnable() {
		this.getPlayer().sendQueue.addToSendQueue(new Packet19EntityAction(this.getPlayer(), 1));
	}
	
	@Marker
	public void onUpdate(EventUpdate eventUpdate) {
		if (this.getState()) {
			this.getPlayer().sendQueue.addToSendQueue(new Packet19EntityAction(this.getPlayer(), 1));
		}
	}
	
	@Override
	public void onDisable() {
		this.getPlayer().sendQueue.addToSendQueue(new Packet19EntityAction(this.getPlayer(), 2));
	}
	
	@Override
	public int getColor() {
		return 0xFF009000;
	}

	@Override
	public boolean canRebind() {
		return true;
	}

}
