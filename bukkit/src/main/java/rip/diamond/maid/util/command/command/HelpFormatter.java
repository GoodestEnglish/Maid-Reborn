package rip.diamond.maid.util.command.command;

import org.bukkit.command.CommandSender;

public interface HelpFormatter {

    void sendHelpFor(CommandSender sender, DrinkCommandContainer container);
}
