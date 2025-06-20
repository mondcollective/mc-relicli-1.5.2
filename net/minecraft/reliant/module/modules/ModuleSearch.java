package net.minecraft.reliant.module.modules;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.events.EventCommand;
import net.minecraft.reliant.event.events.EventRender;
import net.minecraft.reliant.event.events.EventUpdate;
import net.minecraft.reliant.module.Module;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.RenderManager;

public class ModuleSearch extends Module {

	private int radius = 64;
	private int addedBlocks = 0;
	private int curBlockID = 0;
	private int blockLocations[][] = new int[100000][3];
	private long lastRefresh = 0;
	
	public ModuleSearch() {
		super("Search", Keyboard.KEY_NONE, true);
	}

	@Override
	public void onStartup() {
	}
	
	@Marker
	public void onRender(EventRender eventRender) {
		if (this.addedBlocks > 0 && this.getState()) {
			this.getMiscUtils().start3DGLConstants();
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
		    GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
		    GL11.glLineWidth(1F);
		    for (int row = 0; row < this.addedBlocks; row++) {
		    	double xPos = this.blockLocations[row][0] - RenderManager.renderPosX;
		    	double yPos = this.blockLocations[row][1] - RenderManager.renderPosY;
		    	double zPos = this.blockLocations[row][2] - RenderManager.renderPosZ;
		    	AxisAlignedBB axisAlignedBB = AxisAlignedBB.getBoundingBox(xPos + 1, yPos + 1, zPos + 1, xPos, yPos, zPos);
		    	GL11.glColor4f(17f / 255f, 132f / 255f, 97f / 255f, 0.4f); 
		    	this.getMiscUtils().drawBoundingBox(axisAlignedBB);
				GL11.glColor4f(17f / 255f, 132f / 255f, 97f / 255f, 1f);
				this.getMiscUtils().drawSupportBeams(axisAlignedBB);
				this.getMiscUtils().drawOutlinedBoundingBox(axisAlignedBB);
		    }
			this.getMiscUtils().finish3DGLConstants();
		}
	}
	
	@Marker
	public void onUpdate(EventUpdate eventUpdate) {
		this.setState(this.curBlockID > 0);
		if (this.getState() && Reliant.getInstance().getSystemMillis() - this.lastRefresh >= 10000) {
			this.addedBlocks = 0;
			for (int x = 0; x < this.radius * 2; x++) {
				for (int z = 0; z < this.radius * 2; z++) {
					for (int y = 0; y < this.radius * 2; y++) {
						int xPos = Math.round((float) this.getPlayer().posX) - this.radius + x;
						int yPos = Math.round((float) this.getPlayer().posY) - this.radius + y;
						int zPos = Math.round((float) this.getPlayer().posZ) - this.radius + z;
						int blockID = this.getWorld().getBlockId(xPos, yPos, zPos);
						if (blockID != 0 && blockID == this.curBlockID && this.addedBlocks < 100000) {
							int am = this.addedBlocks;
							this.blockLocations[am][0] = xPos;
							this.blockLocations[am][1] = yPos;
							this.blockLocations[am][2] = zPos;
							this.addedBlocks++;
						}
					}
				}
			}
			this.lastRefresh = Reliant.getInstance().getSystemMillis();
		}
	}
	
	@Marker
	public void onCommand(EventCommand eventCommand) {
		String args[];
		try {
			args = eventCommand.getArgs();
			if (args[0].equalsIgnoreCase("search")) {
				try {
					this.curBlockID = Integer.parseInt(args[1]);
					if (this.curBlockID == 0) {
						this.addedBlocks = 0;
						this.blockLocations = new int[100000][3];
						Reliant.getInstance().printMessage("Search cleared.");
					} else {
						Reliant.getInstance().printMessage("Search: 1 block types.");
					}
				} catch (Exception exception) {
					exception.printStackTrace();
					this.curBlockID = 0;
					this.addedBlocks = 0;
					this.blockLocations = new int[100000][3];
					Reliant.getInstance().printMessage("Search cleared.");
				}
				eventCommand.setValid(true);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
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
