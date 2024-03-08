package rip.diamond.maid.util.command.provider.spigot;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rip.diamond.maid.util.command.argument.CommandArg;
import rip.diamond.maid.util.command.exception.CommandExitMessage;
import rip.diamond.maid.util.command.parametric.DrinkProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public class LocationProvider extends DrinkProvider<Location> {

    public static final LocationProvider INSTANCE = new LocationProvider();

    @Override
    public boolean doesConsumeArgument() {
        return true;
    }

    @Override
    public boolean allowNullArgument() {
        return false;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Nullable
    @Override
    public Location provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        try {
            String worldName = arg.get();
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                throw new CommandExitMessage("Cannot find world '" + worldName + "'");
            }
            double x = Double.parseDouble(arg.getArgs().next());
            double y = Double.parseDouble(arg.getArgs().next());
            double z = Double.parseDouble(arg.getArgs().next());
            float yaw = Float.parseFloat(arg.getArgs().next());
            float pitch = Float.parseFloat(arg.getArgs().next());

            return new Location(world, x, y, z, yaw, pitch);
        } catch (NumberFormatException e) {
            throw new CommandExitMessage("Invalid location format");
        }
    }

    @Override
    public String argumentDescription() {
        return "location";
    }

    @Override
    public List<String> getSuggestions(CommandSender sender, @NotNull String prefix) {
        if (sender instanceof Player player) {
            Location location = player.getLocation();
            return Collections.singletonList(location.getWorld().getName() + " " + location.x() + " " + location.y() + " " + location.z() + " " + location.getYaw() + " " + location.getPitch());
        }
        return Collections.emptyList();
    }
}
