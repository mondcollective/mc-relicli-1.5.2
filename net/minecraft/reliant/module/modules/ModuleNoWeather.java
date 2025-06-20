package net.minecraft.reliant.module.modules;

import org.lwjgl.input.Keyboard;

import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.events.EventRainSnow;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.module.Module;

public class ModuleNoWeather extends Module {

	public ModuleNoWeather() {
		super("NoWeather", "weather", Keyboard.KEY_L, false);
	}

	@Override
	public void onStartup() {
	}

	@Marker
	public void onRain(EventRainSnow eventRainSnow) {
		eventRainSnow.setCancelled(this.getState());
	}
	
	@Override
	public int getColor() {
		return 0xFF63D1F4;
	}

	@Override
	public boolean canRebind() {
		return true;
	}

}
