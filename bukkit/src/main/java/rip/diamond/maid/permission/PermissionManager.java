package rip.diamond.maid.permission;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import rip.diamond.maid.api.user.permission.Permission;
import rip.diamond.maid.redis.messaging.PacketHandler;
import rip.diamond.maid.redis.packets.bukkit.PermissionUpdatePacket;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.CraftBukkitImplementation;

import java.lang.reflect.Field;

public class PermissionManager {


    /**
     * All permission checks made on standard Bukkit objects are effectively proxied to a
     * {@link PermissibleBase} object, held as a variable on the object.
     * <p>
     * This field is where the permissible is stored on a HumanEntity.
     */
    public static final Field HUMAN_ENTITY_PERMISSIBLE_FIELD;

    /**
     * The field where attachments are stored on a permissible base.
     */
    public static final Field PERMISSIBLE_BASE_ATTACHMENTS_FIELD;


    static {
        try {
            HUMAN_ENTITY_PERMISSIBLE_FIELD = CraftBukkitImplementation.obcClass("entity.CraftHumanEntity").getDeclaredField("perm");
            HUMAN_ENTITY_PERMISSIBLE_FIELD.setAccessible(true);

            PERMISSIBLE_BASE_ATTACHMENTS_FIELD = PermissibleBase.class.getDeclaredField("attachments");
            PERMISSIBLE_BASE_ATTACHMENTS_FIELD.setAccessible(true);
        } catch (NoSuchFieldException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void initPlayer(Player player) throws IllegalAccessException {
        try {
            // Get the existing PermissibleBase held by the player
            PermissibleBase oldPermissible = (PermissibleBase) HUMAN_ENTITY_PERMISSIBLE_FIELD.get(player);

            if (oldPermissible instanceof UserPermissible) {
                // Seems we have already injected into this player.
                throw new IllegalStateException("UserPermissible already injected into player '" + player.getName() + "'");
            }

            UserPermissible permissible = new UserPermissible(player);
            HUMAN_ENTITY_PERMISSIBLE_FIELD.set(player, permissible);
        } catch (Exception e) {
            player.kick(Common.text(CC.RED + "Failed to inject UserPermissible. Please try again."));
            e.printStackTrace();
        }
    }

}
