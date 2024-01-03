package rip.diamond.maid.api.user.permission;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public abstract class Permission {

    private final String permission;
    @Getter private final long addedAt;
    @Getter @Setter private boolean enabled = true;

    /**
     * Get the permission as string.
     *
     * @return The permission
     */
    public String get() {
        return permission;
    }

}
