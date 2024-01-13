package rip.diamond.maid.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rip.diamond.maid.MaidPermission;

@RequiredArgsConstructor
public enum Alert {

    SERVER_STARTED(AlertType.SERVER, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 伺服器已啟動, 並可讓玩家進入!"),

    PERMISSION_UPDATE(AlertType.UPDATE, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.STEEL_BLUE + "你的權限已更新 " + CC.GRAY + "%s"),

    DISGUISED(AlertType.DISGUISE, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 在 " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 暱稱成為了 " + CC.GRAY + "%s"),

    WARN(AlertType.PUNISHMENT, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 已被 " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 警告"),
    KICK(AlertType.PUNISHMENT, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 已被 " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 踢除"),
    MUTE(AlertType.PUNISHMENT, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 已被 " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 禁言 " + CC.GRAY + "%s"),
    BAN(AlertType.PUNISHMENT, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 已被 " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 封鎖帳號 " + CC.GRAY + "%s"),
    IP_BAN(AlertType.PUNISHMENT, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 已被 " + CC.GRAY + "%s" + CC.STEEL_BLUE + " IP封鎖帳號 " + CC.GRAY + "%s"),
    UNMUTE(AlertType.PUNISHMENT, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 已解除對 " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 的禁言狀態"),
    UNBAN(AlertType.PUNISHMENT, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 已解除對 " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 的封鎖狀態"),
    LOGIN_FAILED_BANNED(AlertType.PUNISHMENT, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 嘗試進入伺服器, 但他已被封鎖帳號 " + CC.GRAY + "%s"),
    CHAT_FAILED_MUTED(AlertType.PUNISHMENT, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 嘗試說話, 但他已被禁言 " + CC.GRAY + "%s"),
    ;

    @Getter private final AlertType type;
    private final String message;

    public String get(String... args) {
        return String.format(message, (Object[]) args);
    }

    @Getter
    @RequiredArgsConstructor
    public enum AlertType {

        SERVER(MaidPermission.ALERT_SERVER),
        UPDATE(MaidPermission.ALERT_UPDATE),
        DISGUISE(MaidPermission.ALERT_DISGUISE),
        PUNISHMENT(MaidPermission.ALERT_PUNISHMENT),
        ;

        private final String permission;

    }
}
