package net.minecraft.reliant.module;

public class InvenClick {

	private int inventorySlot;
	private boolean shiftClick;
	private long delay;
	
	/**
	 * Instantiates a new InvenClick with a delay for
	 * bypassing anti-hacking plugins.
	 * 
	 * @param slot
	 * 		The Inventory slot to click
	 * @param shift
	 * 		Should the click be a shift click
	 * @param delay
	 * 		Desired delay
	 */
	public InvenClick(int slot, boolean shift, long delay) {
		this.inventorySlot = slot;
		this.shiftClick = shift;
		this.delay = delay;
	}
	
	/**
	 * Instantiates a new InvenClick with little-to-no
	 * delay.
	 * 
	 * @param slot
	 * 		The Inventory slot to click
	 * @param shift
	 * 		Should the click be a shift click
	 */
	public InvenClick(int slot, boolean shift) {
		this(slot, shift, 1);
	}
	
	/**
	 * @return
	 * 		Slot in the inventory to click
	 */
	public int getSlotToClick() {
		return this.inventorySlot;
	}
	
	/**
	 * @return
	 * 		True if should shift click
	 */
	public boolean shouldShiftClick() {
		return this.shiftClick;
	}
	
	/**
	 * @return
	 * 		Delay for the click
	 */
	public long getDelay() {
		return this.delay;
	}
	
}
