package net.minecraft.reliant.gui.components;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import net.minecraft.reliant.Reliant;
import net.minecraft.src.GuiScreen;

public abstract class GuiScreenModified extends GuiScreen {

	protected ArrayList<Window> windowList = new ArrayList<Window>();
	private Element selectedElement = null;
	
	@Override
	public final void initGui() {
		super.initGui();
		this.windowList.clear();
		this.addWindows();
		this.clearComponentParts();
		this.addButtons();
	}
	
	@Override
	public final void onGuiClosed() {
		super.onGuiClosed();
		Reliant.getInstance().getGuiPositions().saveFile();
	}
	
	/**
	 * Adds Windows to the list for drawing.
	 */
	public abstract void addWindows();
	
	/**
	 * Adds Buttons to the Windows.
	 */
	public abstract void addButtons();
	
	/**
	 * Handles when specific Buttons are pressed.
	 */
	public abstract void action(Button button);
	
	/**
	 * Clears the list of parts in components.
	 * e.g. clears the Buttons in Windows.
	 */
	private final void clearComponentParts() {
		for (Object o : this.windowList) {
			if (o instanceof Window) {
				((Window) o).componentList.clear();
			}
		}
	}
	
	@Override
	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		this.drawDefaultBackground();
		for (int index = this.windowList.size() - 1; index > -1; index--) {
			Window window = this.windowList.get(index);
			if (window.isDragging() && Mouse.isButtonDown(0)) {
				window.mouseDragged(i, j);
			}
			window.drawElement(i, j);
			for (int index1 = window.componentList.size() - 1; index1 > -1; index1 --) {
				Element element = window.componentList.get(index1);
				if (element instanceof Slider) {
					((Slider) element).mouseDragged(i, j);
				}
			}
		}
	}
	
	@Override
	public void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		try {
			for (Window window : this.windowList) {
				window.mouseClick(i, j);
			}
		} catch (Exception e) {
			;
		}
	}
	
	@Override
	public void mouseMovedOrUp(int i, int j, int k) {
		super.mouseMovedOrUp(i, j, k);
		if (this.selectedElement != null) {
			this.selectedElement.mouseReleased(i, j);
			if (this.selectedElement instanceof Button) {
				this.action((Button) this.selectedElement);
			}
		}
		for (Window window : this.windowList) {
			window.mouseReleased(i, j);
		}
		this.selectedElement = null;
	}
	
	/**
	 * Sets the selected Element to a Button / Slider.
	 * 
	 * @param element
	 */
	public final void setSelected(Element element) {
		this.selectedElement = element;
	}
	
	/**
	 * @return
	 * 		The screen's selected Element
	 */
	public final Element getSelected() {
		return this.selectedElement;
	}
	
	/**
	 * Adds a Window to the list for drawing.
	 * 
	 * @param window
	 * 		The Window to add
	 * @return
	 * 		true
	 */
	public final boolean addWindow(Window window) {
		return this.windowList.add(window);
	}
	
	/**
	 * @return
	 * 		List of Windows to draw.
	 */
	public final ArrayList<Window> getWindowList() {
		return this.windowList;
	}
	
	@Override
	public final boolean doesGuiPauseGame() {
		return false;
	}
	
}
