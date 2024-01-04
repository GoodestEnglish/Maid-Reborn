package rip.diamond.maid.permission.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.diamond.maid.permission.UserPermissible;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.ItemBuilder;
import rip.diamond.maid.util.menu.MenuType;
import rip.diamond.maid.util.menu.buttons.Button;
import rip.diamond.maid.util.menu.pagination.PaginatedMenu;

import java.util.HashMap;
import java.util.Map;

public class PermissionInspectMenu extends PaginatedMenu {

    private final UserPermissible permissible;

    public PermissionInspectMenu(Player player, UserPermissible permissible) {
        super(player, 27);
        this.permissible = permissible;
    }

    @Override
    public String getPaginatedTitle() {
        return "權限檢查";
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.INVENTORY;
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();
        for (String permission : permissible.getAllowPermissions()) {
            buttons.put(buttons.size(), new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(Material.GREEN_WOOL)
                            .name(CC.GREEN + permission)
                            .build();
                }
            });
        }
        for (String permission : permissible.getDenyPermissions()) {
            buttons.put(buttons.size(), new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(Material.RED_WOOL)
                            .name(CC.RED + permission)
                            .build();
                }
            });
        }
        return buttons;
    }

}
