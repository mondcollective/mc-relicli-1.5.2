package net.minecraft.reliant.module.commands;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.events.EventCommand;
import net.minecraft.reliant.module.Command;

public class CommandDel extends Command {

	public CommandDel() {
		super(new String[] {
				"del"
		});
	}

	@Override
	@Marker
	public void onCommand(EventCommand eventCommand) {
		String[] args;
		try {
			args = eventCommand.getArgs();
			if (this.equalsCommand(args[0])) {
				try {
					String orig = args[1];
					if (Reliant.getInstance().getNameProtect().delName(orig)) {
						this.print("\"" + orig + "\" deleted.");
					} else {
						this.print("\"" + orig + "\" doesn't exist.");
					}
					Reliant.getInstance().getNameProtect().saveFile();
				} catch (Exception exception) {
					exception.printStackTrace();
					this.print(this.returnHelp());
				}
				eventCommand.setValid(true);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public String returnHelp() {
		return "Usage: .del [original]";
	}

}
