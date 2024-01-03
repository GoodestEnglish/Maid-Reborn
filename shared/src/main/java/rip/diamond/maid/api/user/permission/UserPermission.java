package rip.diamond.maid.api.user.permission;

public class UserPermission extends Permission {
    public UserPermission(String permission) {
        super(permission, System.currentTimeMillis());
    }
}
