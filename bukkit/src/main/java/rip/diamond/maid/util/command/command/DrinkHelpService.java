package rip.diamond.maid.util.command.command;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import rip.diamond.maid.util.Common;

@Getter
@Setter
public class DrinkHelpService {

    private final DrinkCommandService commandService;
    private HelpFormatter helpFormatter;

    public DrinkHelpService(DrinkCommandService commandService) {
        this.commandService = commandService;
        this.helpFormatter = (sender, container) -> {
            sender.sendMessage(Common.text("<aqua>" + container.getName() + " 指令幫助"));
            for (DrinkCommand c : container.getCommands().values()) {
                sender.sendMessage(Common.text("<aqua><bold> - <!bold><white>/" + container.getName() + " " + c.getName() + " <gray>- <white>" + c.getDescription()));
            }
        };
    }

    public void sendHelpFor(CommandSender sender, DrinkCommandContainer container) {
        this.helpFormatter.sendHelpFor(sender, container);
    }

    public void sendUsageMessage(CommandSender sender, DrinkCommandContainer container, DrinkCommand command) {
        sender.sendMessage(getUsageMessage(container, command));
    }

    public Component getUsageMessage(DrinkCommandContainer container, DrinkCommand command) {
        String usage = "<red>指令用法: /" + container.getName() + " ";
        if (command.getName().length() > 0) {
            usage += command.getName() + " ";
        }
        if (command.getUsage() != null && command.getUsage().length() > 0) {
            usage += command.getUsage();
        } else {
            usage += command.getGeneratedUsage();
        }
        return Common.text(usage);
    }

}
