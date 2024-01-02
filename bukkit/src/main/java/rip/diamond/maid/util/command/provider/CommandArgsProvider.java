package rip.diamond.maid.util.command.provider;

import rip.diamond.maid.util.command.argument.CommandArg;
import rip.diamond.maid.util.command.argument.CommandArgs;
import rip.diamond.maid.util.command.exception.CommandExitMessage;
import rip.diamond.maid.util.command.parametric.DrinkProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;

public class CommandArgsProvider extends DrinkProvider<CommandArgs> {

    public static final CommandArgsProvider INSTANCE = new CommandArgsProvider();

    @Override
    public boolean doesConsumeArgument() {
        return false;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Nullable
    @Override
    public CommandArgs provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        return arg.getArgs();
    }

    @Override
    public String argumentDescription() {
        return "args";
    }

}
