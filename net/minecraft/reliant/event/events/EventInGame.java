package net.minecraft.reliant.event.events;

import net.minecraft.reliant.event.Event;
import net.minecraft.src.FontRenderer;

public class EventInGame extends Event {

	private FontRenderer fontRenderer;
	private int screenDimensions[] = new int[2];
	
	public EventInGame(FontRenderer fontRenderer, int width, int height) {
		this.fontRenderer = fontRenderer;
		this.screenDimensions[0] = width;
		this.screenDimensions[1] = height;
	}
	
	/**
	 * @return
	 * 		Instance of FontRenderer
	 */
	public FontRenderer getFontRenderer() {
		return this.fontRenderer;
	}
	
	/**
	 * @return
	 * 		Dimensions of the screen. [0] - width, [1] - height
	 */
	public int[] getScreenDimensions() {
		return this.screenDimensions;
	}
	
}
