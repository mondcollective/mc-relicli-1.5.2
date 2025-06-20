package net.minecraft.reliant.module.modules;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.Priority;
import net.minecraft.reliant.event.events.EventCommand;
import net.minecraft.reliant.event.events.EventPostUpdate;
import net.minecraft.reliant.event.events.EventStartup;
import net.minecraft.reliant.event.events.EventUpdate;
import net.minecraft.reliant.module.InvenClick;
import net.minecraft.reliant.module.Module;
import net.minecraft.reliant.module.Option;
import net.minecraft.reliant.module.PlayerAngle;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityBoat;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPigZombie;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.EntityTameable;
import net.minecraft.src.ItemStack;

public class ModuleKillAura extends Module {

	private float curYaw;
	private float curPitch;
	private Entity currentTarget = null;
	
	public ModuleKillAura() {
		super("Kill Aura", "aura", Keyboard.KEY_K, false);
	}

	@Override
	public void onStartup() {
		this.addOption(new Option("lock_view", true));
		this.addOption(new Option("players", true));
		this.addOption(new Option("hostile_mobs", true));
		this.addOption(new Option("neutral_mobs", true));
		this.addOption(new Option("passive_animals", true));
		this.addOption(new Option("tame_animals", false));
		this.addOption(new Option("vehicles", false));
		this.addOption(new Option("kill_delay", 5, new Integer[] {
				0, 20
		}));
		this.addOption(new Option("kill_range", 4f, new Float[] {
				2.0f, 8.0f
		}));
	}
	
	@Marker
	public void onCommand(EventCommand eventCommand) {
		if (eventCommand.message.equalsIgnoreCase("lockview")
				|| eventCommand.message.equalsIgnoreCase("lv")) {
			this.getOption("lock_view").toggleBoolean();
			Reliant.getInstance().printMessage("Lock view toggled \2472" 
					+ (this.getOption("lock_view").getBoolean() ? "on" : "off") 
					+ "\247f.");
			eventCommand.setValid(true);
		} else if (eventCommand.getArgs()[0].equalsIgnoreCase("killdelay")
				|| eventCommand.getArgs()[0].equalsIgnoreCase("kd")) {
			try {
				int newDelay = Integer.parseInt(eventCommand.getArgs()[1]);
				this.getOption("kill_delay").setValue(newDelay);
				Reliant.getInstance().printMessage("Kill Delay: \247a" 
						+ this.getOption("kill_delay").getInteger() 
						+ "\247f: \2473[" + this.getOption("kill_delay").getMinAndMax()[0]
						+ " .. "  + this.getOption("kill_delay").getMinAndMax()[1] + "]");
				Reliant.getInstance().printMessage("Kill Delay updated \2472successfully\247f.");
				eventCommand.setValid(true);
			} catch (Exception exception) {
				exception.printStackTrace();
				Reliant.getInstance().printMessage("Kill Delay: \247a" 
						+ this.getOption("kill_delay").getInteger() 
						+ "\247f: \2473[" + this.getOption("kill_delay").getMinAndMax()[0]
						+ " .. "  + this.getOption("kill_delay").getMinAndMax()[1] + "]");
				eventCommand.setValid(true);
			}
		} else if (eventCommand.getArgs()[0].equalsIgnoreCase("killrange")
				|| eventCommand.getArgs()[0].equalsIgnoreCase("kr")) {
			try {
				float newRange = Float.parseFloat(eventCommand.getArgs()[1]);
				this.getOption("kill_range").setValue(newRange);
				float formattedValue = Reliant.getInstance().getMiscUtils().formatFloat(1, this.getOption("kill_range").getFloat());
				float formattedMin = Reliant.getInstance().getMiscUtils().formatFloat(1, (Float) this.getOption("kill_range").getMinAndMax()[0]);
				float formattedMax = Reliant.getInstance().getMiscUtils().formatFloat(1, (Float) this.getOption("kill_range").getMinAndMax()[1]);
				Reliant.getInstance().printMessage("Kill Range: \247a" 
						+ formattedValue
						+ "\247f: \2473[" + formattedMin
						+ " .. "  + formattedMax + "]");
				Reliant.getInstance().printMessage("Kill Range updated \2472successfully\247f.");
				eventCommand.setValid(true);
			} catch (Exception exception) {
				exception.printStackTrace();
				float formattedValue = Reliant.getInstance().getMiscUtils().formatFloat(1, this.getOption("kill_range").getFloat());
				float formattedMin = Reliant.getInstance().getMiscUtils().formatFloat(1, (Float) this.getOption("kill_range").getMinAndMax()[0]);
				float formattedMax = Reliant.getInstance().getMiscUtils().formatFloat(1, (Float) this.getOption("kill_range").getMinAndMax()[1]);
				Reliant.getInstance().printMessage("Kill Range: \247a" 
						+ formattedValue
						+ "\247f: \2473[" + formattedMin
						+ " .. "  + formattedMax + "]");
				eventCommand.setValid(true);
			}
		}
	}
	
