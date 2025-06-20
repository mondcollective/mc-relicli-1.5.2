package net.minecraft.reliant.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * Manager for Events. Fires Events and 
 * adds Listeners. Also checks Methods.
 * 
 * @author Nahr
 */
public class EventManager {

	private static volatile EventManager instance;
	private HashMap<Listener, List<Method>> methodList = new HashMap<Listener, List<Method>>();
	
	public static EventManager getInstance() {
		EventManager result = instance;
		if (result == null) {
			synchronized (EventManager.class) {
				result = instance;
				if (result == null) {
					result = new EventManager();
					instance = result;
				}
			}
		}
		return result;
	}
	
	/**
	 * Registers an Event Listener.
	 * 
	 * @param Listener
	 */
	public void addListener(Listener Listener) {
		ArrayList<Method> methodList = new ArrayList<Method>(); 
		for (Method method : Listener.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent(Marker.class)) {
				methodList.add(method);
			}
		}
		this.methodList.put(Listener, methodList);
	}
	
	public HashMap<Listener, Method> getEventMethods(Event event) {
		HashMap<Listener, Method> localMap = new HashMap<Listener, Method>();
		for (Entry<Listener, List<Method>> entry : this.methodList.entrySet()) {
			for (Method method : entry.getValue()) {
				if (method.getParameterTypes()[0].equals(event.getClass())) {
					localMap.put(entry.getKey(), method);
				}
			}
		}
		return localMap;
	}
	
	/**
	 * Fires an Event.
	 * 
	 * @param event
	 * @return
	 * 		The Event fired. Used for cancellation. 
	 */
	public synchronized Event fireEvent(Event event) {
		for (Priority eventPriority : Priority.values()) {
			for (Entry<Listener, Method> entry : this.getEventMethods(event).entrySet()) {
				if (entry.getValue().getAnnotation(Marker.class).eventPriority().equals(eventPriority)) {	
					try {
						entry.getValue().invoke(entry.getKey(), event);
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			}
		}
		return event;
	}
	
}
