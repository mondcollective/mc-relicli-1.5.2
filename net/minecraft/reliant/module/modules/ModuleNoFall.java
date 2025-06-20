package net.minecraft.reliant.module.modules;

import org.lwjgl.input.Keyboard;

import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.events.EventPostUpdate;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.event.events.EventUpdate;
import net.minecraft.reliant.module.Module;

public class ModuleNoFall extends Module {

	private boolean curOnGround = true;
	
	public ModuleNoFall() {
		super("NoFall", Keyboard.KEY_V, false);
	}

	@Override
	public void onStartup() {
		this.setState(true);
	}

	@Marker
	public void onUpdate(EventUpdate eventUpdate) {
		if (this.getState()) {
			this.curOnGround = this.getPlayer().onGround;
			this.getPlayer().onGround = true;
		}
	}
	
	@Marker
	public void postUpdate(EventPostUpdate eventPostUpdate) {
		if (this.getState()) {
			this.getPlayer().onGround = this.curOnGround;
		}
	}
	
	@Override
	public int getColor() {
		return 0xFF13C422;
	}

	@Override
	public boolean canRebind() {
		return true;
	}

}
