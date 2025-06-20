package net.minecraft.reliant.gui.components;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.module.Module;

public class ButtonModule extends Button {

	private Module module;
	
	public ButtonModule(String title, Module module) {
		super(title, -9999);
		this.module = module;
	}
	
	public ButtonModule(String title, String module) {
		super(title, -9999);
		this.module = Reliant.getInstance().getModuleHelper().getModuleByName(module);
	}
	
	@Override
	public void drawElement(int mouseX, int mouseY) {
		this.state = this.module.getState();
		super.drawElement(mouseX, mouseY);
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		super.mouseReleased(mouseX, mouseY);
		if (this.isHovering(mouseX, mouseY)) {
			this.module.toggleState();
		}
	}

}
