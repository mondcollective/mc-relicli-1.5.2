package net.minecraft.reliant.gui.components;

import java.text.DecimalFormat;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.module.Option;

public class Slider extends Element {

	private String name;
	private Option option;
	private int type;
	public Window attachedWindow;				// Window Element to attach the Button to. Used for determining where to draw the Button.
	private int dragX, dragY;					// Variables used when the Window is dragged, and the button needs to be, too.
	private float x, width;						// Variables used to draw the Button and to handle the hovering.
	private float yPos = -666;					// Variable used to draw the Button at a certain Y coordinate.
	protected boolean dragging = false;	
	private float percent;
	private float minValue, maxValue;
	
	public Slider(String name, Option option) {
		this.name = name;
		this.option = option;
		this.type = option.getType();
		this.width = 92f;
		switch (this.type) {
			case 0: /*Integer*/
				this.minValue = (float) ((Integer) option.getMinAndMax()[0]);
				this.maxValue = (float) ((Integer) option.getMinAndMax()[1]);
				this.percent = this.getPercent(option.getInteger());
				break;
				
			case 1: /*Float*/
				this.minValue = (Float) option.getMinAndMax()[0];
				this.maxValue = (Float) option.getMinAndMax()[1];
				this.percent = this.getPercent(option.getFloat());
				break;
				
			case 2: /*Double*/
				this.minValue = (Float) option.getMinAndMax()[0];
				this.maxValue = (Float) option.getMinAndMax()[1];
				this.percent = this.getPercent((float) option.getDouble());
				break;
		}
	}
	
	public Slider(String name, String module, String option) {
		this(name, Reliant.getInstance().getModuleHelper().getModuleByName(module).getOption(option));
	}
	
	@Override
	public void drawElement(int mouseX, int mouseY) {
		this.x = this.attachedWindow.getXPos() + 4f;
		this.dragX = this.attachedWindow.getDragX();
		this.dragY = this.attachedWindow.getDragY();
		this.getButtonFont().drawString(this.name, this.x + this.dragX, this.yPos + this.dragY, 
				NahrFont.TEXT_EMBOSSED_BOTTOM, 0xFFC0C0C0, 0xFF1F1F1F);
		DecimalFormat decimalFormat = new DecimalFormat(this.type == 0 ? "0" : "0.00");
		String valueShow = decimalFormat.format(this.getFinalValue(this.percent));
		this.getButtonFont().drawString(valueShow, 
				this.x + this.width - this.getButtonFont().getStringWidth(valueShow) + this.dragX, 
				this.yPos + this.dragY, NahrFont.TEXT_EMBOSSED_BOTTOM, 0xFFC0C0C0, 0xFF1F1F1F);
		this.drawSlider(mouseX, mouseY, this.percent);
	}

	@Override
	public void mouseClick(int mouseX, int mouseY) {
		if (this.isHovering(mouseX, mouseY)) {
			this.percent = (float) ((mouseX - this.x) - this.dragX) / 90f;

            if (this.percent < 0.0F) {
                this.percent = 0.0F;
            }

            if (this.percent > 1.0F) {
                this.percent = 1.0F;
            }
			
            switch (this.type) {
				case 0: /*Integer*/
					this.option.setValue(Math.round(this.getFinalValue(this.percent)));
					break;
					
				case 1: /*Float*/
					this.option.setValue(this.getFinalValue(this.percent));
					break;
					
				case 2:
					this.option.setValue((double) this.getFinalValue(this.percent));
					break;
            }
			this.dragging = true;
		} else {
			this.dragging = false;
		}
	}

