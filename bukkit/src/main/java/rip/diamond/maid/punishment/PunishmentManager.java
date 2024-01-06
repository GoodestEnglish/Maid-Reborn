package rip.diamond.maid.punishment;

import com.google.common.collect.ImmutableList;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import org.bson.Document;
import org.bukkit.entity.Player;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.user.IPunishment;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.player.User;
import rip.diamond.maid.redis.messaging.PacketHandler;
import rip.diamond.maid.redis.packets.bukkit.BroadcastPacket;
import rip.diamond.maid.redis.packets.bukkit.PunishmentExecutePacket;
import rip.diamond.maid.redis.packets.bukkit.PunishmentUpdatePacket;
import rip.diamond.maid.util.*;
import rip.diamond.maid.util.command.annotation.Sender;
import rip.diamond.maid.util.command.annotation.Text;
import rip.diamond.maid.util.extend.MaidManager;
import rip.diamond.maid.util.json.GsonProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PunishmentManager extends MaidManager {

    @Getter private final List<IPunishment> punishments = new ArrayList<>();

    public PunishmentManager() {
        loadPunishments();
    }

    private void loadPunishments() {
        for (Document document : plugin.getMongoManager().getPunishments().find()) {
            IPunishment punishment = Punishment.of(document);
            punishments.add(punishment);
        }

        // TODO: 5/1/2024 Possibly sort punishments to make get method faster?
    }

    public void updatePunishment(IPunishment punishment) {
        punishments.removeIf(punishment_ -> punishment_.getUniqueID().equals(punishment.getUniqueID()));
        punishments.add(punishment);

        Tasks.runAsync(() -> {
            plugin.getMongoManager().getPunishments().replaceOne(Filters.eq("_id", punishment.getUniqueID().toString()), Document.parse(GsonProvider.GSON.toJson(punishment)), new ReplaceOptions().upsert(true));
            PacketHandler.send(new PunishmentUpdatePacket(MaidAPI.INSTANCE.getPlatform().getServerID(), (Punishment) punishment));
        });
    }

    public List<String> getPunishmentMessage(IPunishment punishment) {
        long duration = punishment.getDuration();
        switch (punishment.getType()) {
            case WARN -> {
                return ImmutableList.of(
                        "",
                        CC.RED + "你已因為 " + CC.YELLOW + punishment.getReason() + CC.RED + " 而被警告",
                        CC.RED + "請仔細閱讀我們的規則, 屢勸不聽將會被封鎖帳號",
                        ""
                );
            }
            case KICK -> {
                return ImmutableList.of(
                        "",
                        CC.RED + "你已因為 " + CC.YELLOW + punishment.getReason() + CC.RED + " 而被踢出伺服器",
                        CC.RED + "請仔細閱讀我們的規則, 屢勸不聽將會被封鎖帳號",
                        ""
                );
            }
            case MUTE -> {
                return ImmutableList.of(
                        "",
                        CC.RED + "你的帳號已被禁言!",
                        "",
                        CC.DARK_GRAY + "原因: " + CC.WHITE + punishment.getReason(),
                        CC.DARK_GRAY + "解除時間: " + CC.WHITE + (duration == -1 ? "永久" : TimeUtil.formatDuration(punishment.getIssuedAt() + punishment.getDuration() - System.currentTimeMillis())),
                        ""
                );
            }
            case BAN, IP_BAN -> {
                return ImmutableList.of(
                        "",
                        CC.RED + "你的帳號已被封鎖!",
                        "",
                        CC.DARK_GRAY + "原因: " + CC.WHITE + punishment.getReason(),
                        CC.DARK_GRAY + "解封時間: " + CC.WHITE + (duration == -1 ? "永久" : TimeUtil.formatDuration(punishment.getIssuedAt() + punishment.getDuration() - System.currentTimeMillis())),
                        ""
                );
            }
        }
        throw new NullPointerException("Cannot find punishment message with type " + punishment.getType().name());
    }

    /**
     * Ban the player
     *
     * @param executor The player who executed the command
     * @param targetUUID The UUID of the target who is getting ban
     * @param duration The duration of this punishment
     * @param reason The reason why this punishment is created
     */
    public void ban(Audience executor, UUID targetUUID, String duration, String reason) {
        IUser user = executor instanceof Player player ? plugin.getUserManager().getUser(player.getUniqueId()).join() : User.CONSOLE;
        IUser target = plugin.getUserManager().getUser(targetUUID).join();
        long duration_ = TimeUtil.getDuration(duration);

        //Build the punishment and save it to the database
        Punishment punishment = new Punishment(target, IPunishment.PunishmentType.BAN, user, reason, System.currentTimeMillis(), duration_);
        updatePunishment(punishment);

        String serverID = MaidAPI.INSTANCE.getPlatform().getServerID();
        Alert alert = Alert.BAN;
        String durationReadable = duration_ == -1 ? "永久" : TimeUtil.formatDuration(punishment.getIssuedAt() + punishment.getDuration() - System.currentTimeMillis());

        //Execute the punishment across all servers and broadcast to all staff members
        PacketHandler.send(new PunishmentExecutePacket(serverID, punishment));
        PacketHandler.send(new BroadcastPacket(serverID, alert.getType().getPermission(), ImmutableList.of(alert.get(target.getSimpleDisplayName(false), user.getSimpleDisplayName(false), "(" + durationReadable + ")"))));

        if (executor instanceof Player player) {
            Common.sendMessage(player, CC.GREEN + "成功封鎖玩家 " + CC.AQUA + target.getSimpleDisplayName(false));
        }
    }

}
