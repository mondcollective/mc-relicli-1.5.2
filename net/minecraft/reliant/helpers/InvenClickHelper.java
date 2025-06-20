package net.minecraft.reliant.helpers;

import java.util.ArrayList;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.event.Listener;
import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.Priority;
import net.minecraft.reliant.event.events.EventUpdate;
import net.minecraft.reliant.module.InvenClick;
import net.minecraft.src.ItemStack;

/**
 * InvenClickHelper is a Helper class for assisting the
 * client in making clicks in the inventory of the 
 * Player. It has a delaying function for bypassing 
 * anti-hacking measures, too.
 * 
 * @author Nahr
 */
public class InvenClickHelper implements Listener {

	private static volatile InvenClickHelper instance;
	private ArrayList<InvenClick> clickQueue = new ArrayList<InvenClick>();
	private InvenClick currentClick;
	private long lastClicked = 0;
	
	private InvenClickHelper() {
		Reliant.getInstance().getEventManager().addListener(this);
	}
	
	public static InvenClickHelper getInstance() {
		InvenClickHelper result = instance;
		if (result == null) {
			synchronized (InvenClickHelper.class) {
				result = instance;
				if (result == null) {
					result = new InvenClickHelper();
					instance = result;
				}
			}
		}
		return result;
	}
	
	/**
	 * Iterates through the queue of InvenClick's and
	 * selects the first one added. Then it does the
	 * click, sets the new delay, and selects a new
	 * InvenClick.
	 * 
	 * @param eventUpdate
	 * 		An Event parameter
	 */
	@Marker(eventPriority = Priority.HIGH)
	public void onUpdate(EventUpdate eventUpdate) {
		if (this.clickQueue.size() > 0 && this.currentClick == null) {
			for (InvenClick invenClick : this.clickQueue) {
				this.currentClick = invenClick;
			}
		}
		if (this.currentClick != null && this.isDelayed(currentClick)) {
			Reliant.getInstance().getMinecraft().playerController.windowClick(0 /*Inventory*/, 
					this.currentClick.getSlotToClick(), 0, this.currentClick.shouldShiftClick() ? 1 : 0, 
					Reliant.getInstance().getPlayer());
			this.setLastClick();
			return;
		}
	}
	
	/**
	 * Adds an InvenClick to the queue.
	 * 
	 * @param invenClick
	 * 		The InvenClick to add
	 */
	public void addToQueue(InvenClick invenClick) {
		this.clickQueue.add(invenClick);
	}
	
	/**
	 * Checks if enough time has passed for the 
	 * specified InvenClick's delay.
	 * 
	 * @param invenClick
	 * 		The InvenClick to check
	 * @return
	 * 		True if the InvenClick's delay has passed
	 */
	private boolean isDelayed(InvenClick invenClick) {
		return Reliant.getInstance().getSystemMillis() - this.lastClicked >= invenClick.getDelay();
	}
	
	/**
	 * Sets the time for <code>lastClicked</code> so
	 * delays can be reset. Also sets the 
	 * <code>currentClick</code> to null.
	 */
	private void setLastClick() {
		this.lastClicked = Reliant.getInstance().getSystemMillis();
		int index = this.clickQueue.indexOf(this.currentClick);
		this.currentClick = null;
		this.clickQueue.remove(index);
	}
	
	/**
	 * @param noneSlot
	 * 		If no open slot is found, return this slot
	 * @return
	 * 		First open slot in the hotbar. If there is
	 * 		none, returns the specified slot.
	 */
	public int getFirstOpenHotbarSlot(int noneSlot) {
		for (int slot = 36; slot < 44; slot++) {
			ItemStack itemStack = Reliant.getInstance().getPlayer().inventoryContainer.getSlot(slot).getStack();
			if (itemStack == null) {
				return slot;
			}
		}
		return noneSlot;
	}
	
	/**
	 * @return
	 * 		ArrayList of InvenClicks that are queued
	 */
	public ArrayList<InvenClick> getClickQueue() {
		return this.clickQueue;
	}
	
}
