package net.minecraft.reliant.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.lwjgl.input.Keyboard;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.module.Module;

public class Binds extends IO {

	private static volatile Binds instance;
	private File keyBindFile;
	
	private Binds() {
		this.keyBindFile = new File(Reliant.getInstance().getRelFolder().getReliantFolder() + "/binds.txt");
	}
	
	public static Binds getInstance() {
		Binds result = instance;
		if (result == null) {
			synchronized (Binds.class) {
				result = instance;
				if (result == null) {
					result = new Binds();
					instance = result;
				}
			}
		}
		return result;
	}
	
	@Override
	public void checkForFile() {
		try {
			if (!this.keyBindFile.exists()) {
				System.out.println("Could not find Binds file. Attempting to create.");
				this.saveFile();
				return;
			} 
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(this.keyBindFile));
				String readLine = "";
				while ((readLine = reader.readLine()) != null) {
					String[] split = readLine.split(this.splitString);
					Module module = Reliant.getInstance().getModuleHelper().getModuleByName(split[0]);
					if (module != null) {
            			int keyBind = Keyboard.getKeyIndex(split[1].toUpperCase());
            			module.setKeybind(keyBind);
            			System.out.println("Module \"" + module.getDisplayName() + "\" bound to key " + split[1].toUpperCase() + " (Key " + keyBind + ").");
					}
				}
				reader.close();
			} catch (Exception exception) {
				exception.printStackTrace();
				System.out.println("Error when loading a keybind for a Module.");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println("Could not find or load from Binds file.");
		}
	}

	@Override
	public void saveFile() {
		try {
			if (!this.keyBindFile.exists()) {
				if (this.keyBindFile.createNewFile()) {
					System.out.println("Created Keybinds file.");
				} else {
					System.out.println("Could not create Keybinds file.");
					return;
				}
			}
			PrintWriter writer;
			try {
				writer = new PrintWriter(new FileWriter(this.keyBindFile));
				for (Module module : Reliant.getInstance().getModuleHelper().getModules()) {
					if (module.canRebind()) {
						String keyBind = Keyboard.getKeyName(module.getKeyBind());
						writer.println(module.getDisplayName() + this.splitString + keyBind);
						System.out.println("Module \"" + module.getDisplayName() + "\" saved with keybind " + keyBind + " (Key " + module.getKeyBind() + ").");
					}
				}
				writer.close();
			} catch (Exception exception) {
				exception.printStackTrace();
				System.out.println("Error in saving a Module's keybind.");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println("Could not save or create Binds file.");
		}
	}
	
	/**
	 * @return
	 * 		File containing the keybinds.
	 */
	public File getKeyBindFile() {
		return this.keyBindFile;
	}
	
}
