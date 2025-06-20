package net.minecraft.reliant.helpers;

import java.util.ArrayList;

import net.minecraft.reliant.module.Command;
import net.minecraft.reliant.module.Module;
import net.minecraft.reliant.module.modules.*;
import net.minecraft.reliant.module.commands.*;
import net.minecraft.src.Block;

public class ModuleHelper {

	private static volatile ModuleHelper instance;
	private ArrayList<Module> moduleList = new ArrayList<Module>();
	private ArrayList<Command> commandList = new ArrayList<Command>();
	public ModuleFreecam freeCam;
	public ModuleKillAura killAura;
	public ModuleWallhack wallHack;
	
	public static ModuleHelper getInstance() {
		ModuleHelper result = instance;
		if (result == null) {
			synchronized (ModuleHelper.class) {
				result = instance;
				if (result == null) {
					result = new ModuleHelper();
					instance = result;
				}
			}
		}
		return result;
	}
	
	/**
	 * Adds all the Modules to the list.
	 */
	public void addModules() {
		this.addModule(new ModuleAnnoy()); 			
		this.addModule(new ModuleFlight());			
		this.addModule(new ModuleFullbright());		// Work - Find out how to modify lightmap without gammaSetting or reloading. Fading.
		this.addModule(this.killAura = new ModuleKillAura());		
		this.addModule(new ModuleNoFall());
		this.addModule(new ModuleNoWeather()); 
		this.addModule(new ModuleRetardMode());			
		this.addModule(new ModuleSneak()); 			
		this.addModule(new ModuleSpeed()); 			
		this.addModule(new ModuleSpeedyGonzales());	// Work - Figure out how to increase damage. FastPlace hook
		this.addModule(this.wallHack = new ModuleWallhack());	
		/********Hidden********/
		this.addModule(new ModuleBreadcrumb());
		this.addModule(new ModuleESP());	
		this.addModule(this.freeCam = new ModuleFreecam());
		this.addModule(new ModuleGui());
		this.addModule(new ModuleNameProtect());	
		this.addModule(new ModuleNoCheat());
		this.addModule(new ModuleReliantChat());	
		this.addModule(new ModuleRevive());	
		this.addModule(new ModuleSearch());
		this.addModule(new ModuleStep());			
	}
	
	/**
	 * Adds all the commands to the list.
	 */
	public void addCommands() {
		this.addCommand(new CommandAdd());
		this.addCommand(new CommandDel());
		this.addCommand(new CommandEnchantList());
	}
	
	/**
	 * @param name
	 * 		Name of a Module
	 * @return
	 * 		Module matching the name provided.
	 */
	public Module getModuleByName(String name) {
		for (Module module : this.moduleList) {
			if (module.getShortName().equalsIgnoreCase(name)
					|| module.getDisplayName().equalsIgnoreCase(name)) {
				return module;
			}
		}
		return null;
	}
	
	/**
	 * Adds a Module to the list.
	 * 
	 * @param module
	 */
	public void addModule(Module module) {
		boolean add = this.moduleList.add(module);
		if (add) {
			System.out.println("Loaded Module \"" + module.getDisplayName() + "\"");
		} else {
			System.out.println("[Err]: Could not load Module \"" + module.getDisplayName() + "\"");
		}
	}
	
	/**
	 * Adds a Command to the list.
	 * 
	 * @param command
	 */
	public void addCommand(Command command) {
		boolean add = this.commandList.add(command);
		if (add) {
			System.out.println("Loaded Command \"" + command.getFirstName() + "\"");
		} else {
			System.out.println("[Err]: Could not load Command \"" + command.getFirstName() + "\"");
		}
	}
	
	/**
	 * @return
	 * 		List of Modules
	 */
	public ArrayList<Module> getModules() {
		return this.moduleList;
	}
	
	/**
	 * @return
	 * 		List of Commands
	 */
	public ArrayList<Command> getCommands() {
		return this.commandList;
	}
	
	/**
	 * @return
	 * 		Opacity for the Blocks.
	 */
	public int getOpacityHook() {
		if (this.wallHack != null && this.wallHack.getState()) {
			return this.wallHack.getOption("world_alpha").getInteger();
		}
		return 255;
	}
	
	/**
	 * Checks if the Wallhack is enabled. If so, checks if
	 * the block should be shown as solid by the Wallhack.
	 * Returns true if it should.
	 * 
	 * @param block
	 * 		The Block to check
	 * @return
	 * 		True if should be shown by the Wallhack, false
	 * 		otherwise
	 */
	public boolean shouldSideBeRenderedHook(Block block) {
		if (this.wallHack != null && this.wallHack.getState()) {
			return this.wallHack.shouldShowBlock(block);
		}
		return false;
	}
	
	/**
	 * Checks if the Wallhack is enabled. If so, checks if
	 * the block should be shown as solid by the Wallhack.
	 * Changes the Block's pass based on this.
	 * 
	 * @param block
	 * 		The Block to check
	 * @return
	 * 		Render pass for the Block
	 */
	public int renderBlockPassHook(Block block) {
		if (this.wallHack != null && this.wallHack.getState() && this.wallHack.shouldShowBlock(block)) {
			return 0;
		} else if (this.wallHack != null && this.wallHack.getState()) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * Checks if the Wallhack is enabled. If so, checks if
	 * the block should be shown as solid by the Wallhack.
	 * Changes the Block's brightness based on this.
	 * 
	 * @param block
	 * 		The Block to check
	 * @param curBrightness
	 * 		The current brightness of the Block
	 * @return
	 * 		New brightness for the Block
	 */
	public float getBlockBrightnessHook(Block block, float curBrightness) {
		if (this.wallHack != null && this.wallHack.getState() && this.wallHack.shouldShowBlock(block)) {
			return 255f;
		} else if (this.wallHack != null && this.wallHack.getState()) {
			return 200f;
		}
		return curBrightness;
	}
	
}
