package net.minecraft.reliant;

import net.minecraft.client.Minecraft;
import net.minecraft.reliant.event.EventManager;
import net.minecraft.reliant.event.Listener;
import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.Priority;
import net.minecraft.reliant.event.events.EventChatSend;
import net.minecraft.reliant.event.events.EventCommand;
import net.minecraft.reliant.event.events.EventInGame;
import net.minecraft.reliant.event.events.EventKey;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.gui.components.GuiUtils;
import net.minecraft.reliant.gui.components.NahrFont;
import net.minecraft.reliant.gui.screens.RelChat;
import net.minecraft.reliant.gui.screens.RelGui;
import net.minecraft.reliant.helpers.InvenClickHelper;
import net.minecraft.reliant.helpers.MiscUtils;
import net.minecraft.reliant.helpers.ModuleHelper;
import net.minecraft.reliant.helpers.RotationHelper;
import net.minecraft.reliant.io.Binds;
import net.minecraft.reliant.io.NameProtect;
import net.minecraft.reliant.io.Positions;
import net.minecraft.reliant.io.RelFolder;
import net.minecraft.reliant.io.Values;
import net.minecraft.reliant.module.Module;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GameSettings;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.WorldClient;

/**
 * Reliant is the main class for the hacked
 * client. It contains many wrapper methods,
 * as well as some hooks.
 * 
 * @author Nahr
 */
public class Reliant implements Listener {
	
	private static volatile Reliant instance;
	private static final Minecraft minecraft = Minecraft.getMinecraft();
	public static final String COMMAND_PREFIX = ".";
	private NahrFont windowTitle, buttonText;
	public NahrFont relFont;
	public NahrFont lucidaConsole;
	public RelGui reliantGui;
	public RelChat reliantChat;
	
	private Reliant() {
		this.getEventManager().addListener(this);
	}
	
	public static Reliant getInstance() {
		Reliant result = instance;
		if (result == null) {
			synchronized (Reliant.class) {
				result = instance;
				if (result == null) {
					result = new Reliant();
					instance = result;
				}
			}
		}
		return result;
	}
	
	/**
	 * Handles what needs to happen on game start.
	 * 
	 * @param eventStartup
	 * 		An Event parameter
	 */
	@Marker(eventPriority = Priority.HIGH)
	public void onStart(EventStartup eventStartup) {
        ScaledResolution scaledResolution = new ScaledResolution(this.getMinecraft().gameSettings, this.getMinecraft().displayWidth, this.getMinecraft().displayHeight);
		this.getModuleHelper().addModules();
		for (Module module : this.getModuleHelper().getModules()) {
			module.onStartup();
		}
		this.getModuleHelper().addCommands();
		this.reliantGui = new RelGui();
		this.reliantGui.addWindows2();
		this.reliantChat = new RelChat(this.getMinecraft());
		this.reliantChat.chatWidth = Math.round((float) scaledResolution.getScaledWidth_double() * 0.55f);
		this.reliantChat.chatWidth -= this.reliantChat.chatWidth % 17f;
		this.windowTitle = new NahrFont("Verdana Bold", 14, -1f);
		this.buttonText = new NahrFont("Tahoma Bold", 14);
		this.relFont = new NahrFont("Verdana Bold", 17, 1f);
		this.lucidaConsole = new NahrFont("Lucida Console", 20);
		this.getRelFolder().checkForFile();
		this.getBinds().checkForFile();
		this.getGuiPositions().checkForFile();
		this.getNameProtect().checkForFile();
		this.getValuesIO().checkForFile();
	}
	
	/**
	 * Handles what's displayed in-game.
	 * 
	 * @param eventInGame
	 * 		An Event parameter
	 */
	@Marker
	public void inGameDisplay(EventInGame eventInGame) {
		FontRenderer fontRenderer = eventInGame.getFontRenderer();
		int screenWidth = eventInGame.getScreenDimensions()[0];
		int screenHeight = eventInGame.getScreenDimensions()[1];
		int line = 2;
		for (Module module : this.getModuleHelper().getModules()) {
			if (!module.isHidden() && module.getState()) {
				fontRenderer.drawStringWithShadow(module.getDisplayName(), 
						screenWidth - fontRenderer.getStringWidth(module.getDisplayName()) - 2, 
						line, module.getColor());
				line += 10;
			}
		}
		fontRenderer.drawStringWithShadow("Reliant (rel-1.5.2)", 2, 2, 0xFFFFFFFF);
	}
	
