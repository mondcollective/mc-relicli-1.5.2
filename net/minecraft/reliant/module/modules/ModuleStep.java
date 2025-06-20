package net.minecraft.reliant.module.modules;

import org.lwjgl.input.Keyboard;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.Priority;
import net.minecraft.reliant.event.events.EventPostUpdate;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.event.events.EventUpdate;
import net.minecraft.reliant.module.Module;
import net.minecraft.reliant.module.Option;

public class ModuleStep extends Module {

	public ModuleStep() {
		super("Step", Keyboard.KEY_NONE, true);
	}

	@Override
	public void onStartup() {
		this.addOption(new Option("step_height", 1.3f, new Float[] {
				0.5f, 10f
		}));
	}

	/**
	 * Updates the Player's <code>stepHeight</code> to match
	 * the Option's value. Also sets up for NCP step.
	 * 
	 * @param eventUpdate
	 * 		An Event parameter
	 */
	@Marker
	public void onUpdate(EventUpdate eventUpdate) {
		this.getPlayer().stepHeight = this.getOption("step_height").getFloat();
		if (Reliant.getInstance().getModuleHelper().getModuleByName("nocheat").getState()
				&& this.getOption("step_height").getFloat() >= 1f) {
			this.getPlayer().stepHeight = 0.5f;
			if (this.getPlayer().isCollidedHorizontally &&
					!this.getPlayer().isOnLadder() &&
					this.getPlayer().onGround &&
					this.getWorld().getBlockId(
							Math.round((float) this.getPlayer().posX), 
							Math.round((float) this.getPlayer().boundingBox.maxY + 0.5f), 
							Math.round((float) this.getPlayer().posZ))
							== 0 /*Air*/) {
				this.getPlayer().boundingBox.minY += 1.25;
				this.getPlayer().posY += 1.25;
			}
		}
	}
	
	/**
	 * Makes the movement "silent" client-side during
	 * NCP step.
	 * 
	 * @param eventPostUpdate
	 * 		An Event parameter
	 */
	@Marker(eventPriority = Priority.LOW)
	public void postUpdate(EventPostUpdate eventPostUpdate) {
		if (Reliant.getInstance().getModuleHelper().getModuleByName("nocheat").getState()
				&& this.getOption("step_height").getFloat() >= 1f) {
			if (this.getPlayer().isCollidedHorizontally &&
					!this.getPlayer().isOnLadder() &&
					this.getPlayer().onGround &&
					this.getWorld().getBlockId(
							Math.round((float) this.getPlayer().posX), 
							Math.round((float) this.getPlayer().boundingBox.maxY + 0.5f), 
							Math.round((float) this.getPlayer().posZ))
							== 0 /*Air*/) {
				this.getPlayer().stepHeight = 1f;
				this.getPlayer().boundingBox.minY -= 1.25;
				this.getPlayer().posY -= 1.25;
			}
		}
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
