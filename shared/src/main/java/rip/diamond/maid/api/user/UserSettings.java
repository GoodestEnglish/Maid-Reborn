package rip.diamond.maid.api.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rip.diamond.maid.MaidPermission;

import java.util.List;

// TODO: 13/1/2024 Make these option actually works
@Getter
@RequiredArgsConstructor
public enum UserSettings {
    SERVER_ALERT(
            "CHEST",
            "伺服器通知",
            List.of("", " 如果開啟, 你將會收到:", "  • 伺服器開啟通知", ""),
            MaidPermission.SETTINGS_SERVER_ALERT,
            List.of("開啟", "關閉"),
            "開啟"
    ),
    UPDATE_ALERT(
            "PAPER",
            "偽裝通知",
            List.of("", " 如果開啟, 你將會收到:", "  • 權限更新通知", ""),
            MaidPermission.SETTINGS_UPDATE_ALERT,
            List.of("開啟", "關閉"),
            "開啟"
    ),
    DISGUISE_ALERT(
            "PLAYER_HEAD",
            "偽裝通知",
            List.of("", " 如果開啟, 你將會收到:", "  • 偽裝開啟通知", ""),
            MaidPermission.SETTINGS_DISGUISE_ALERT,
            List.of("開啟", "關閉"),
            "開啟"
    ),
    PUNISHMENT_ALERT(
            "BARRIER",
            "封鎖通知",
            List.of("", " 如果開啟, 你將會收到:", "  • 懲罰發生通知", "  • 嘗試迴避懲罰通知", ""),
            MaidPermission.SETTINGS_PUNISHMENT_ALERT,
            List.of("開啟", "關閉"),
            "開啟"
    ),
    ;

    private final String icon;
    private final String name;
    private final List<String> description;
    private final String permission;
    private final List<String> options;
    private final String defaultOption;
}
