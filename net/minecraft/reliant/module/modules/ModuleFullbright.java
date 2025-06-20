package net.minecraft.reliant.module.modules;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.Priority;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.event.events.EventUpdate;
import net.minecraft.reliant.module.Module;
import net.minecraft.reliant.module.Option;

public class ModuleFullbright extends Module {

	private float newBrightness = 0f;
	
	public ModuleFullbright() {
		super("Fullbright", "bright", Keyboard.KEY_B, false);
	}

	@Override
	public void onStartup() {
		this.addOption(new Option("brightness", 0.4f, new Float[] {
				0.1f, 0.5f
		}));
	}

	@Marker(eventPriority = Priority.LOW)
	public void onUpdate(EventUpdate eventUpdate) {
		// TODO: Fading brightness
	}
	
	@Override
	public int getColor() {
		return 0xFFEAFF07;
	}

	@Override
	public boolean canRebind() {
		return true;
	}

}