	@Override
	public void onDisable() {
		this.currentTarget = null;
	}
	
	@Marker
	public void onUpdate(EventUpdate eventUpdate) {
		if (this.getState()) {
			this.curYaw = this.getPlayer().rotationYaw;
			this.curPitch = this.getPlayer().rotationPitch;
			if (this.currentTarget instanceof EntityLiving) {
				EntityLiving curLivingTarget = (EntityLiving) this.currentTarget;
				if (curLivingTarget.deathTime > 0) {
					this.currentTarget = null;
				}
			}
			if (this.currentTarget == null || this.currentTarget.isDead
					|| !this.getPlayer().canEntityBeSeen(this.currentTarget)
					|| !this.shouldTarget(this.currentTarget)
					|| this.getPlayer().getDistanceToEntity(this.currentTarget)
					> this.getOption("kill_range").getFloat()) {
				this.currentTarget = this.getClosest();
			}
			if (this.currentTarget != null) {
				float[] angles = this.getRotationHelper().getPerfectAngles(this.currentTarget);
				PlayerAngle playerAngle;
				if (this.getRotationHelper().isAiming(angles[0], angles[1])) {
					playerAngle = new PlayerAngle(angles[0], angles[1], Priority.MEDIUM);
				} else {
					playerAngle = new PlayerAngle(angles[0], angles[1], 57f, Priority.MEDIUM);
				}
				this.getRotationHelper().addToQueue(playerAngle);
				if (this.getRotationHelper().isAiming(angles[0], angles[1])
						&& this.isDelayed(this.getOption("kill_delay").getInteger() * 50L)) {
					this.autoSword(this.currentTarget);
					this.getPlayer().swingItem();
					Reliant.getInstance().getMinecraft().playerController.attackEntity(this.getPlayer(), this.currentTarget);
					this.setLastTickNow();
				}
			}
		}
	}
	
	@Marker(eventPriority = Priority.LOW)
	public void postUpdate(EventPostUpdate eventPostUpdate) {
		if (this.getState() && !this.getOption("lock_view").getBoolean()) {
			this.getRotationHelper().setYaw(this.curYaw);
			this.getRotationHelper().setPitch(this.curPitch);
		}
	}
	
	/**
	 * @return
	 * 		Closest Entity to the Player, after sorting them
	 * 		based on the Module's Options.
	 */
	private Entity getClosest() {
		float range = this.getOption("kill_range").getFloat() + 0.05f;
		Entity closest = null;
		for (Entity entity : (List<Entity>) this.getWorld().loadedEntityList) {
			if (!this.shouldTarget(entity)) {
				continue;
			}
			float distance = this.getPlayer().getDistanceToEntity(entity);
			if (distance < range) {
				closest = entity;
				range = distance;
			}
		}
		return closest;
	}

