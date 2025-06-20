package net.minecraft.reliant.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.module.Module;
import net.minecraft.reliant.module.Option;

public class Values extends IO {

	private File valuesFile;
	private static volatile Values instance;
	
	private Values() {
		this.valuesFile = new File(Reliant.getInstance().getRelFolder().getReliantFolder() + "/values.txt");
	}
	
	public static Values getInstance() {
		Values result = instance;
		if (result == null) {
			synchronized (Values.class) {
				result = instance;
				if (result == null) {
					result = new Values();
					instance = result;
				}
			}
		}
		return result;
	}
	
	@Override
	public void checkForFile() {
		try {
			if (!this.valuesFile.exists()) {
				System.out.println("Could not find values file. Attempting to create.");
				this.saveFile();
				return;
			}
			
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(this.valuesFile));
				String readLine = "";
				while ((readLine = reader.readLine()) != null) {
					String split[] = readLine.split(this.splitString);
					Module module = Reliant.getInstance().getModuleHelper().getModuleByName(split[0]);
					if (module != null) {
						Option option = module.getOption(split[1]);
						if (option != null) {
							switch (option.getType()) {
								case 0: /*Integer*/
									option.setValue(Integer.parseInt(split[2]));
									break;
									
								case 1: /*Float*/
									option.setValue(Float.parseFloat(split[2]));
									break;
									
								case 2: /*Double*/
									option.setValue(Double.parseDouble(split[2]));
									break;	
									
								case 3: /*String*/
									option.setValue(split[2]);
									break;
									
								case 4: /*Boolean*/
									option.setValue(Boolean.parseBoolean(split[2]));
									break;		
									
								default:
									break;
							}
							System.out.println("Loaded value \"" + split[1] + "\" for Module \"" + split[0] + ".\"");
						} else {
							System.out.println("Unrecognized Option \"" + split[1] + ".\"");
						}
					} else {
						System.out.println("Unrecognized Module \"" + split[0] + ".\"");
					}
				}
				reader.close();
			} catch (Exception exception) {
				exception.printStackTrace();
				System.out.println("Error when trying to load a value.");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println("Error when trying to load values.");
		}
	}

	@Override
	public void saveFile() {
		try {
			if (!this.valuesFile.exists()) {
				if (this.valuesFile.createNewFile()) {
					System.out.println("Created values file.");
				} else {
					System.out.println("Could not create values file.");
					return;
				}
			}
			
			PrintWriter writer;
			try {
				writer = new PrintWriter(new FileWriter(this.valuesFile));
				for (Module module : Reliant.getInstance().getModuleHelper().getModules()) {
					String moduleName = module.getShortName();
					for (Option option : module.getOptions()) {
						String optionName = option.getName();
						Object optionValue = option.getValue();
						writer.println(moduleName + this.splitString + optionName + this.splitString + optionValue);
						System.out.println("Saved value for \"" + optionName + "\" from Module \"" + moduleName + ".\"");
					}
				}
				writer.close();
			} catch (Exception exception) {
				exception.printStackTrace();
				System.out.println("Error when trying to save a value.");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println("Error when trying to save values.");
		}
	}

}