	public void mouseDragged(int mouseX, int mouseY) {
		if (this.dragging) {
			this.percent = (float) ((mouseX - this.x) - this.dragX) / 90f;

            if (this.percent < 0.0F) {
                this.percent = 0.0F;
            }

            if (this.percent > 1.0F) {
                this.percent = 1.0F;
            }
			
            switch (this.type) {
				case 0: /*Integer*/
					this.option.setValue(Math.round(this.getFinalValue(this.percent)));
					break;
					
				case 1: /*Float*/
					this.option.setValue(this.getFinalValue(this.percent));
					break;
					
				case 2:
					this.option.setValue((double) this.getFinalValue(this.percent));
					break;
            }
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		switch (this.type) {
			case 0: /*Integer*/
				this.option.setValue(Math.round(this.getFinalValue(this.percent)));
				break;
				
			case 1: /*Float*/
				this.option.setValue(this.getFinalValue(this.percent));
				break;
				
			case 2:
				this.option.setValue((double) this.getFinalValue(this.percent));
				break;
		}
		Reliant.getInstance().getValuesIO().saveFile();
		this.dragging = false;
	}

	@Override
	public boolean isHovering(int mouseX, int mouseY) {
		float x = this.percent * 90f - 3;
		return mouseX >= this.x + x + this.dragX
				&& mouseY >= this.yPos + 10.5f + this.dragY
				&& mouseX <= this.x + x + 8 + this.dragX
				&& mouseY <= this.yPos + 19.5f + this.dragY;
	}

	private void drawSlider(int mouseX, int mouseY, float percentage) {
		this.drawTexturedRectangle("/GuiRedux.png", this.x - 3f + this.dragX, this.yPos + 12 + this.dragY, 3f, 7.5f, 0, 63, 6.5f, 15); // Grey round left
		this.drawTexturedRectangle("/GuiRedux.png", this.x + this.dragX, this.yPos + 12 + this.dragY, 91.5f, 7.5f, 8, 63, 10, 15); // Grey main
		this.drawTexturedRectangle("/GuiRedux.png", this.x + this.dragX + 91.5f, this.yPos + 12 + this.dragY, 3f, 7.5f, 93, 63, 7f, 15); // Grey round right

		float x2 = percentage * 90f;
		this.drawTexturedRectangle("/GuiRedux.png", this.x - 2.5f + this.dragX, this.yPos + 13.5f + this.dragY, 3, 5, 0, 53, 6.5f, 10); // Blue round left
		this.drawTexturedRectangle("/GuiRedux.png", this.x + 0.5f + this.dragX, this.yPos + 13.5f + this.dragY, x2, 5, 8, 53, 10, 10); // Blue main

		this.drawSliderHandle(mouseX, mouseY, percentage);
	}
	
	private void drawSliderHandle(int mouseX, int mouseY, float percentage) {
		float x = percentage * 90f;
		if (this.dragging) {
			this.drawTexturedRectangle("/GuiRedux.png", this.x + x - 2.5f + this.dragX, this.yPos + 10.5f + this.dragY, 9f, 9.5f, 37, 77, 18.5f, 18);
		} else if (this.isHovering(mouseX, mouseY)) {
			this.drawTexturedRectangle("/GuiRedux.png", this.x + x - 2.5f + this.dragX, this.yPos + 10.5f + this.dragY, 9f, 9.5f, 18.5f, 77, 18, 18);
		} else {
			this.drawTexturedRectangle("/GuiRedux.png", this.x + x - 3 + this.dragX, this.yPos + 10.5f + this.dragY, 9f, 9.5f, 0, 77, 18, 18);
		}
	}
	
	private float getPercent(float value) {
		return (value - this.minValue) / (this.maxValue - this.minValue);
	}
	
	private float getFinalValue(float percent) {
		float value = (percent * (this.maxValue - this.minValue)) + this.minValue;
		if (this.type == 0 /*Integer*/) {
			value = Math.round(value);
		}
		return Reliant.getInstance().getMiscUtils().formatFloat(2, value);
	}
	
	@Override
	public void addElement(Element element) {
	}

	@Override
	public float getSpacing() {
		return 20;
	}
	
	/**
	 * Sets the Y position of the Slider.
	 * 
	 * @param yPos
	 */
	public void setYPos(float yPos) {
		this.yPos = yPos;
	}

}
