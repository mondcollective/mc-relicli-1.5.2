package net.minecraft.reliant.gui.screens;

import java.util.ArrayList;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.gui.components.*;

public class RelGui extends GuiScreenModified {

	public ArrayList<Window> loadedWindowList = new ArrayList<Window>();
	private Window killAura = new Window("Kill Aura", 4f, 4f, 100f);
	private Window modes = new Window("Modes", 108f, 4f, 100f);
	private Window values = new WindowValues(212f, 4f);
	private Window wallhack = new Window("Wallhack", 316f, 4f, 100f);
	private Window retardMode = new Window("Retard Mode", 4f, 116f, 100f);
	
	/**
	 * Adds the Windows to the loadedWindowList. Used
	 * for position loading and saving.
	 */
	public void addWindows2() {
		this.loadedWindowList.add(killAura);
		this.loadedWindowList.add(modes);
		this.loadedWindowList.add(values);
		this.loadedWindowList.add(wallhack);
		this.loadedWindowList.add(retardMode);
	}
	
	@Override
	public void addWindows() {
		this.addWindow(killAura);
		this.addWindow(modes);
		this.addWindow(values);
		this.addWindow(wallhack);
		this.addWindow(retardMode);
	}

	@Override
	public void addButtons() {
		/***Kill Aura***/
		this.killAura.addElement(new ButtonOption("Players", "aura", "players"));
		this.killAura.addElement(new ButtonOption("Hostile Mobs", "aura", "hostile_mobs"));
		this.killAura.addElement(new ButtonOption("Neutral Mobs", "aura", "neutral_mobs"));
		this.killAura.addElement(new ButtonOption("Passive Animals", "aura", "passive_animals"));
		this.killAura.addElement(new ButtonOption("Tame Animals", "aura", "tame_animals"));
		this.killAura.addElement(new ButtonOption("Vehicles", "aura", "vehicles"));
		/***Retard Mode***/
		this.retardMode.addElement(new ButtonOption("Retard", "retard", "retard") {
			public void mouseReleased(int mouseX, int mouseY) {
				Reliant.getInstance().getModuleHelper().getModuleByName("retard").getOption("headless").setValue(false);
				Reliant.getInstance().getModuleHelper().getModuleByName("retard").getOption("stare").setValue(false);
				Reliant.getInstance().getModuleHelper().getModuleByName("retard").getOption("add").setValue(false);
				super.mouseReleased(mouseX, mouseY);
			} 
		});
		this.retardMode.addElement(new ButtonOption("Headless", "retard", "headless") {
			public void mouseReleased(int mouseX, int mouseY) {
				Reliant.getInstance().getModuleHelper().getModuleByName("retard").getOption("retard").setValue(false);
				Reliant.getInstance().getModuleHelper().getModuleByName("retard").getOption("stare").setValue(false);
				Reliant.getInstance().getModuleHelper().getModuleByName("retard").getOption("add").setValue(false);
				super.mouseReleased(mouseX, mouseY);
			} 
		});
		this.retardMode.addElement(new ButtonOption("Stare", "retard", "stare") {
			public void mouseReleased(int mouseX, int mouseY) {
				Reliant.getInstance().getModuleHelper().getModuleByName("retard").getOption("retard").setValue(false);
				Reliant.getInstance().getModuleHelper().getModuleByName("retard").getOption("headless").setValue(false);
				Reliant.getInstance().getModuleHelper().getModuleByName("retard").getOption("add").setValue(false);
				super.mouseReleased(mouseX, mouseY);
			} 
		});
		this.retardMode.addElement(new ButtonOption("ADD", "retard", "add") {
			public void mouseReleased(int mouseX, int mouseY) {
				Reliant.getInstance().getModuleHelper().getModuleByName("retard").getOption("retard").setValue(false);
				Reliant.getInstance().getModuleHelper().getModuleByName("retard").getOption("headless").setValue(false);
				Reliant.getInstance().getModuleHelper().getModuleByName("retard").getOption("stare").setValue(false);
				super.mouseReleased(mouseX, mouseY);
			} 
		});
		/***Modes***/
		this.modes.addElement(new ButtonModule("Breadcrumb", "crumb"));
		this.modes.addElement(new ButtonModule("ReliantChat", "chat"));
		this.modes.addElement(new ButtonModule("NameProtect", "name"));
		this.modes.addElement(new ButtonModule("NoCheat", "nocheat"));
		this.modes.addElement(new ButtonModule("Sneak", "sneak"));
		this.modes.addElement(new ButtonModule("Fly Hack", "flight"));
		this.modes.addElement(new ButtonModule("NoFall", "nofall"));
		this.modes.addElement(new ButtonModule("NoWeather", "weather"));
		this.modes.addElement(new ButtonModule("Revive", "revive"));
		this.modes.addElement(new ButtonModule("Speed Hack", "speed"));
		this.modes.addElement(new ButtonModule("Speedy Gonzales", "gonzales"));
		/***Values***/
		this.values.addElement(new Slider("Step Height", "step", "step_height"));
		this.values.addElement(new Slider("Flight Speed", "flight", "flight_speed"));
		this.values.addElement(new Slider("Flight Speed Cap", "flight", "flight_cap"));
		this.values.addElement(new Slider("Brightness", "bright", "brightness"));
		this.values.addElement(new Slider("Run Speed", "speed", "run_speed"));
		this.values.addElement(new Slider("Run Speed Cap", "speed", "run_cap"));
		this.values.addElement(new Slider("Fast Place", "gonzales", "fast_place"));
		this.values.addElement(new Slider("World Alpha", "wallhack", "world_alpha") {
			public void mouseReleased(int mouseX, int mouseY) {
				super.mouseReleased(mouseX, mouseY);
				if (Reliant.getInstance().getModuleHelper().getModuleByName("xray").getState()) {
					Reliant.getInstance().getMinecraft().renderGlobal.loadRenderers();
				}
			}
		});
		/***Wallhack***/
		this.wallhack.addElement(new ButtonOption("Valuable", "xray", "valuable") {
			public void mouseReleased(int mouseX, int mouseY) {
				super.mouseReleased(mouseX, mouseY);
				if (Reliant.getInstance().getModuleHelper().getModuleByName("xray").getState()) {
					Reliant.getInstance().getMinecraft().renderGlobal.loadRenderers();
				}
			}
		});
		this.wallhack.addElement(new ButtonOption("Fuel", "xray", "fuel") {
			public void mouseReleased(int mouseX, int mouseY) {
				super.mouseReleased(mouseX, mouseY);
				if (Reliant.getInstance().getModuleHelper().getModuleByName("xray").getState()) {
					Reliant.getInstance().getMinecraft().renderGlobal.loadRenderers();
				}
			}
		});
		this.wallhack.addElement(new ButtonOption("Iron", "xray", "iron") {
			public void mouseReleased(int mouseX, int mouseY) {
				super.mouseReleased(mouseX, mouseY);
				if (Reliant.getInstance().getModuleHelper().getModuleByName("xray").getState()) {
					Reliant.getInstance().getMinecraft().renderGlobal.loadRenderers();
				}
			}
		});
		this.wallhack.addElement(new ButtonOption("Diamond", "xray", "diamond") {
			public void mouseReleased(int mouseX, int mouseY) {
				super.mouseReleased(mouseX, mouseY);
				if (Reliant.getInstance().getModuleHelper().getModuleByName("xray").getState()) {
					Reliant.getInstance().getMinecraft().renderGlobal.loadRenderers();
				}
			}
		});
		this.wallhack.addElement(new ButtonOption("Gold", "xray", "gold") {
			public void mouseReleased(int mouseX, int mouseY) {
				super.mouseReleased(mouseX, mouseY);
				if (Reliant.getInstance().getModuleHelper().getModuleByName("xray").getState()) {
					Reliant.getInstance().getMinecraft().renderGlobal.loadRenderers();
				}
			}
		});
		this.wallhack.addElement(new ButtonOption("Redstone", "xray", "redstone") {
			public void mouseReleased(int mouseX, int mouseY) {
				super.mouseReleased(mouseX, mouseY);
				if (Reliant.getInstance().getModuleHelper().getModuleByName("xray").getState()) {
					Reliant.getInstance().getMinecraft().renderGlobal.loadRenderers();
				}
			}
		});
		this.wallhack.addElement(new ButtonOption("Lapis Lazuli", "xray", "lapis_lazuli") {
			public void mouseReleased(int mouseX, int mouseY) {
				super.mouseReleased(mouseX, mouseY);
				if (Reliant.getInstance().getModuleHelper().getModuleByName("xray").getState()) {
					Reliant.getInstance().getMinecraft().renderGlobal.loadRenderers();
				}
			}
		});
		this.wallhack.addElement(new ButtonOption("Danger", "xray", "danger") {
			public void mouseReleased(int mouseX, int mouseY) {
				super.mouseReleased(mouseX, mouseY);
				if (Reliant.getInstance().getModuleHelper().getModuleByName("xray").getState()) {
					Reliant.getInstance().getMinecraft().renderGlobal.loadRenderers();
				}
			}
		});
		this.wallhack.addElement(new ButtonOption("Circuits", "xray", "circuits") {
			public void mouseReleased(int mouseX, int mouseY) {
				super.mouseReleased(mouseX, mouseY);
				if (Reliant.getInstance().getModuleHelper().getModuleByName("xray").getState()) {
					Reliant.getInstance().getMinecraft().renderGlobal.loadRenderers();
				}
			}
		});
		this.wallhack.addElement(new ButtonOption("Dungeon", "xray", "dungeon") {
			public void mouseReleased(int mouseX, int mouseY) {
				super.mouseReleased(mouseX, mouseY);
				if (Reliant.getInstance().getModuleHelper().getModuleByName("xray").getState()) {
					Reliant.getInstance().getMinecraft().renderGlobal.loadRenderers();
				}
			}
		});
	}

	@Override
	public void action(Button button) {
		switch (button.buttonID) {
		
		}
	}

}
