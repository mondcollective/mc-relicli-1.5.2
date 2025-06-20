package net.minecraft.reliant.event.events;

import net.minecraft.reliant.event.EventCancellable;

public class EventChatSend extends EventCancellable {

	private String chatMessage;
	
	public EventChatSend(String chatMessage) {
		this.chatMessage = chatMessage;
	}
	
	public String getChatMessage() {
		return this.chatMessage;
	}
	
}
