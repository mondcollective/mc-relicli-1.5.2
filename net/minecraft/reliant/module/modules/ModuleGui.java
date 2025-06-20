package net.minecraft.reliant.module.modules;

import org.lwjgl.input.Keyboard;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.module.Module;

public class ModuleGui extends Module {

	public ModuleGui() {
		super("GUI", Keyboard.KEY_GRAVE, true);
	}

	@Override
	public void onStartup() {
	}

	@Override
	public void toggleState() {
		super.toggleState();
		Reliant.getInstance().getMinecraft().displayGuiScreen(Reliant.getInstance().reliantGui);
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
