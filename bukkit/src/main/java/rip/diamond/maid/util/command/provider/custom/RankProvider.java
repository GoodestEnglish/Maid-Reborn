package rip.diamond.maid.util.command.provider.custom;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.rank.Rank;
import rip.diamond.maid.util.command.argument.CommandArg;
import rip.diamond.maid.util.command.exception.CommandExitMessage;
import rip.diamond.maid.util.command.parametric.DrinkProvider;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.List;

public class RankProvider extends DrinkProvider<Rank> {

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
    public Rank provide(@NotNull CommandArg arg, @NotNull List<? extends Annotation> annotations) throws CommandExitMessage {
        String name = arg.get();
        IRank rank = Maid.INSTANCE.getRankManager().getRank(name);
        if (rank != null) {
            return (Rank) rank;
        }
        throw new CommandExitMessage("No rank is available with name '" + name + "'.");
    }

    @Override
    public String argumentDescription() {
        return "rank";
    }

    @Override
    public List<String> getSuggestions(@Nonnull String prefix) {
        final String finalPrefix = prefix.toLowerCase();
        return Maid.INSTANCE.getRankManager().getRanks().values().stream().map(rank -> rank.getName().toLowerCase()).filter(s -> finalPrefix.length() == 0 || s.startsWith(finalPrefix)).toList();
    }
}
