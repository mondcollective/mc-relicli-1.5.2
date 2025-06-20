package net.minecraft.reliant.module.modules;

import org.lwjgl.input.Keyboard;

import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.Priority;
import net.minecraft.reliant.event.events.EventFOVSet;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.event.events.EventUpdate;
import net.minecraft.reliant.module.Module;
import net.minecraft.reliant.module.Option;
import net.minecraft.reliant.Reliant;

public class ModuleSpeed extends Module {

	private float oldLandFactor;
	private float oldAirFactor;
	
	public ModuleSpeed() {
		super("Speed", Keyboard.KEY_R, false);
	}

	@Override
	public void onStartup() {
		this.addOption(new Option("run_speed", 2.5f, new Float[] {
				1f, 10f
		}));
		this.addOption(new Option("run_cap", 1.25f, new Float[] {
				1f, 3f
		}));
	}
	
	@Override
	public void onEnable() {
		this.oldLandFactor = this.getPlayer().landMovementFactor;
		this.oldAirFactor = this.getPlayer().jumpMovementFactor;
	}
	
	@Override
	public void onDisable() {
		this.getPlayer().landMovementFactor = this.oldLandFactor;
		this.getPlayer().jumpMovementFactor = this.oldAirFactor;
	}
	
	@Marker(eventPriority = Priority.HIGHEST)
	public void onUpdate(EventUpdate eventUpdate) {
		if (this.getState() && !Reliant.getInstance().getModuleHelper().getModuleByName("nocheat").getState()) {
			this.getPlayer().landMovementFactor *= this.getOption("run_speed").getFloat();
			this.getPlayer().jumpMovementFactor *= this.getOption("run_speed").getFloat();
		} else if (this.getState() && Reliant.getInstance().getGameSettings().keyBindForward.pressed) {
			this.getPlayer().landMovementFactor *= this.getOption("run_cap").getFloat();
			this.getPlayer().jumpMovementFactor *= this.getOption("run_cap").getFloat();
			this.getPlayer().setSprinting(true);
		}
	}

	@Marker(eventPriority = Priority.LOW)
	public void onFOVSet(EventFOVSet eventFOVSet) {
		if (this.getState() && eventFOVSet.getFOVMultiplier() != 1f) {
			eventFOVSet.cancelFOV(1f);
		}
	}
	
	@Override
	public int getColor() {
		return 0xFF96CC39;
	}

	@Override
	public boolean canRebind() {
		return true;
	}

}
