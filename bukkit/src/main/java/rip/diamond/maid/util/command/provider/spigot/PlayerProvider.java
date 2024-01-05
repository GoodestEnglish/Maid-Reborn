package rip.diamond.maid.util.command.provider.spigot;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import rip.diamond.maid.util.command.argument.CommandArg;
import rip.diamond.maid.util.command.exception.CommandExitMessage;
import rip.diamond.maid.util.command.parametric.DrinkProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerProvider extends DrinkProvider<Player> {

    private final Plugin plugin;

    public PlayerProvider(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean doesConsumeArgument() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean allowNullArgument() {
        return false;
    }

    @Nullable
    @Override
    public Player defaultNullValue() {
        return null;
    }

    @Nullable
    @Override
    public Player provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        String name = arg.get();
        Player p = plugin.getServer().getPlayer(name);
        if (p != null) {
            return p;
        }
        throw new CommandExitMessage("No player online with name '" + name + "'.");
    }

    @Override
    public String argumentDescription() {
        return "player";
    }

    @Override
    public List<String> getSuggestions(@Nonnull String prefix) {
        return plugin.getServer().getOnlinePlayers().stream().map(Player::getName).filter(s -> prefix.length() == 0 || s.toLowerCase().startsWith(prefix.toLowerCase())).collect(Collectors.toList());
    }
}
