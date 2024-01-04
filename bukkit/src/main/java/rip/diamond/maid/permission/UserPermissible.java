package rip.diamond.maid.permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.util.Common;

import java.lang.reflect.Field;
import java.util.*;

// TODO: 4/1/2024 Recode this, to make this class structure better
// TODO: 4/1/2024 Support * permission
// TODO: 4/1/2024 Support - permission
public class UserPermissible extends PermissibleBase {

    private static final Field ATTACHMENTS_FIELD;

    static {
        try {
            ATTACHMENTS_FIELD = PermissibleBase.class.getDeclaredField("attachments");
            ATTACHMENTS_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final Player player;
    private final Maid plugin;
    private final List<PermissionAttachment> attachments;
    private final Map<String, PermissionAttachmentInfo> permissions;

    public UserPermissible(Player opable) {
        super(opable);
        this.player = opable;
        this.permissions = new HashMap<>();
        this.plugin = Maid.INSTANCE;
        this.attachments = new ArrayList<>();
        try {
            ATTACHMENTS_FIELD.set(this, this.attachments);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        this.recalculatePermissions();
    }

    @Override
    public boolean isOp() {
        return player.isOp();
    }

    @Override
    public void setOp(boolean value) {
        player.setOp(value);
    }

    @Override
    public boolean isPermissionSet(@NotNull String name) {
        String permission = name.toLowerCase();

        IUser user = plugin.getUserManager().getUser(player.getUniqueId()).join();
        if (user.containPermission(name)) {
            return true;
        }

        if (this.permissions.containsKey(permission)) {
            return this.permissions.get(permission).getValue();
        }

        if (this.isOp()) {
            return true;
        }

        Permission perm = plugin.getServer().getPluginManager().getPermission(name);

        if (perm != null) {
            return perm.getDefault().getValue(isOp());
        } else {
            return Permission.DEFAULT_PERMISSION.getValue(this.isOp());
        }
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission perm) {
        return this.isPermissionSet(perm.getName());
    }

    @Override
    public boolean hasPermission(@NotNull String inName) {
        return isPermissionSet(inName);
    }

    @Override
    public boolean hasPermission(@NotNull Permission perm) {
        return this.hasPermission(perm.getName());
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getName() + " is not enabled yet");
        }

        PermissionAttachment permissionAttachment = new PermissionAttachment(plugin, this.player);
        this.attachments.add(permissionAttachment);
        this.recalculatePermissions();
        return permissionAttachment;
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value) {
        if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getName() + " is not enabled yet");
        }

        PermissionAttachment permissionAttachment = new PermissionAttachment(plugin, this.player);
        permissionAttachment.setPermission(name, value);
        this.attachments.add(permissionAttachment);
        this.recalculatePermissions();
        return permissionAttachment;
    }

    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value, int ticks) {
        if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getName() + " is disabled");
        }

        PermissionAttachment result = this.addAttachment(plugin, ticks);

        if (result != null) {
            result.setPermission(name, value);
        }

        return result;
    }

    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, int ticks) {
        if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getName() + " is disabled");
        }

        PermissionAttachment result = this.addAttachment(plugin);
        if (Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new RemoveAttachmentRunnable(result), ticks) == -1) {
            Common.log("Error: Could not add PermissionAttachment to " + this.player + " for plugin " + plugin.getDescription().getName() + ": Scheduler returned -1");
            result.remove();
            return null;
        }

        return result;
    }

    @Override
    public void removeAttachment(@NotNull PermissionAttachment attachment) {
        if (!this.attachments.contains(attachment)) {
            throw new IllegalArgumentException("Given attachment is not part of Permissible object " + this.player.getName());
        }

        this.attachments.remove(attachment);
        PermissionRemovedExecutor ex = attachment.getRemovalCallback();

        if (ex != null) {
            ex.attachmentRemoved(attachment);
        }

        this.recalculatePermissions();
    }

    @Override
    public void recalculatePermissions() {
        if (this.permissions == null) return;

        this.clearPermissions();

        Set<Permission> defaults = Bukkit.getServer().getPluginManager().getDefaultPermissions(this.isOp());
        Bukkit.getServer().getPluginManager().subscribeToDefaultPerms(this.isOp(), this.player);

        for (Permission perm : defaults) {
            String name = perm.getName().toLowerCase();
            this.permissions.put(name, new PermissionAttachmentInfo(this.player, name, null, true));
            Bukkit.getServer().getPluginManager().subscribeToPermission(name, this.player);
            this.calculateChildPermissions(perm.getChildren(), false, null);
        }

        for (PermissionAttachment attachment : this.attachments) {
            for (Map.Entry<String, Boolean> entry : attachment.getPermissions().entrySet()) {
                this.permissions.put(
                        entry.getKey().toLowerCase(),
                        new PermissionAttachmentInfo(this.player, entry.getKey().toLowerCase(), attachment, entry.getValue())
                );
                plugin.getServer().getPluginManager().subscribeToPermission(entry.getKey().toLowerCase(), this.player);
                this.calculateChildPermissions(attachment.getPermissions(), entry.getValue(), attachment);
            }
        }

        IUser user = plugin.getUserManager().getUser(player.getUniqueId()).join();
        for (rip.diamond.maid.api.user.permission.Permission permission : user.getAllPermissions()) {
            String perm = permission.get();
            if (this.permissions.containsKey(perm.toLowerCase())) {
                continue;
            }
            this.permissions.put(perm.toLowerCase(), new PermissionAttachmentInfo(this.player, perm, null, true));
            plugin.getServer().getPluginManager().subscribeToPermission(perm.toLowerCase(), this.player);
        }
    }

    @Override
    public synchronized void clearPermissions() {
        Set<String> permissions = this.permissions.keySet();

        for (String permission : permissions) {
            Bukkit.getServer().getPluginManager().unsubscribeFromPermission(permission, this.player);
        }

        for (PermissionAttachment attachment : this.attachments) {
            for (String permission : attachment.getPermissions().keySet()) {
                Bukkit.getServer().getPluginManager().unsubscribeFromPermission(permission.toLowerCase(), this.player);
            }
        }

        Bukkit.getServer().getPluginManager().unsubscribeFromDefaultPerms(false, this.player);
        Bukkit.getServer().getPluginManager().unsubscribeFromDefaultPerms(true, this.player);

        this.permissions.clear();
    }

    @Override
    public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return new HashSet<>(this.permissions.values());
    }

    private void calculateChildPermissions(Map<String, Boolean> children, boolean invert, PermissionAttachment attachment) {
        Set<String> keys = children.keySet();
        for (String name : keys) {
            Permission perm = Bukkit.getServer().getPluginManager().getPermission(name);
            boolean value = children.get(name) ^ invert;
            String lname = name.toLowerCase();
            this.permissions.put(lname, new PermissionAttachmentInfo(this.player, lname, attachment, value));
            Bukkit.getServer().getPluginManager().subscribeToPermission(name, this.player);
            if (perm != null) {
                this.calculateChildPermissions(perm.getChildren(), !value, attachment);
            }
        }

    }

    private static class RemoveAttachmentRunnable implements Runnable {
        private final PermissionAttachment attachment;

        public RemoveAttachmentRunnable(PermissionAttachment attachment) {
            this.attachment = attachment;
        }

        public void run() {
            this.attachment.remove();
        }
    }
}
