package rip.diamond.maid.api.user.permission;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Permission {

    private final String permission;
    @Getter private final long addedAt;

    /**
     * Get the permission as string.
     *
     * @return The permission
     */
    public String get() {
        return permission;
    }

}
