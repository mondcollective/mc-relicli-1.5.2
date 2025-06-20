package net.minecraft.reliant.module.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.events.EventCommand;
import net.minecraft.reliant.event.events.EventRender;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.event.events.EventUpdate;
import net.minecraft.reliant.module.Module;
import net.minecraft.src.RenderManager;

public class ModuleBreadcrumb extends Module {

	private ArrayList<double[]> positions = new ArrayList<double[]>();
	private double lastX, lastY, lastZ;
	
	public ModuleBreadcrumb() {
		super("Breadcrumb", "crumb", Keyboard.KEY_NONE, true);
	}

	@Override
	public void onStartup() {
	}

	/**
	 * Adds positions to the list for rendering.
	 * 
	 * @param eventUpdate
	 * 		An Event parameter
	 */
	@Marker
	public void onUpdate(EventUpdate eventUpdate) {
		if (this.getState()) {
			double xDiff = Math.pow(this.lastX - this.getPlayer().posX, 2);
			double yDiff = Math.pow(this.lastY - this.getPlayer().boundingBox.minY, 2);
			double zDiff = Math.pow(this.lastZ - this.getPlayer().posZ, 2);
			double distance = Math.sqrt(xDiff + yDiff + zDiff);
			if (distance >= 0.45) {
				this.positions.add(new double[]{
						this.getPlayer().posX,
						this.getPlayer().boundingBox.minY, 
						this.getPlayer().posZ
				});
				this.lastX = this.getPlayer().posX;
				this.lastY = this.getPlayer().boundingBox.minY;
				this.lastZ = this.getPlayer().posZ;
			}
		}
	}
	
	/**
	 * Iterates through the list of positions and
	 * renders the trail.
	 * 
	 * @param eventRender
	 * 		An Event parameter
	 */
	@Marker
	public void onRender(EventRender eventRender) {
		if (this.getState() && this.positions.size() > 0) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(2929);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
			GL11.glDepthMask(false);
			GL11.glLineWidth(1.5F);
			GL11.glBegin(GL11.GL_LINE_STRIP);
			for (double[] posArray : this.positions) {
				GL11.glColor4f(58f / 255f, 187f / 255f, 187f / 255f, 1f);
				GL11.glVertex3d(posArray[0] - RenderManager.renderPosX, 
						posArray[1] - RenderManager.renderPosY,
						posArray[2] - RenderManager.renderPosZ);
			}
			GL11.glEnd();
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glEnable(2929);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_BLEND);
		}
	}
	
	@Marker
	public void onCommand(EventCommand eventCommand) {
		if (eventCommand.getArgs()[0].equalsIgnoreCase("bc") || eventCommand.getArgs()[0].equalsIgnoreCase("breadcrumb")) {
			try {
				if (eventCommand.getArgs()[1].equalsIgnoreCase("c") || eventCommand.getArgs()[1].equalsIgnoreCase("clear")) {
					this.positions.clear();
					eventCommand.setValid(true);
				}
			} catch (Exception exception) {
				exception.printStackTrace();
				Reliant.getInstance().printMessage("Breadcrumb Commands");
				Reliant.getInstance().printMessage(".bc c/clear - clear path");
				eventCommand.setValid(true);
			}
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
