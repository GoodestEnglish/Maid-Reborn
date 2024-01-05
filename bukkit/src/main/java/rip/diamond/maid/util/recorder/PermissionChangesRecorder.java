package rip.diamond.maid.util.recorder;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import rip.diamond.maid.util.*;

import java.util.LinkedHashSet;
import java.util.Set;

@RequiredArgsConstructor
public class PermissionChangesRecorder {

    private final Set<String> oldAllowPermissions;
    private final Set<String> oldDenyPermissions;

    private Set<String> newAllowPermissions;
    private Set<String> newDenyPermissions;

    public void recordNewPermissions(Set<String> newAllowPermissions, Set<String> newDenyPermissions) {
        this.newAllowPermissions = newAllowPermissions;
        this.newDenyPermissions = newDenyPermissions;
    }

    public void outputChanges(Player player) {
        Preconditions.checkArgument(canOutput(), "Failed out changes in " + getClass().getSimpleName() + " for " + player.getName());

        Tasks.runAsync(() -> {
            Alert alert = Alert.PERMISSION_UPDATE;
            Set<String> changes = new LinkedHashSet<>();

            //Check if the player has permission to view the changes
            if (!player.hasPermission(alert.getType().getPermission())) {
                return;
            }

            //Check if the allow permission is removed
            for (String permission : oldAllowPermissions) {
                if (!newAllowPermissions.contains(permission)) {
                    changes.add(CC.ITALIC + "<#705353>移除了 " + CC.RESET + CC.GREEN + permission);
                }
            }

            //Check if the deny permission is removed
            for (String permission : oldDenyPermissions) {
                if (!newDenyPermissions.contains(permission)) {
                    changes.add(CC.ITALIC + "<#705353>移除了 " + CC.RESET + CC.RED + permission);
                }
            }

            //Check if the allow permission is added
            for (String permission : newAllowPermissions) {
                if (!oldAllowPermissions.contains(permission)) {
                    changes.add(CC.ITALIC + "<#537056>新增了 " + CC.RESET + CC.GREEN + permission);
                }
            }

            //Check if the deny permission is added
            for (String permission : newDenyPermissions) {
                if (!oldDenyPermissions.contains(permission)) {
                    changes.add(CC.ITALIC + "<#537056>新增了 " + CC.RESET + CC.RED + permission);
                }
            }

            if (changes.isEmpty()) {
                return;
            }

            Common.sendMessage(player, alert.get(new ChatButton("[查看轉變]", StringUtils.join(changes, "\n"), null, null).toString()));
        });
    }

    public boolean canOutput() {
        return oldAllowPermissions != null && oldDenyPermissions != null && newAllowPermissions != null && newDenyPermissions != null;
    }

}
