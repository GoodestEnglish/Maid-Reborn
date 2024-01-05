package rip.diamond.maid.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Alert {

    SERVER_STARTED(AlertType.SERVER, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.LIME_GREEN + "%s" + CC.STEEL_BLUE + " 伺服器已啟動, 並可讓玩家進入!"),

    PERMISSION_UPDATE(AlertType.UPDATE, CC.DARK_GRAY + "[" + CC.ORANGE_RED + "⚠" + CC.DARK_GRAY + "] " + CC.STEEL_BLUE + "你的權限已更新 " + CC.GRAY + "%s")
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
        ;

        private final String permission;

    }
}
