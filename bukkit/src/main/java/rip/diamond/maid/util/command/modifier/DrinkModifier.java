package rip.diamond.maid.util.command.modifier;

import rip.diamond.maid.util.command.command.CommandExecution;
import rip.diamond.maid.util.command.exception.CommandExitMessage;
import rip.diamond.maid.util.command.parametric.CommandParameter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public interface DrinkModifier<T> {

    Optional<T> modify(@Nonnull CommandExecution execution, @Nonnull CommandParameter commandParameter, @Nullable T argument) throws CommandExitMessage;

}
