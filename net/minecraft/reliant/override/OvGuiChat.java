package net.minecraft.reliant.override;

import net.minecraft.reliant.Reliant;
import net.minecraft.src.GuiChat;

public class OvGuiChat extends GuiChat {

	public OvGuiChat() {
		;
	}
	
	public OvGuiChat(String par1Str) {
        this.defaultInputFieldText = par1Str;
    }
	
	@Override 
	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		Reliant.getInstance().reliantChat.drawScreen(i, j);
	}
	
	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		Reliant.getInstance().reliantChat.mouseClicked(i, j);
	}
	
	@Override
	protected void mouseMovedOrUp(int i, int j, int k) {
		super.mouseMovedOrUp(i, j, k);
		Reliant.getInstance().reliantChat.mouseReleased(i, j);
	}
	
}
