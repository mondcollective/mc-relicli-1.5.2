package net.minecraft.reliant.gui.components;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.module.Option;

public class ButtonOption extends Button {

	private Option option;
	
	public ButtonOption(String title, Option option) {
		super(title, -9999);
		this.option = option;
	}
	
	public ButtonOption(String title, String module, String option) {
		super(title, -9999);
		this.option = Reliant.getInstance().getModuleHelper().getModuleByName(module).getOption(option);
	}
	
	@Override
	public void drawElement(int mouseX, int mouseY) {
		this.state = this.option.getBoolean();
		super.drawElement(mouseX, mouseY);
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		super.mouseReleased(mouseX, mouseY);
		if (this.isHovering(mouseX, mouseY)) {
			this.option.toggleBoolean();
			Reliant.getInstance().getValuesIO().saveFile();
		}
	}

}
