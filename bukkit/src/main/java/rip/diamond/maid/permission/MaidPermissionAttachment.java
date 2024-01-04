package rip.diamond.maid.permission;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class MaidPermissionAttachment {
    @Getter private final String permission;
    private final boolean value;

    @Getter @Setter private Map<String, MaidPermissionAttachment> childAttachments = new HashMap<>();

    public boolean getValue() {
        return value;
    }
}
