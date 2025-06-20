package net.minecraft.reliant.module.modules;

import org.lwjgl.input.Keyboard;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.events.EventFOVSet;
import net.minecraft.reliant.event.events.EventMovementUpdate;
import net.minecraft.reliant.event.events.EventUpdate;
import net.minecraft.reliant.module.Module;
import net.minecraft.src.EntityClientPlayerMP;

public class ModuleFreecam extends Module {

	public EntityClientPlayerMP freecamEntity;
	private double curX, curY, curZ;
	private float curYaw, curPitch;
	
	public ModuleFreecam() {
		super("Freecam", Keyboard.KEY_C, true);
	}

	@Override
	public void onStartup() {
	}
	
	@Override
	public void onEnable() {
		this.curX = this.getPlayer().posX;
		this.curY = this.getPlayer().posY;
		this.curZ = this.getPlayer().posZ;
		this.curYaw = this.getPlayer().rotationYaw;
		this.curPitch = this.getPlayer().rotationPitch;
		this.freecamEntity = new EntityClientPlayerMP(Reliant.getInstance().getMinecraft(), Reliant.getInstance().getWorld(),
				Reliant.getInstance().getMinecraft().session, Reliant.getInstance().getPlayer().sendQueue) {
			protected boolean pushOutOfBlocks(double par1, double par3, double par5) {
				return false;
			}
			
			public boolean isEntityInsideOpaqueBlock() {
				return false;
			}
			
			public boolean isInRangeToRenderDist(double par1) {
		        return false;
		    }
		};
		this.freecamEntity.copyDataFrom(this.getPlayer(), true);
		this.freecamEntity.setPositionAndRotation(this.curX, this.curY, this.curZ, 
				this.curYaw, this.curPitch);
		this.freecamEntity.movementInput = this.getPlayer().movementInput;
		this.freecamEntity.noClip = true;
		this.getWorld().addEntityToWorld(-666, this.freecamEntity);
		Reliant.getInstance().getMinecraft().renderViewEntity = this.freecamEntity;
	}
	
	@Override
	public void onDisable() {
		Reliant.getInstance().getMinecraft().renderViewEntity = this.getPlayer();
		this.getWorld().removeEntityFromWorld(-666);
		this.freecamEntity = null;
	}
	
	/**
	 * Handles the flying for the Freecam and sets
	 * the Player to be still.
	 * 
	 * @param eventUpdate
	 * 		An Event parameter
	 */
	@Marker
	public void onUpdate(EventUpdate eventUpdate) {
		if (this.getState()) {
			this.getPlayer().setPositionAndRotation(this.curX, this.curY, this.curZ, 
				this.curYaw, this.curPitch);
			this.freecamEntity.motionY = 0;
			if (Reliant.getInstance().getGameSettings().keyBindJump.pressed) {
				this.freecamEntity.motionY += 1;
			} else if (Reliant.getInstance().getGameSettings().keyBindSneak.pressed) {
				this.freecamEntity.motionY -= 1;
			}
			this.freecamEntity.landMovementFactor *= 3.5;
			this.freecamEntity.jumpMovementFactor *= 3.5;
		}
	}
	
	/**
	 * Cancels the movement updates and sends <code>Packet0KeepAlive</code>
	 * if Freecam is active.
	 * 
	 * @param eventMovementUpdate
	 * 		An Event parameter
	 */
	@Marker
	public void onMovementUpdates(EventMovementUpdate eventMovementUpdate) {
		if (this.getState()) {
			eventMovementUpdate.setCancelled(true);
		}
	}
	
	/**
	 * Resets the FOV multiplier to 1 if Freecam is active.
	 * 
	 * @param eventFOVSet
	 * 		An Event parameter
	 */
	@Marker
	public void onSetFOV(EventFOVSet eventFOVSet) {
		if (this.getState()) {
			eventFOVSet.cancelFOV(1f);
		}
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
