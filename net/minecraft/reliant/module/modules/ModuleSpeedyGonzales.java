package net.minecraft.reliant.module.modules;

import org.lwjgl.input.Keyboard;

import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.module.Module;
import net.minecraft.reliant.module.Option;

public class ModuleSpeedyGonzales extends Module {

	public ModuleSpeedyGonzales() {
		super("Speedy Gonzales", "gonzales", Keyboard.KEY_O, false);
	}

	@Override
	public void onStartup() {
		this.setState(true);
		this.addOption(new Option("fast_place", 3, new Integer[] {
				1, 5
		}));
	}

	@Override
	public int getColor() {
		return 0xFFE0A341;
	}

	@Override
	public boolean canRebind() {
		return true;
	}

}
