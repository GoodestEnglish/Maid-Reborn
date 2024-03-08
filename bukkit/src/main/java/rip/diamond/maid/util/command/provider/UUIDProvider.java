package rip.diamond.maid.util.command.provider;

import org.bukkit.plugin.Plugin;
import rip.diamond.maid.util.command.argument.CommandArg;
import rip.diamond.maid.util.command.exception.CommandExitMessage;
import rip.diamond.maid.util.command.parametric.DrinkProvider;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.UUID;

public class UUIDProvider extends DrinkProvider<UUID> {

    public static final UUIDProvider INSTANCE = new UUIDProvider();

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

    @Override
    public UUID provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        return UUID.fromString(arg.get());
    }

    @Override
    public String argumentDescription() {
        return "UUID";
    }

}
