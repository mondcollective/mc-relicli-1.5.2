package net.minecraft.reliant.module;

import java.util.ArrayList;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.event.EventManager;
import net.minecraft.reliant.event.Listener;
import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.Priority;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.helpers.InvenClickHelper;
import net.minecraft.reliant.helpers.MiscUtils;
import net.minecraft.reliant.helpers.RotationHelper;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.WorldClient;

public abstract class Module implements Listener {

	private String displayName;
	private String shortName;
	private ArrayList<Option> options = new ArrayList<Option>();
	private boolean state;
	private boolean hidden;
	private int keyBind = 0;
	private long lastTick = 0;
	
	public Module(String displayName, String shortName, int keyBind, boolean hidden) {
		this.getEventManager().addListener(this);
		this.displayName = displayName;
		this.shortName = shortName;
		this.keyBind = keyBind;
		this.hidden = hidden;
	}
	
	public Module(String displayName, int keyBind, boolean hidden) {
		this(displayName, displayName, keyBind, hidden);
	}
	
	/**
	 * Called on the game start. Used to add
	 * any Options or anything else.
	 */
	public abstract void onStartup();
	
	/**
	 * @return
	 * 		Hexadecimal (0xAARRGGBB) colour to represent the Module in the ArrayList.
	 */
	public abstract int getColor();
	
	/**
	 * @return
	 * 		Should the Module be able to be re-binded
	 */
	public abstract boolean canRebind();
	
	/**
	 * @return
	 * 		The String to display for the Module
	 */
	public String getDisplayName() {
		return this.displayName;
	}
	
	/**
	 * @return
	 * 		The short version of the Module's name. 
	 * 		Used for returning a Module object.
	 */
	public String getShortName() {
		return this.shortName;
	}
	
	/**
	 * Called when the Module is enabled.
	 */
	public void onEnable() {
		this.state = true;
	}
	
	/**
	 * Called when the Module is disabled.
	 */
	public void onDisable() {
		this.state = false;
	}
	
	/**
	 * Toggles the Module's state.
	 */
	public void toggleState() {
		this.state = !this.state;
		if (this.state) {
			this.onEnable();
		} else {
			this.onDisable();
		}
	}
	
	/**
	 * Sets the Module's state.
	 * 
	 * @param flag
	 */
	public final void setState(boolean flag) {
		this.state = flag;
		if (this.state) {
			this.onEnable();
		} else {
			this.onDisable();
		}
	}
	
	/**
	 * Returns true if enough time for the
	 * delay has passed.
	 * 
	 * @param delay
	 * 		Delay to check with
	 * @return
	 * 		Delay time has passed
	 */
	public boolean isDelayed(long delay) {
		return Reliant.getInstance().getSystemMillis() - this.lastTick >= delay;
	}
	
	/**
	 * @param name
	 * @return
	 * 		Option with a name matching the given name
	 */
	public final Option getOption(String name) {
		for (Option option : this.options) {
			if (option.getName().equalsIgnoreCase(name)) {
				return option;
			}
		}
		return null;
	}
	
	/**
	 * @return
	 * 		The state of the Module
	 */
	public final boolean getState() {
		return this.state;
	}
	
	/**
	 * @return
	 * 		Should Module be hidden
	 */
	public final boolean isHidden() {
		return this.hidden;
	}
	
	/**
	 * @return
	 * 		int representing the KeyBind
	 */
	public final int getKeyBind() {
		return this.keyBind;
	}
	
	/**
	 * @return
	 * 		The ArrayList of the Module's Options
	 */
	public final ArrayList<Option> getOptions() {
		return this.options;
	}
	
	/**
	 * Sets lastTick to the current System millisecond
	 * time. Used for resetting a delay after 
	 * isDelayed() returns true.
	 */
	public final void setLastTickNow() {
		this.setLastTick(Reliant.getInstance().getSystemMillis());
	}
	
	/**
	 * Set's the Module's lastTick variable. Used
	 * after isDelayed() returns true, to reset 
	 * the delay.
	 * 
	 * @param millis
	 * 		Millisecond time to set lastTick to
	 */
	public final void setLastTick(long millis) {
		this.lastTick = millis;
	}
	
	/**
	 * Adds an Option to the Module's list.
	 * 
	 * @param option
	 */
	public final void addOption(Option option) {
		boolean add = this.options.add(option);
		if (add) {
			System.out.println(this.getDisplayName() + ": Added Option \"" + option.getName() + "\"");
		} else {
			System.out.println("[Err]: " + this.getDisplayName() + ": Could not add Option \"" + option.getName() + "\"");
		}
	}
	
	/**
	 * Sets the Module's keybind to a new key.
	 * 
	 * @param keyBind
	 */
	public final void setKeybind(int keyBind) {
		if (keyBind < 0) {
			keyBind = 0;
		}
		this.keyBind = keyBind;
	}
	
	/**
	 * @return
	 * 		Instance of EventManager
	 */
	protected final EventManager getEventManager() {
		return Reliant.getInstance().getEventManager();
	}
	
	/**
	 * @return
	 * 		Instance of RotationHelper
	 */
	protected final RotationHelper getRotationHelper() {
		return Reliant.getInstance().getRotationHelper();
	}
	
	/**
	 * @return
	 * 		Instance of InvenClickHelper
	 */
	protected final InvenClickHelper getClickHelper() {
		return Reliant.getInstance().getClickHelper();
	}
	
	/**
	 * @return
	 * 		Instance of MiscUtils
	 */
	protected final MiscUtils getMiscUtils() {
		return Reliant.getInstance().getMiscUtils();
	}
	
	/**
	 * @return
	 * 		Instance of EntityClientPlayerMP
	 */
	protected final EntityClientPlayerMP getPlayer() {
		return Reliant.getInstance().getPlayer();
	}
	
	/**
	 * @return
	 * 		Instance of WorldClient
	 */
	protected final WorldClient getWorld() {
		return Reliant.getInstance().getWorld();
	}
	
}