	/**
	 * Handles when Keys are pressed. 
	 * 
	 * @param eventKey
	 * 		An Event parameter
	 */
	@Marker(eventPriority = Priority.HIGH)
	public void onKeyPressed(EventKey eventKey) {
		for (Module module : this.getModuleHelper().getModules()) {
			if (module.getKeyBind() == eventKey.getKeyEvent()) {
				module.toggleState();
			}
		}
	}
	
	/**
	 * Handles when the client wants to send something
	 * through chat. If it's recognized as a command,
	 * an EventCommand is fired.
	 * 
	 * @param eventChatSend
	 * 		An Event parameter
	 */
	@Marker
	public void onChatSend(EventChatSend eventChatSend) {
		String message = eventChatSend.getChatMessage();
		if (message.startsWith(this.COMMAND_PREFIX) && !message.endsWith(this.COMMAND_PREFIX)) {
			EventCommand eventCommand = new EventCommand(message);
			this.getEventManager().fireEvent(eventCommand);
			if (!eventCommand.isCommandValid()) {
				this.printMessage("Invalid command.");
			}
			eventChatSend.setCancelled(true);
		}
	}
	
	/**
	 * Prints a message in chat with "[Reliant]: "
	 * 
	 * @param message
	 * 		Message to print
	 */
	public void printMessage(String message) {
		this.getPlayer().addChatMessage("\247u[Reliant]: \247f" + message);
	}
	
	/**
	 * @return
	 * 		NahrFont instance for Windows
	 */
	public NahrFont getWindowFont() {
		 return this.windowTitle;
	}
	
	/**
	 * @return
	 * 		NahrFont instance for Buttons
	 */
	public NahrFont getButtonFont() {
		 return this.buttonText;
	}
	
	/**
	 * @return
	 * 		Instance of Minecraft
	 */
	public Minecraft getMinecraft() {
		return this.minecraft;
	}
	
	/**
	 * @return
	 * 		Instance of GameSettings
	 */
	public GameSettings getGameSettings() {
		return this.getMinecraft().gameSettings;
	}
	
	/**
	 * @return
	 * 		Instance of EntityClientPlayerMP
	 */
	public EntityClientPlayerMP getPlayer() {
		return this.getMinecraft().thePlayer;
	}
	
	/**
	 * @return
	 * 		Instance of WorldClient
	 */
	public WorldClient getWorld() {
		return this.getMinecraft().theWorld;
	}
	
	/**
	 * @return
	 * 		Instance of RenderGlobal
	 */
	public RenderGlobal getGlobalRenderer() {
		return this.getMinecraft().renderGlobal;
	}
	
	/**
	 * @return
	 * 		Instance of EventManager
	 */
	public EventManager getEventManager() {
		return EventManager.getInstance();
	}
	
	/**
	 * @return
	 * 		Instance of ModuleHelper
	 */
	public ModuleHelper getModuleHelper() {
		return ModuleHelper.getInstance();
	}
	
	/**
	 * @return
	 * 		Instance of RotationHelper
	 */
	public RotationHelper getRotationHelper() {
		return RotationHelper.getInstance();
	}
	
	/**
	 * @return
	 * 		Instance of GuiUtils
	 */
	public GuiUtils getGuiUtils() {
		return GuiUtils.getInstance();
	}
	
	/**
	 * @return
	 * 		Instance of InvenClickHelper
	 */
	public InvenClickHelper getClickHelper() {
		return InvenClickHelper.getInstance();
	}
	
	/**
	 * @return
	 * 		Instance of RelFolder
	 */
	public RelFolder getRelFolder() {
		return RelFolder.getInstance();
	}
	
	/**
	 * @return
	 * 		Instance of Binds
	 */
	public Binds getBinds() {
		return Binds.getInstance();
	}
	
	/**
	 * @return
	 * 		Instance of NameProtect
	 */
	public NameProtect getNameProtect() {
		return NameProtect.getInstance();
	}
	
	/**
	 * @return
	 * 		Instance of Positions
	 */
	public Positions getGuiPositions() {
		return Positions.getInstance();
	}
	
	/**
	 * @return
	 * 		Instance of MiscUtils
	 */
	public MiscUtils getMiscUtils() {
		return MiscUtils.getInstance();
	}
	
	/**
	 * @return
	 * 		Instance of Values
	 */
	public Values getValuesIO() {
		return Values.getInstance();
	}
	
	/**
	 * @return
	 * 		Current System time in milliseconds.
	 */
	public long getSystemMillis() {
		return System.nanoTime() / 1000000L;
	}
	
}
