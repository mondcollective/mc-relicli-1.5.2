package net.minecraft.reliant.module.modules;

import org.lwjgl.input.Keyboard;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.module.Module;
import net.minecraft.reliant.module.Option;
import net.minecraft.src.Block;

public class ModuleWallhack extends Module {
	// TODO: Rest of wallhack
	public ModuleWallhack() {
		super("Wallhack", "xray", Keyboard.KEY_X, false);
	}

	@Override
	public void onStartup() {
		this.addOption(new Option("world_alpha", 120, new Integer[] {
				30, 255
		}));
		this.addOption(new Option("valuable", true));
		this.addOption(new Option("fuel", false));
		this.addOption(new Option("iron", false));
		this.addOption(new Option("diamond", false));
		this.addOption(new Option("gold", false));
		this.addOption(new Option("redstone", false));
		this.addOption(new Option("lapis_lazuli", false));
		this.addOption(new Option("danger", true));
		this.addOption(new Option("circuits", false));
		this.addOption(new Option("dungeon", false));
	}

	@Override
	public void toggleState() {
		super.toggleState();
		Reliant.getInstance().getGlobalRenderer().loadRenderers();
	}
	
	@Override
	public int getColor() {
		return 0xFF7C9E9A;
	}

	@Override
	public boolean canRebind() {
		return true;
	}
	
	/**
	 * Returns true if a Block should be shown in the Wallhack.
	 * 
	 * @param block
	 * 		The Block in question
	 * @return
	 * 		True if the Block should be shown
	 */
	public boolean shouldShowBlock(Block block) {
		int blockID = block.blockID;
		boolean valuable = this.getOption("valuable").getBoolean() &&
				this.getMiscUtils().doesArrayContain(this.valuableID, blockID);
		boolean fuel = this.getOption("fuel").getBoolean() &&
				this.getMiscUtils().doesArrayContain(this.fuelID, blockID);
		boolean iron = this.getOption("iron").getBoolean() &&
				this.getMiscUtils().doesArrayContain(this.ironID, blockID);
		boolean diamond = this.getOption("diamond").getBoolean() &&
				this.getMiscUtils().doesArrayContain(this.diamondID, blockID);
		boolean gold = this.getOption("gold").getBoolean() &&
				this.getMiscUtils().doesArrayContain(this.goldID, blockID);
		boolean redstone = this.getOption("redstone").getBoolean() &&
				this.getMiscUtils().doesArrayContain(this.redstoneID, blockID);
		boolean lapis = this.getOption("lapis_lazuli").getBoolean() &&
				this.getMiscUtils().doesArrayContain(this.lapisID, blockID);
		boolean danger = this.getOption("danger").getBoolean() &&
				this.getMiscUtils().doesArrayContain(this.dangerID, blockID);
		boolean circuits = this.getOption("circuits").getBoolean() &&
				this.getMiscUtils().doesArrayContain(this.circuitsID, blockID);
		boolean dungeon = this.getOption("dungeon").getBoolean() &&
				this.getMiscUtils().doesArrayContain(this.dungeonID, blockID);
		return valuable || fuel || iron || diamond || gold
				|| redstone || lapis || danger || circuits || dungeon;
	}
	
	/**
	 * Sponge, Gold block, Iron block, TNT, Diamond block, Pumpkin, Jack-O-Lantern
	 */
	private Integer valuableID[] = new Integer[] {
			19, 41, 42, 46, 57, 86, 91
	};
	
	/**
	 * Planks, Coal, Logs, Oak stairs, Crafting bench, Furnace, Furnace2, Spruce stairs, 
	 * Birch stairs, Jungle stairs
	 */
	private Integer fuelID[] = new Integer[] {
			5, 16, 17, 53, 58, 61, 62, 134, 
			135, 136
	};

	/**
	 * Iron ore, Iron block
	 */
	private Integer ironID[] = new Integer[] {
			15, 42
	}; 
	
	/**
	 * Diamond ore, Diamond block
	 */
	private Integer diamondID[] = new Integer[] {
			56, 57
	};
	
	/**
	 * Gold ore, Gold block
	 */
	private Integer goldID[] = new Integer[] {
			14, 41
	};
	
	/**
	 * Redstone ore, Redstone ore2
	 */
	private Integer redstoneID[] = new Integer[] {
			73, 74
	};
	
	/**
	 * Lapis ore, Lapis block
	 */
	private Integer lapisID[] = new Integer[] {
			21, 22
	};
	
	/**
	 * Lava, Lava2, TNT
	 */
	private Integer dangerID[] = new Integer[] {
			10, 11, 46
	}; 
	
	/**
	 * Dispenser, Sticky piston, Piston, Wire, Lever, Torch (Off), Torch (On), Stone button,
	 * Repeater (Off), Repeater (On), Tripwire hook, Tripwire, Wood button
	 */
	private Integer circuitsID[] = new Integer[] {
			23, 29, 33, 55, 69, 75, 76, 77, 
			93, 94, 131, 132, 143
	}; 
	
	/**
	 * Mossy cobble, Spawner
	 */
	private Integer dungeonID[] = new Integer[] {
			48, 52
	};

}
