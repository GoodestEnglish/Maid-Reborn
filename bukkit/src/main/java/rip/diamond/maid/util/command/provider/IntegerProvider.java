package rip.diamond.maid.util.command.provider;

import rip.diamond.maid.util.command.argument.CommandArg;
import rip.diamond.maid.util.command.exception.CommandExitMessage;
import rip.diamond.maid.util.command.parametric.DrinkProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;

public class IntegerProvider extends DrinkProvider<Integer> {

    public static final IntegerProvider INSTANCE = new IntegerProvider();

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
    public Integer defaultNullValue() {
        return 0;
    }

    @Override
    @Nullable
    public Integer provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        String s = arg.get();
        try {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException ex) {
            throw new CommandExitMessage("Required: Integer, Given: '" + s + "'");
        }
    }

    @Override
    public String argumentDescription() {
        return "integer";
    }

}
