package net.minecraft.reliant.module.modules;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.events.EventRender;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.module.Module;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.RenderManager;

public class ModuleESP extends Module {

	public ModuleESP() {
		super("ESP", Keyboard.KEY_G, true);
	}

	@Override
	public void onStartup() {
	}

	/**
	 * Sorts through the Entities and renders the 
	 * tracer lines and ESP boxes.
	 * 
	 * @param eventRender
	 * 		An Event parameter
	 */
	@Marker
	public void onRender(EventRender eventRender) {
		if (this.getState()) {
			for (Entity entity : (List<Entity>) Reliant.getInstance().getWorld().loadedEntityList) {
				if (entity.equals(this.getPlayer())
						|| entity instanceof EntityClientPlayerMP) {
					continue;
				}
				if (Reliant.getInstance().getModuleHelper().killAura.getState()) {
					if (!Reliant.getInstance().getModuleHelper().killAura.shouldTarget2(entity)) {
						continue;
					} 
					if (!(entity instanceof EntityPlayer) && this.getPlayer().getDistanceToEntity(entity) > 20f) {
						continue;
					}
					this.drawTracer(entity);
					this.drawBox(entity);
				} else {
					if (!(entity instanceof EntityPlayer)) {
						continue;
					}
					this.drawTracer(entity);
					this.drawBox(entity);
				}
			}
		}
	}
	
	/**
	 * Draws a tracer line to an Entity.
	 * 
	 * @param entity
	 * 		The Entity to trace
	 */
	private void drawTracer(Entity entity) {
		float xDiff = (float) (entity.posX - RenderManager.renderPosX);
		float yDiff = (float) (entity.boundingBox.minY - RenderManager.renderPosY);
		float zDiff = (float) (entity.posZ - RenderManager.renderPosZ);
		float distance = this.getPlayer().getDistanceToEntity(entity);
		boolean friend = false;
		if (entity instanceof EntityPlayer) {
			EntityPlayer entityPlayer = (EntityPlayer) entity;
			String username2 = Reliant.getInstance().relFont.stripControlCodes(entityPlayer.username);
			for (String nameArray[] : Reliant.getInstance().getNameProtect().getNameList()) {
				if (nameArray[0].equalsIgnoreCase(username2)) {
        			friend = true;
        		} else {
                	username2 = username2.replaceAll("(?i)" + nameArray[0], nameArray[1]);
        		}
			}
			if (!username2.equalsIgnoreCase(Reliant.getInstance().relFont.stripControlCodes(entityPlayer.username))) {
				friend = true;
			}
		}
		this.getMiscUtils().start3DGLConstants();
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_FASTEST);
		float red = 1f;
		float green = 0;
		float blue = 0;
		if (friend) {
		    GL11.glColor4f(92f / 255f, 191f / 255f, 1f, 1.0f); 
		} else if (distance >= 64f) {
		    GL11.glColor4f(0f, 1f, 0f, 1.0f); 
		} else {
			green += (distance / 64f);
			GL11.glColor4f(red, green, blue, 1.0f); 
		}		
		GL11.glLineWidth(1.5F);
		GL11.glBegin(GL11.GL_LINES);
		   GL11.glVertex2f(0f, 0f);
		   GL11.glVertex3f(xDiff, yDiff, zDiff);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		this.getMiscUtils().finish3DGLConstants();
	}
	
	/**
	 * Draws an ESP box around an Entity.
	 * 
	 * @param entity
	 * 		The Entity to be drawn around
	 */
	private void drawBox(Entity entity) {
		float xDiff = (float) (entity.posX - RenderManager.renderPosX);
		float yDiff = (float) (entity.boundingBox.minY - RenderManager.renderPosY);
		float zDiff = (float) (entity.posZ - RenderManager.renderPosZ);
		float distance = this.getPlayer().getDistanceToEntity(entity);
		boolean friend = false;
		if (entity instanceof EntityPlayer) {
			EntityPlayer entityPlayer = (EntityPlayer) entity;
			String username2 = Reliant.getInstance().relFont.stripControlCodes(entityPlayer.username);
			for (String nameArray[] : Reliant.getInstance().getNameProtect().getNameList()) {
				if (nameArray[0].equalsIgnoreCase(username2)) {
        			friend = true;
        		} else {
                	username2 = username2.replaceAll("(?i)" + nameArray[0], nameArray[1]);
        		}
			}
			if (!username2.equalsIgnoreCase(Reliant.getInstance().relFont.stripControlCodes(entityPlayer.username))) {
				friend = true;
			}
		}
		this.getMiscUtils().start3DGLConstants();
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_FASTEST);
		float red = 1f;
		float green = 0;
		float blue = 0;
		if (friend) {
		    GL11.glColor4f(92f / 255f, 191f / 255f, 1f, 0.6f); 
		} else if (Reliant.getInstance().getModuleHelper().killAura.getState()) {
			float hitDistance = Reliant.getInstance().getModuleHelper().killAura.getOption("kill_range").getFloat();
			if (distance > hitDistance) {
			    GL11.glColor4f(0f, 1f, 0f, 0.4f); 
			} else if (entity.hurtResistantTime > 13) {
			    GL11.glColor4f(1f, 85f / 255f, 0f, 0.4f); 
			} else {
			    GL11.glColor4f(1f, 0f, 0f, 0.4f); 
			}
		} else if (distance >= 64f) {
		    GL11.glColor4f(0f, 1f, 0f, 0.4f); 
		} else {
			green += (distance / 64f);
			GL11.glColor4f(red, green, blue, 0.4f); 
		}
		GL11.glLineWidth(1.5F);
		float width = entity.width / (4f / 3f);
		AxisAlignedBB axisAlignedBB = AxisAlignedBB.getBoundingBox(xDiff - width, yDiff - 0.1f, zDiff - width, 
				xDiff + width, yDiff + entity.height + 0.2f, zDiff + width);
		GL11.glPushMatrix();
		GL11.glTranslatef(xDiff, yDiff, zDiff);
	    GL11.glRotatef(entity.rotationYaw, 0F, yDiff, 0F);
	    GL11.glTranslatef(-xDiff, -yDiff, -zDiff);
		this.getMiscUtils().drawSupportBeams(axisAlignedBB);
		this.getMiscUtils().drawOutlinedBoundingBox(axisAlignedBB);
	    GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		this.getMiscUtils().finish3DGLConstants();
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
