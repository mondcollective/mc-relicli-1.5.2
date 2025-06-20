package net.minecraft.reliant.module.modules;

import org.lwjgl.input.Keyboard;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.events.EventFOVSet;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.event.events.EventUpdate;
import net.minecraft.reliant.module.Module;
import net.minecraft.reliant.module.Option;

public class ModuleFlight extends Module {

	public ModuleFlight() {
		super("Flight", Keyboard.KEY_F, false);
	}

	@Override
	public void onStartup() {
		this.addOption(new Option("flight_speed", 3f, new Float[] {
				0.1f, 10f
		}));
		this.addOption(new Option("flight_cap", 1.25f, new Float[] {
				1f, 3f
		}));
	}

	@Marker
	public void onUpdate(EventUpdate eventUpdate) {
		if (this.getState()) {
			this.getPlayer().motionY = 0;
			if (Reliant.getInstance().getGameSettings().keyBindJump.pressed) {
				this.getPlayer().motionY += 
						Reliant.getInstance().getModuleHelper().getModuleByName("nocheat").getState() ?
								0.3 : 0.55;
			} else if (Reliant.getInstance().getGameSettings().keyBindSneak.pressed) {
				this.getPlayer().motionY -= 
						Reliant.getInstance().getModuleHelper().getModuleByName("nocheat").getState() ?
								0.3 : 0.55;
			}
			if (Reliant.getInstance().getModuleHelper().getModuleByName("nocheat").getState()) {
				this.getPlayer().landMovementFactor *= this.getOption("flight_cap").getFloat();
				this.getPlayer().jumpMovementFactor *= this.getOption("flight_cap").getFloat();
			} else {
				this.getPlayer().landMovementFactor *= this.getOption("flight_speed").getFloat();
				this.getPlayer().jumpMovementFactor *= this.getOption("flight_speed").getFloat();
			}
		}
	}
	
	@Marker
	public void onFOVSet(EventFOVSet eventFOVSet) {
		if (this.getState()) {
			//eventFOVSet.cancelFOV(1f);
		}
	}
	
	@Override
	public int getColor() {
		return 0xFF7FB9D8;
	}

	@Override
	public boolean canRebind() {
		return true;
	}

}
