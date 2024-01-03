package rip.diamond.maid.player.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import rip.diamond.maid.api.user.permission.Permission;
import rip.diamond.maid.api.user.permission.RankPermission;
import rip.diamond.maid.api.user.permission.UserPermission;
import rip.diamond.maid.player.User;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.ItemBuilder;
import rip.diamond.maid.util.TimeUtil;
import rip.diamond.maid.util.menu.Menu;
import rip.diamond.maid.util.menu.MenuType;
import rip.diamond.maid.util.menu.buttons.Button;
import rip.diamond.maid.util.menu.pagination.PaginatedMenu;

import java.util.HashMap;
import java.util.Map;

public class PermissionsMenu extends PaginatedMenu {
    private final User target;

    public PermissionsMenu(Player player, Menu backMenu, User target) {
        super(player, 27, backMenu);
        this.target = target;
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.INVENTORY;
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();
        for (Permission permission : target.getAllPermissions()) {
            if (permission instanceof UserPermission) {
                buttons.put(buttons.size(), new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return new ItemBuilder(Material.DIAMOND)
                                .name(CC.AQUA + permission.get())
                                .lore(
                                        "",
                                        CC.WHITE + " 加入時間: " + CC.AQUA + TimeUtil.formatDate(permission.getAddedAt()),
                                        "",
                                        CC.YELLOW + "點擊移除該權限"
                                )
                                .build();
                    }

                    @Override
                    public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
                        target.removePermission(permission.get());
                        updateMenu();
                    }
                });
            } else if (permission instanceof RankPermission rankPermission) {
                buttons.put(buttons.size(), new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return new ItemBuilder(Material.EMERALD)
                                .name(CC.GREEN + permission.get())
                                .lore(
                                        "",
                                        CC.GRAY + CC.ITALIC.toString() + "該權限是" + rankPermission.getAssociatedRank().getDisplayName() + CC.GRAY + CC.ITALIC + "的一部分",
                                        "",
                                        CC.WHITE + " 加入時間: " + CC.AQUA + TimeUtil.formatDate(permission.getAddedAt()),
                                        "",
                                        CC.YELLOW + "點擊移除該權限"
                                )
                                .build();
                    }

                    @Override
                    public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
                        rankPermission.getAssociatedRank().removePermission(permission.get());
                        updateMenu();
                    }
                });
            }
        }
        return buttons;
    }

    @Override
    public String getPaginatedTitle() {
        return "權限一覽 - " + target.getRealName();
    }
}
