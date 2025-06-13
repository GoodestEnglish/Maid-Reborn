package rip.diamond.maid.api.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import rip.diamond.maid.util.MaidPermission;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum UserSettings {
    GLOBAL_MESSAGE(
            "HOPPER",
            "聊天室",
            List.of("", " 如果開啟, 你將會看到:", "  • 聊天室", ""),
            MaidPermission.SETTINGS_GLOBAL_MESSAGE,
            List.of("開啟", "關閉"),
            "開啟"
    ),
    PRIVATE_MESSAGE(
            "OAK_SIGN",
            "私人信息",
            List.of("", " 如果開啟, 你將會看到:", "  • 私人訊息", ""),
            MaidPermission.SETTINGS_PRIVATE_MESSAGE,
            List.of("開啟", "關閉"),
            "開啟"
    ),
    STAFF_CHAT(
            "CHEST",
            "工作人員聊天室",
            List.of("", " 如果開啟, 你將會看到:", "  • 工作人員聊天室的訊息", ""),
            MaidPermission.SETTINGS_STAFF_CHAT,
            List.of("開啟", "關閉"),
            "開啟"
    ),
    SOCIAL_SPY(
            "LEATHER_HELMET",
            "私訊監聽",
            List.of("", " 如果開啟, 你將會看到:", "  • 玩家私訊對方的訊息", ""),
            MaidPermission.SETTINGS_SOCIAL_SPY,
            List.of("開啟", "關閉"),
            "開啟"
    )
    ;

    private final String icon;
    private final String name;
    private final List<String> description;
    private final String permission;
    private final List<String> options;
    private final String defaultOption;

}
