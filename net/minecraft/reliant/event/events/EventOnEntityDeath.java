package net.minecraft.reliant.event.events;

import net.minecraft.reliant.event.Event;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityLiving;

public class EventOnEntityDeath extends Event {

	private EntityLiving deadEntity;
	private DamageSource damageSource;
	
	public EventOnEntityDeath(EntityLiving entityLiving, DamageSource damageSource) {
		this.deadEntity = entityLiving;
		this.damageSource = damageSource;
	}
	
	public EntityLiving getDeadEntity() {
		return this.deadEntity;
	}
	
	public DamageSource getSource() {
		return this.damageSource;
	}
	
}
