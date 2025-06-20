package net.minecraft.reliant.module.modules;

import org.lwjgl.input.Keyboard;

import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.module.Module;

public class ModuleNoCheat extends Module {

	public ModuleNoCheat() {
		super("NoCheat", Keyboard.KEY_NONE, true);
	}

	@Override
	public void onStartup() {
	}

	@Override
	public int getColor() {
		return 0;
	}

	@Override
	public boolean canRebind() {
		return false;
	}

}
