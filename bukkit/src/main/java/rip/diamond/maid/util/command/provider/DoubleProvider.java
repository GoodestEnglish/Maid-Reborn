package rip.diamond.maid.util.command.provider;

import rip.diamond.maid.util.command.argument.CommandArg;
import rip.diamond.maid.util.command.exception.CommandExitMessage;
import rip.diamond.maid.util.command.parametric.DrinkProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;

public class DoubleProvider extends DrinkProvider<Double> {

    public static final DoubleProvider INSTANCE = new DoubleProvider();

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
    public Double defaultNullValue() {
        return 0D;
    }

    @Override
    public Double provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        String s = arg.get();
        try {
            return Double.parseDouble(s);
        }
        catch (NumberFormatException ex) {
            throw new CommandExitMessage("Required: Decimal Number, Given: '" + s + "'");
        }
    }

    @Override
    public String argumentDescription() {
        return "decimal number";
    }
}