	/**
	 * Checks if an Entity meets the requirements for
	 * targeting (complies with the enabled / disabled
	 * Options).
	 * 
	 * @param entity
	 * 		The Entity to check
	 * @return
	 * 		True if the Entity can be targeted
	 */
	public boolean shouldTarget(Entity entity) {
		if (!entity.isEntityAlive()) {
			return false;
		}
		if (!this.getPlayer().canEntityBeSeen(entity)) {
			return false;
		}
		if (!(entity instanceof EntityLiving)) {
			boolean isVehicle = entity instanceof EntityMinecart || entity instanceof EntityBoat;
			if (isVehicle && !this.getOption("vehicles").getBoolean()) {
				return false;
			}
			if (!isVehicle) {
				return false;
			}
		}
		if (entity instanceof EntityPlayer) {
			if (entity.equals(this.getPlayer())
					|| entity instanceof EntityClientPlayerMP) {
				return false;
			}
			EntityPlayer entityPlayer = (EntityPlayer) entity;
			String username2 = Reliant.getInstance().relFont.stripControlCodes(entityPlayer.username);
			for (String nameArray[] : Reliant.getInstance().getNameProtect().getNameList()) {
				username2 = username2.replaceAll("(?i)" + nameArray[0], nameArray[1]);
			}
			if (Reliant.getInstance().getNameProtect().containsName(
					Reliant.getInstance().relFont.stripControlCodes(entityPlayer.username))
					|| !username2.equalsIgnoreCase(
							Reliant.getInstance().relFont.stripControlCodes(entityPlayer.username))) {
				return false;
			}
			if (!this.getOption("players").getBoolean()) {
				return false;
			}
		}
		if (entity instanceof EntityMob) {
			EntityMob entityMob = (EntityMob) entity;
			if (entityMob instanceof EntitySpider) {
				if(((EntitySpider) entityMob).getBrightness(1f) >= 0.5f
						&& !this.getOption("neutral_mobs").getBoolean()) {
					return false;
				}
			} else if (entityMob instanceof EntityPigZombie) {
				EntityPigZombie pigZombie = (EntityPigZombie) entityMob;
				if (pigZombie.angerLevel == 0
						&& !this.getOption("neutral_mobs").getBoolean()) {
					return false;
				}
			} else if (!this.getOption("hostile_mobs").getBoolean()) {
				return false;
			}
		}
		if (entity instanceof EntityTameable) {
			EntityTameable tameable = (EntityTameable) entity;
			if (tameable.isTamed() && !this.getOption("tame_animals").getBoolean()) {
				return false;
			}
		}
		if (entity instanceof EntityAnimal && !this.getOption("passive_animals").getBoolean()) {
			if (entity instanceof EntityTameable) {
				if (!((EntityTameable) entity).isTamed()) {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks if an Entity meets the requirements for
	 * targeting (complies with the enabled / disabled
	 * Options), except for Friends. Used in ESP.
	 * 
	 * @param entity
	 * 		The Entity to check
	 * @return
	 * 		True if the Entity can be targeted
	 */
	public boolean shouldTarget2(Entity entity) {
		if (!entity.isEntityAlive()) {
			return false;
		}
		if (entity instanceof EntityPlayer) {
			if (entity.equals(this.getPlayer())
					|| entity instanceof EntityClientPlayerMP) {
				return false;
			}
			EntityPlayer entityPlayer = (EntityPlayer) entity;
			boolean friend = false;
			String username2 = Reliant.getInstance().relFont.stripControlCodes(entityPlayer.username);
			for (String nameArray[] : Reliant.getInstance().getNameProtect().getNameList()) {
				username2 = username2.replaceAll("(?i)" + nameArray[0], nameArray[1]);
			}
			if (Reliant.getInstance().getNameProtect().containsName(
					Reliant.getInstance().relFont.stripControlCodes(entityPlayer.username))
					|| !username2.equalsIgnoreCase(
							Reliant.getInstance().relFont.stripControlCodes(entityPlayer.username))) {
				friend = true;
			}
			if (!this.getOption("players").getBoolean() && !friend) {
				return false;
			}
		}
		if (entity instanceof EntityMob) {
			EntityMob entityMob = (EntityMob) entity;
			if (entityMob instanceof EntitySpider) {
				if(((EntitySpider) entityMob).getBrightness(1f) >= 0.5f
						&& !this.getOption("neutral_mobs").getBoolean()) {
					return false;
				}
			} else if (entityMob instanceof EntityPigZombie) {
				EntityPigZombie pigZombie = (EntityPigZombie) entityMob;
				if (pigZombie.angerLevel == 0
						&& !this.getOption("neutral_mobs").getBoolean()) {
					return false;
				}
			} else if (!this.getOption("hostile_mobs").getBoolean()) {
				return false;
			}
		}
		if (entity instanceof EntityTameable) {
			EntityTameable tameable = (EntityTameable) entity;
			if (tameable.isTamed() && !this.getOption("tame_animals").getBoolean()) {
				return false;
			}
		}
		if (entity instanceof EntityAnimal && !this.getOption("passive_animals").getBoolean()) {
			if (entity instanceof EntityTameable) {
				if (!((EntityTameable) entity).isTamed()) {
					return false;
				}
			} else {
				return false;
			}
		}
		if (!(entity instanceof EntityLiving)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Automatically switches to the item in the hotbar
	 * that will do the most damage to an Entity.
	 * 
	 * @param entity
	 * 		The Entity to compare damage to
	 */
	private void autoSword(Entity entity) {
		int curItem = this.getPlayer().inventory.currentItem;
		float baseDamage = 1f;
		for (int slot = 36; slot < 45; slot++) {
			ItemStack itemStack = this.getPlayer().inventoryContainer.getSlot(slot).getStack();
			if (itemStack == null) {
				continue;
			}
			if (itemStack.getDamageVsEntity(entity) > baseDamage) {
				curItem = slot - 36;
				baseDamage = itemStack.getDamageVsEntity(entity);
			}
			this.getPlayer().inventory.currentItem = curItem;
		}
	}
	
	@Override
	public int getColor() {
		return 0xFFE21418;
	}

	@Override
	public boolean canRebind() {
		return true;
	}

}
