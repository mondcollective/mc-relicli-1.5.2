package net.minecraft.reliant.module.modules;

import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.Priority;
import net.minecraft.reliant.event.events.EventPostUpdate;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.event.events.EventUpdate;
import net.minecraft.reliant.module.Module;
import net.minecraft.reliant.module.Option;
import net.minecraft.reliant.module.PlayerAngle;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Packet18Animation;

public class ModuleRetardMode extends Module {

	private Random random = new Random();
	private float curYaw, curPitch;
	private float horizontal = 0f;
	
	public ModuleRetardMode() {
		super("Retard", Keyboard.KEY_NONE, false);
	}

	@Override
	public void onStartup() {
		this.addOption(new Option("Retard", false));
		this.addOption(new Option("Headless", false));
		this.addOption(new Option("Stare", false));
		this.addOption(new Option("ADD", false));
	}

	@Marker(eventPriority = Priority.HIGHEST)
	public void onUpdate(EventUpdate eventUpdate) {
		boolean retard = this.getOption("retard").getBoolean();
		boolean headless = this.getOption("Headless").getBoolean();
		boolean stare = this.getOption("Stare").getBoolean();
		boolean add = this.getOption("ADD").getBoolean();
		this.setState(retard || headless || stare || add);
		if (this.getState()) {
			this.curPitch = this.getPlayer().rotationPitch;
			this.curYaw = this.getPlayer().rotationYaw;
			if (retard) {
				int newYaw = this.random.nextInt(360);
				int newPitch = this.random.nextInt(360) - 180;
				this.getRotationHelper().addToQueue(new PlayerAngle((float) newYaw, (float) newPitch, Priority.HIGH));
				this.getPlayer().sendQueue.addToSendQueue(new Packet18Animation(this.getPlayer(), 1));
			} else if (headless) {
				this.getRotationHelper().addToQueue(new PlayerAngle(this.getPlayer().rotationYaw, 180, Priority.HIGH));
			} else if (stare) {
				for (Entity entity : (List<Entity>) this.getWorld().loadedEntityList) {
					if (!(entity instanceof EntityPlayer) || entity.equals(this.getPlayer())) {
						continue;
					}
					if (this.getPlayer().getDistanceToEntity(entity) > 20) {
						continue;
					}
					if (entity != null) {
						float angles[] = this.getRotationHelper().getPerfectAngles(entity);
						this.getRotationHelper().addToQueue(new PlayerAngle(angles[0], angles[1], Priority.HIGH));
					}
				}
			} else if (add) {
				this.getRotationHelper().addToQueue(new PlayerAngle(this.horizontal += 10f, -90, Priority.HIGH));
			}
		}
	}
	
	@Marker(eventPriority = Priority.HIGH)
	public void postUpdate(EventPostUpdate eventPostUpdate) {
		if (this.getState()) {
			this.getRotationHelper().setYaw(this.curYaw);
			this.getRotationHelper().setPitch(this.curPitch);
		}
	}
	
	@Override
	public int getColor() {
		return 0xFF6D9900;
	}

	@Override
	public boolean canRebind() {
		return false;
	}

}
