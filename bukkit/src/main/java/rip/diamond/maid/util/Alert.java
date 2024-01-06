package rip.diamond.maid.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Alert {

    SERVER_STARTED(AlertType.SERVER, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 伺服器已啟動, 並可讓玩家進入!"),
    PERMISSION_UPDATE(AlertType.UPDATE, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.STEEL_BLUE + "你的權限已更新 " + CC.GRAY + "%s"),
    DISGUISED(AlertType.DISGUISE, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 在 " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 暱稱成為了 " + CC.GRAY + "%s"),
    BAN(AlertType.PUNISHMENT, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 已被 " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 封鎖帳號 " + CC.GRAY + "%s"),
    LOGIN_FAILED_BANNED(AlertType.PUNISHMENT, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 嘗試進入伺服器, 但他已被封鎖帳號 " + CC.GRAY + "%s"),
    CHAT_FAILED_MUTED(AlertType.PUNISHMENT, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.GRAY + "%s" + CC.STEEL_BLUE + " 嘗試進入伺服器, 但他已被封鎖帳號 " + CC.GRAY + "%s"),
    ;

    @Getter private final AlertType type;
    private final String message;

    public String get(String... args) {
        return String.format(message, (Object[]) args);
    }

    @Getter
    @RequiredArgsConstructor
    public enum AlertType {

        SERVER("maid.alert.server"),
        UPDATE("maid.alert.update"),
        DISGUISE("maid.alert.disguise"),
        PUNISHMENT("maid.alert.punishment"),
        ;

        private final String permission;

    }
}
