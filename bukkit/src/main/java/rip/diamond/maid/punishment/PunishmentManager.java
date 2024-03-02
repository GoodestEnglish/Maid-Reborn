package rip.diamond.maid.punishment;

import com.google.common.collect.ImmutableList;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import org.bson.Document;
import org.bukkit.entity.Player;
import rip.diamond.maid.IMaidAPI;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IPunishment;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.mongo.MongoManager;
import rip.diamond.maid.player.User;
import rip.diamond.maid.player.UserManager;
import rip.diamond.maid.redis.packets.bukkit.BroadcastPacket;
import rip.diamond.maid.redis.packets.bukkit.PunishmentExecutePacket;
import rip.diamond.maid.redis.packets.bukkit.PunishmentUpdatePacket;
import rip.diamond.maid.util.*;
import rip.diamond.maid.util.json.GsonProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class PunishmentManager {

    private final IMaidAPI api;
    private final MongoManager mongoManager;
    private final UserManager userManager;

    @Getter private final List<IPunishment> punishments = new ArrayList<>();

    public PunishmentManager(IMaidAPI api, MongoManager mongoManager, UserManager userManager) {
        this.api = api;
        this.mongoManager = mongoManager;
        this.userManager = userManager;

        loadPunishments();
    }

    private void loadPunishments() {
        for (Document document : mongoManager.getPunishments().find()) {
            IPunishment punishment = Punishment.of(document);
            punishments.add(punishment);
        }

        // TODO: 5/1/2024 Possibly sort punishments to make get method faster?
    }

    public void updatePunishment(IPunishment punishment) {
        punishments.removeIf(punishment_ -> punishment_.getUniqueID().equals(punishment.getUniqueID()));
        punishments.add(punishment);

        Tasks.runAsync(() -> {
            mongoManager.getPunishments().replaceOne(Filters.eq("_id", punishment.getUniqueID().toString()), Document.parse(GsonProvider.GSON.toJson(punishment)), new ReplaceOptions().upsert(true));
            api.getPacketHandler().send(new PunishmentUpdatePacket(Maid.API.getPlatform().getServerID(), (Punishment) punishment));
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
        throw new NoSuchElementException("Cannot find punishment message with type " + punishment.getType().name());
    }

    /**
     * Punish the player
     *
     * @param executor The player who executed the command
     * @param targetUUID The UUID of the target who is getting punished
     * @param duration The duration of this punishment
     * @param reason The reason why this punishment is created
     */
    public void punish(Audience executor, UUID targetUUID, IPunishment.PunishmentType type, String duration, String reason) {
        IUser user = executor instanceof Player player ? userManager.getUserNow(player.getUniqueId()) : User.CONSOLE;

        userManager.getUser(targetUUID).whenComplete((target, throwable) -> {
            if (throwable != null) {
                Common.sendMessage(executor, CC.RED + "執行這個動作時發生了錯誤, 請查看後台請查看後台觀看詳細錯誤 (" + throwable.getMessage() + ")");
                return;
            }
            String serverID = Maid.API.getPlatform().getServerID();
            long duration_ = TimeUtil.getDuration(duration);

            //Build the punishment and save it to the database
            Punishment punishment = new Punishment(target, type, user, reason, System.currentTimeMillis(), duration_);
            updatePunishment(punishment);
            api.getPacketHandler().send(new PunishmentExecutePacket(serverID, punishment));

            //Broadcast to all staff members
            Alert alert = Alert.valueOf(type.name());
            String durationReadable = duration_ == -1 ? "永久" : TimeUtil.formatDuration(punishment.getIssuedAt() + punishment.getDuration() - System.currentTimeMillis());
            api.getPacketHandler().send(new BroadcastPacket(serverID, alert.getType().getPermission(), ImmutableList.of(alert.get(target.getSimpleDisplayName(false), user.getSimpleDisplayName(false), "(" + durationReadable + ")"))));
            Common.sendMessage(executor, CC.GREEN + "成功" + type.getName() + "玩家 " + CC.AQUA + target.getSimpleDisplayName(false));
        });
    }

    /**
     * Un-punish the player
     *
     * @param executor The player who executed the command
     * @param targetUUID The UUID of the target who is getting revoked
     * @param reason The reason why this punishment is revoked
     */
    public void unpunish(Audience executor, UUID targetUUID, IPunishment.PunishmentType type, String reason) {
        IUser user = executor instanceof Player player ? userManager.getUserNow(player.getUniqueId()) : User.CONSOLE;

        userManager.getUser(targetUUID).whenComplete((target, throwable) -> {
            if (throwable != null) {
                Common.sendMessage(executor, CC.RED + "執行這個動作時發生了錯誤, 請查看後台請查看後台觀看詳細錯誤 (" + throwable.getMessage() + ")");
                return;
            }
            String serverID = Maid.API.getPlatform().getServerID();

            //If the PunishmentType is BAN or IP_BAN, we will get both BAN and IP_BAN data because they are all "bans"
            List<IPunishment> punishments = type == IPunishment.PunishmentType.BAN || type == IPunishment.PunishmentType.IP_BAN ? target.getActivePunishments(ImmutableList.of(IPunishment.PunishmentType.BAN, IPunishment.PunishmentType.IP_BAN)) : target.getActivePunishments(ImmutableList.of(type));
            punishments.forEach(punishment -> {
                punishment.revoke(user, reason);
                updatePunishment(punishment);
            });

            Alert alert;
            switch (type) {
                case MUTE -> alert = Alert.UNMUTE;
                case BAN, IP_BAN -> alert = Alert.UNBAN;
                default -> throw new NoSuchElementException("Cannot find punishment type " + type.name());
            }
            api.getPacketHandler().send(new BroadcastPacket(serverID, alert.getType().getPermission(), ImmutableList.of(alert.get(user.getSimpleDisplayName(false), target.getSimpleDisplayName(false)))));
            Common.sendMessage(executor, CC.GREEN + "成功解除" + type.getName() + "玩家 " + CC.AQUA + target.getSimpleDisplayName(false));
        });
    }

    /**
     * Un-punish the player, with selected punishment provided
     *
     * @param executor The player who executed the command
     * @param punishment The punishment to revoke
     * @param reason The reason why this punishment is revoked
     */
    public void unpunish(Audience executor, IPunishment punishment, String reason) {
        IUser user = executor instanceof Player player ? userManager.getUserNow(player.getUniqueId()) : User.CONSOLE;

        userManager.getUser(punishment.getUser()).whenComplete((target, throwable) -> {
            if (throwable != null) {
                Common.sendMessage(executor, CC.RED + "執行這個動作時發生了錯誤, 請查看後台請查看後台觀看詳細錯誤 (" + throwable.getMessage() + ")");
                return;
            }
            String serverID = Maid.API.getPlatform().getServerID();

            punishment.revoke(user, reason);
            updatePunishment(punishment);

            Alert alert;
            switch (punishment.getType()) {
                case MUTE -> alert = Alert.UNMUTE;
                case BAN, IP_BAN -> alert = Alert.UNBAN;
                default -> throw new NoSuchElementException("Cannot find punishment type " + punishment.getType().name());
            }
            api.getPacketHandler().send(new BroadcastPacket(serverID, alert.getType().getPermission(), ImmutableList.of(alert.get(user.getSimpleDisplayName(false), target.getSimpleDisplayName(false)))));
            Common.sendMessage(executor, CC.GREEN + "成功解除" + punishment.getType().getName() + "玩家 " + CC.AQUA + target.getSimpleDisplayName(false));
        });
    }

}
