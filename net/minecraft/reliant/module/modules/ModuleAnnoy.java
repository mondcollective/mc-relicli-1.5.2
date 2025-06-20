package net.minecraft.reliant.module.modules;

import org.lwjgl.input.Keyboard;

import java.util.List;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.events.EventCommand;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.event.events.EventUpdate;
import net.minecraft.reliant.module.Module;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;

public class ModuleAnnoy extends Module {

	private String targetName = "<None>";
	
	public ModuleAnnoy() {
		super("Annoy", Keyboard.KEY_H, false);
	}

	@Override
	public void onStartup() {
	}
	

	@Override
	public void onDisable() {
		Reliant.getInstance().getGameSettings().keyBindForward.pressed = false;
	}
	
	@Marker
	public void onUpdate(EventUpdate eventUpdate) {
		if (this.getState()) {
			EntityPlayer target = null;
			for (Entity entity : (List<Entity>) this.getWorld().loadedEntityList) {
				if (entity instanceof EntityPlayer) {
					EntityPlayer entityPlayer = (EntityPlayer) entity;
					if (entityPlayer.username.equalsIgnoreCase(this.targetName)) {
						target = entityPlayer;
						break;
					}
				}
			}
			if (target != null && this.getPlayer().getDistanceToEntity(target) <= 50) {
				float angles[] = this.getRotationHelper().getPerfectAngles(target);
				this.getRotationHelper().setYaw(angles[0]);
				this.getRotationHelper().setPitch(angles[1]);
				Reliant.getInstance().getGameSettings().keyBindForward.pressed = true;
			}
		}
	}
	
	@Marker
	public void onCommand(EventCommand eventCommand) {
		String args[];
		try {
			args = eventCommand.getArgs();
			if (args[0].equalsIgnoreCase("annoy") || args[0].equalsIgnoreCase("a")) {
				this.toggleState();
				Reliant.getInstance().printMessage("Annoy toggled \2472" + (this.getState() ? "on" : "off") + "\247f.");
				eventCommand.setValid(true);
			} else if (args[0].equalsIgnoreCase("at") || args[0].equalsIgnoreCase("annoytarget")) {
				try {
					String newTarget = args[1];
					this.targetName = newTarget;
					Reliant.getInstance().printMessage("Annoy target: \247a" + this.targetName + "\247f.");
					Reliant.getInstance().printMessage("Annoy target updated \2472successfully\247f.");
				} catch (Exception exception) {
					exception.printStackTrace();
					Reliant.getInstance().printMessage("Annoy target: \247a" + this.targetName + "\247f.");
				}
				eventCommand.setValid(true);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	@Override
	public int getColor() {
		return 0xFFC456FF;
	}

	@Override
	public boolean canRebind() {
		return true;
	}

}
