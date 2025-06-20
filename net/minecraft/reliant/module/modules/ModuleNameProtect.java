package net.minecraft.reliant.module.modules;

import org.lwjgl.input.Keyboard;

import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.module.Module;

public class ModuleNameProtect extends Module {

	public ModuleNameProtect() {
		super("NameProtect", "name", Keyboard.KEY_NONE, true);
	}

	@Override
	public void onStartup() {
		this.setState(true);
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
