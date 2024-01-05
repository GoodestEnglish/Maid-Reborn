package rip.diamond.maid.permission;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.recorder.PermissionChangesRecorder;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class UserPermissible extends PermissibleBase {

    private final Player player;
    private final Maid plugin;
    @Getter private final LinkedHashSet<String> allowPermissions;
    @Getter private final LinkedHashSet<String> denyPermissions;

    public UserPermissible(Player player) {
        super(player);
        this.player = player;
        this.plugin = Maid.INSTANCE;
        this.allowPermissions = new LinkedHashSet<>();
        this.denyPermissions = new LinkedHashSet<>();

        recalculatePermissions(false);
    }

    @Override
    public boolean isOp() {
        return player.isOp();
    }

    @Override
    public void setOp(boolean value) {
        player.setOp(value);
    }

    public PermissionState checkPermission(@NotNull String name) {
        String permission = name.toLowerCase();

        IUser user = plugin.getUserManager().getUser(player.getUniqueId()).join();

        //Check if the user is null or not
        if (user == null) {
            //Should not happen because IUser is created during login and UserPermissible is injected during join, but just in case
            throw new NullPointerException("Cannot check permission because user is null");
        }

        //Check if the player is an operator
        if (isOp()) {
            return PermissionState.TRUE;
        }

        if (denyPermissions.contains("*")) {
            return PermissionState.FALSE;
        }
        if (allowPermissions.contains("*")) {
            return PermissionState.TRUE;
        }

        //Check the permissions map and see if the permission is set
        for (String denyPermission : denyPermissions) {
            //Check for basic permission
            if (denyPermission.equals(permission)) {
                return PermissionState.FALSE;
            }
            //Check for * permission
            if (!denyPermission.contains(".")) {
                continue;
            }
            int index = denyPermission.lastIndexOf(".");
            String head = denyPermission.substring(0, index);
            String tail = denyPermission.substring(index);
            if (permission.startsWith(head) && tail.equals(".*")) {
                return PermissionState.FALSE;
            }
        }
        for (String allowPermission : allowPermissions) {
            //Check for basic permission
            if (allowPermission.equals(permission)) {
                return PermissionState.TRUE;
            }
            //Check for * permission
            if (!allowPermission.contains(".")) {
                continue;
            }
            int index = allowPermission.lastIndexOf(".");
            String head = allowPermission.substring(0, index);
            String tail = allowPermission.substring(index);
            if (permission.startsWith(head) && tail.equals(".*")) {
                return PermissionState.TRUE;
            }
        }

        //No overrides found.
        return PermissionState.NOT_FOUND;
    }

    @Override
    public boolean isPermissionSet(@NotNull String name) {
        return checkPermission(name) == PermissionState.TRUE;
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission perm) {
        return isPermissionSet(perm.getName());
    }

    @Override
    public boolean hasPermission(@NotNull String inName) {
        PermissionState state = checkPermission(inName);

        //Check if the permission is found from overrides
        if (state != PermissionState.NOT_FOUND) {
            return state.getValue();
        }

        //Otherwise, return the default value of the permission
        Permission permission = plugin.getServer().getPluginManager().getPermission(inName);
        if (permission != null) {
            return permission.getDefault().getValue(isOp());
        } else {
            return Permission.DEFAULT_PERMISSION.getValue(isOp());
        }
    }

    @Override
    public boolean hasPermission(@NotNull Permission permission) {
        PermissionState state = checkPermission(permission.getName());

        //Check if the permission is found from overrides
        if (state != PermissionState.NOT_FOUND) {
            return state.getValue();
        }

        //Otherwise, return the default value of the permission
        return permission.getDefault().getValue(isOp());
    }

    @Override
    @NotNull
    public synchronized PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value) {
        if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getName() + " is not enabled yet");
        }

        Common.log(
                "Warning: Some plugin called the function addAttachment(Plugin, String, boolean), but this function shouldn't be used because Permissible is a custom Permissible.",
                "This function will still work because it is marked as @NotNull"
        );

        PermissionAttachment permissionAttachment = new PermissionAttachment(plugin, player);
        permissionAttachment.setPermission(name, value);

        return permissionAttachment;
    }

    @Override
    @NotNull
    public synchronized PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getName() + " is not enabled yet");
        }

        Common.log(
                "Warning: Some plugin called the function addAttachment(Plugin), but this function shouldn't be used because Permissible is a custom Permissible.",
                "This function will still work because it is marked as @NotNull"
        );

        return new PermissionAttachment(plugin, player);
    }

    @Override
    @Nullable
    public synchronized PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value, int ticks) {
        Common.log("Warning: Some plugin called the function addAttachment(Plugin, String, boolean, int), but this function is no longer be used because Permissible is a custom Permissible.");
        return null;
    }

    @Override
    public synchronized PermissionAttachment addAttachment(@NotNull Plugin plugin, int ticks) {
        Common.log("Warning: Some plugin called the function addAttachment(Plugin, int), but this function is no longer be used because Permissible is a custom Permissible.");
        return null;
    }

    @Override
    public synchronized void removeAttachment(@NotNull PermissionAttachment attachment) {
        Common.log("Warning: Some plugin called the function removeAttachment(PermissionAttachment), but this function is no longer be used because Permissible is a custom Permissible.");
    }

    /**
     * Recalculate player's permission by reset the permissions set and reassign them.
     *
     * @param record True if player permission should be recorded for logging purpose
     */
    public void recalculatePermissions(boolean record) {
        //All variable will be null when running the default constructor of bukkit's PermissibleBase
        //We will just don't do anything if those are null, and call this function afterward in our own constructor
        if (player == null || plugin == null || allowPermissions == null || denyPermissions == null) {
            return;
        }

        //Record all the old permissions, for logging purposes
        PermissionChangesRecorder recorder = record ? new PermissionChangesRecorder(Set.copyOf(allowPermissions), Set.copyOf(denyPermissions)) : null;

        //Clear all the old permissions
        clearPermissions();

        IUser user = plugin.getUserManager().getUser(player.getUniqueId()).getNow(null);
        if (user == null) {
            throw new NullPointerException("Cannot get user for player '" + player.getName() + "' instantly");
        }

        user.getAllPermissions().forEach(perm -> {
            String permission = perm.get().toLowerCase();
            if (perm.isEnabled()) {
                allowPermissions.add(permission);
                Bukkit.getServer().getPluginManager().subscribeToPermission(permission, player);
            } else {
                denyPermissions.add(permission);
                Bukkit.getServer().getPluginManager().unsubscribeFromPermission(permission, player);
            }
        });

        //Collect all the changed permissions, for logging purposes
        if (record) {
            recorder.recordNewPermissions(Set.copyOf(allowPermissions), Set.copyOf(denyPermissions));
            recorder.outputChanges(player);
        }
    }

    @Override
    public void recalculatePermissions() {
        recalculatePermissions(true);
    }

    @Override
    public synchronized void clearPermissions() {
        Set<String> permissions = getPermissions();

        for (String permission : permissions) {
            Bukkit.getServer().getPluginManager().unsubscribeFromPermission(permission, player);
        }

        Bukkit.getServer().getPluginManager().unsubscribeFromDefaultPerms(false, player);
        Bukkit.getServer().getPluginManager().unsubscribeFromDefaultPerms(true, player);

        this.allowPermissions.clear();
        this.denyPermissions.clear();
    }

    @Override
    public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
        throw new IllegalArgumentException("getEffectivePermissions() is no longer used");
    }

    public Set<String> getPermissions() {
        Set<String> permissions = new TreeSet<>(allowPermissions);
        permissions.addAll(denyPermissions);

        return permissions;
    }

    enum PermissionState {
        TRUE,
        FALSE,
        NOT_FOUND;

        public boolean getValue() {
            switch (this) {
                case TRUE -> {
                    return true;
                }
                case FALSE -> {
                    return false;
                }
                default -> throw new RuntimeException();
            }
        }
    }
}
