package net.minecraft.reliant.module.commands;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.events.EventCommand;
import net.minecraft.reliant.module.Command;

public class CommandAdd extends Command {

	public CommandAdd() {
		super(new String[] {
				"add"
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
					String alias = args[2];
					if (Reliant.getInstance().getNameProtect().addName(orig, alias)) {
						this.print("\"" + orig + "\" added.");
					} else {
						Reliant.getInstance().getNameProtect().replaceName(orig, alias);
						this.print("\"" + orig + "\" replaced.");
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
		return "Usage: .add [original] [modified]";
	}

}
