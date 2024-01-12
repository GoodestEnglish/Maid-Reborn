package rip.diamond.maid.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidPermission;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;

import java.util.Comparator;
import java.util.stream.Collectors;

@Require(MaidPermission.LIST)
public class ListCommand {

    @Command(name = "", desc = "")
    public void root(@Sender Player player) {
        String ranks = Maid.INSTANCE.getRankManager().getRanks().values().stream().sorted(Comparator.comparingInt(IRank::getPriority).reversed()).map(rank -> rank.getDisplayName(true)).collect(Collectors.joining(CC.WHITE + ", "));
        String amount = CC.WHITE + "(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ")";
        String players = Bukkit.getOnlinePlayers().stream()
                .map(p -> Maid.INSTANCE.getUserManager().getUser(p.getUniqueId()).join())
                .sorted(Comparator.comparingInt(obj -> ((IUser) obj).getRank().getPriority()).reversed())
                .map(user -> user.getSimpleDisplayName(true))
                .collect(Collectors.joining(CC.WHITE + ", "));

        Common.sendMessage(player, ranks, amount + ": " + players);
    }

}
