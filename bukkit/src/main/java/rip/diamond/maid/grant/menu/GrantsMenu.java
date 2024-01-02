package rip.diamond.maid.grant.menu;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.MaidPermission;
import rip.diamond.maid.api.user.IGrant;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.grant.Grant;
import rip.diamond.maid.player.User;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.ItemBuilder;
import rip.diamond.maid.util.TimeUtil;
import rip.diamond.maid.util.menu.MenuType;
import rip.diamond.maid.util.menu.buttons.Button;
import rip.diamond.maid.util.menu.pagination.PaginatedMenu;
import rip.diamond.maid.util.procedure.Procedure;

import java.util.HashMap;
import java.util.Map;

public class GrantsMenu extends PaginatedMenu {
    private final User target;

    public GrantsMenu(Player player, User target) {
        super(player, 27);
        this.target = target;
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.INVENTORY;
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();
        for (IGrant grant : target.getGrants()) {
            buttons.put(buttons.size(), new GrantButton(grant));
        }
        return buttons;
    }

    @Override
    public String getPaginatedTitle() {
        return "職階升級紀錄 - " + target.getRealName();
    }

    @RequiredArgsConstructor
    class GrantButton extends Button {
        private final IGrant grant;

        @Override
        public ItemStack getButtonItem(Player player) {
            ItemBuilder builder;
            if (grant.isActive()) {
                builder = new ItemBuilder(Material.LIME_WOOL)
                        .name(CC.LIME_GREEN + "(有效) " + TimeUtil.formatDate(grant.getIssuedAt()))
                        .lore(
                                " ",
                                CC.WHITE + " " + "職階: " + CC.AQUA + grant.getRank().getDisplayName(),
                                CC.WHITE + " " + "持續時間: " + CC.AQUA + TimeUtil.formatDuration(grant.getDuration()),
                                "",
                                CC.WHITE + " " + "執行者: " + CC.AQUA + grant.getIssuerName(),
                                CC.WHITE + " " + "執行原因: " + CC.AQUA + grant.getReason()
                        );
                if (!grant.getRank().isDefault()) {
                    builder.lore("", CC.YELLOW + "點擊移除本次升級紀錄!");
                }
            } else {
                builder = new ItemBuilder(Material.RED_WOOL)
                        .name(CC.LIME_GREEN + "(無效) " + TimeUtil.formatDate(grant.getIssuedAt()))
                        .lore(
                                CC.WHITE + " " + "職階: " + CC.AQUA + grant.getRank().getDisplayName(),
                                CC.WHITE + " " + "持續時間: " + CC.AQUA + TimeUtil.formatDuration(grant.getDuration()),
                                "",
                                CC.WHITE + " " + "執行者: " + CC.AQUA + grant.getIssuer(),
                                CC.WHITE + " " + "執行原因: " + CC.AQUA + grant.getReason(),
                                "",
                                CC.WHITE + " " + "移除者: " + CC.AQUA + grant.getRevokerName(),
                                CC.WHITE + " " + "移除原因: " + CC.AQUA + grant.getRevokedReason(),
                                CC.WHITE + " " + "移除時間: " + CC.AQUA + grant.getRevokedAt()
                        );
            }
            return builder.build();
        }

        @Override
        public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
            if (!grant.isActive()) {
                return;
            }
            if (!player.hasPermission(MaidPermission.GRANTS)) {
                return;
            }
            if (grant.getRank().isDefault()) {
                Common.sendMessage(player, CC.RED + "無法移除預設的職階");
                return;
            }

            player.closeInventory();
            Procedure.buildProcedure(player, "請在聊天室輸入移除的原因", (reason) -> {
                User user = (User) Maid.INSTANCE.getUserManager().getUser(player.getUniqueId());

                Grant g = (Grant) grant;
                g.revoke(user, reason);
                Maid.INSTANCE.getUserManager().saveUser(target);

                updateMenu();
            });
        }
    }
}
