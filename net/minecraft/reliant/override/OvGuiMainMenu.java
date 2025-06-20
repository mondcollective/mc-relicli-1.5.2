package net.minecraft.reliant.override;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.gui.screens.Login;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMainMenu;

public class OvGuiMainMenu extends GuiMainMenu {

	public void initGui() {
		super.initGui();
		//this.buttonList.add(new GuiButton(666, 3, 12, 100, 20, "Login to Account"));
	}
	
	public void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		if (button.id == 666) {
			//Reliant.getInstance().getMinecraft().displayGuiScreen(new Login());
		}
	}
	
	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		this.fontRenderer.drawStringWithShadow("Logged in as: " + this.mc.session.username, 2, 2, 0xFFFFFF55);
	}
	
}
