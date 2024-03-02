package rip.diamond.maid.command;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.CommandSender;
import rip.diamond.maid.util.MaidPermission;
import rip.diamond.maid.api.user.IPunishment;
import rip.diamond.maid.player.User;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.UUIDCache;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;
import rip.diamond.maid.util.command.annotation.Text;
import rip.diamond.maid.util.extend.MaidCommand;

import java.util.UUID;

@Require(MaidPermission.UNMUTE)
public class UnmuteCommand extends MaidCommand {

    @Command(name = "", desc = "解除禁言一位玩家")
    public void root(@Sender CommandSender sender, String targetName, @Text String reason) {
        UUIDCache.getUUID(targetName).whenComplete((uuid, throwable) -> {
            if (throwable != null) {
                Common.sendMessage(sender, CC.RED + "執行這個動作時發生了錯誤, 請查看後台請查看後台觀看詳細錯誤 (" + throwable.getMessage() + ")");
                return;
            }

            if (!plugin.getUserManager().hasUser(uuid).join()) {
                Common.sendMessage(sender, CC.RED + "未能找到玩家 '" + targetName + "' 的資料");
                return;
            }

            User targetUser = (User) plugin.getUserManager().getUser(uuid).join();
            if (targetUser.getActivePunishments(ImmutableList.of(IPunishment.PunishmentType.MUTE)).isEmpty()) {
                Common.sendMessage(sender, CC.RED + "該玩家沒有任何活躍的禁言懲罰紀錄");
                return;
            }

            plugin.getPunishmentManager().unpunish(sender, uuid, IPunishment.PunishmentType.MUTE, reason);
        });


    }

}
