package rip.diamond.maid.grant.menu;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidPermission;
import rip.diamond.maid.api.user.IGrant;
import rip.diamond.maid.grant.Grant;
import rip.diamond.maid.player.User;
import rip.diamond.maid.redis.messaging.PacketHandler;
import rip.diamond.maid.redis.packets.bukkit.PermissionUpdatePacket;
import rip.diamond.maid.util.*;
import rip.diamond.maid.util.menu.Menu;
import rip.diamond.maid.util.menu.MenuType;
import rip.diamond.maid.util.menu.buttons.Button;
import rip.diamond.maid.util.menu.buttons.ConversationButton;
import rip.diamond.maid.util.menu.pagination.PaginatedMenu;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class GrantsMenu extends PaginatedMenu {
    private final User target;

    public GrantsMenu(Player player, User target) {
        super(player, 27);
        this.target = target;
    }

    public GrantsMenu(Player player, Menu backMenu, User target) {
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
        for (IGrant grant : target.getGrants()) {
            buttons.put(buttons.size(), new GrantButton(grant));
        }
        return buttons;
    }

    @Override
    public Map<Integer, Button> getNavigationBar() {
        Map<Integer, Button> buttons = super.getNavigationBar();
        buttons.put(getSize() - 5, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PLAYER_HEAD)
                        .texture(HeadUtil.LIME_PLUS.get())
                        .name(CC.AQUA + "新增升級紀錄")
                        .lore(
                                "",
                                CC.WHITE + " 有效升級紀錄: " + CC.GREEN + target.getActiveGrants().size(),
                                CC.WHITE + " 無效升級紀錄: " + CC.RED + (target.getGrants().size() - target.getActiveGrants().size()),
                                "",
                                CC.YELLOW + "點擊新增權限"
                        )
                        .build();
            }

            @Override
            public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
                new GrantMenu(player, target).updateMenu();
            }
        });
        return buttons;
    }

    @Override
    public String getPaginatedTitle() {
        return "職階升級紀錄 - " + target.getRealName();
    }

    @RequiredArgsConstructor
    class GrantButton extends ConversationButton {
        private final IGrant grant;

        @Override
        public String getInstruction() {
            return "請在聊天室輸入移除的原因";
        }

        @Override
        public BiConsumer<ConversationContext, String> getAction() {
            return (cc, reason) -> {
                User user = (User) Maid.INSTANCE.getUserManager().getUser(player.getUniqueId()).join();

                Grant g = (Grant) grant;
                g.revoke(user, reason);
                Maid.INSTANCE.getUserManager().saveUser(target);
                PacketHandler.send(new PermissionUpdatePacket(target.getUniqueID()));

                updateMenu();
            };
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            ItemBuilder builder;
            if (grant.isActive()) {
                builder = new ItemBuilder(Material.LIME_WOOL)
                        .name(CC.LIME_GREEN + "(有效) " + TimeUtil.formatDate(grant.getIssuedAt()))
                        .lore(
                                " ",
                                CC.WHITE + " 職階: " + CC.AQUA + grant.getRank().getDisplayName(true),
                                CC.WHITE + " 持續時間: " + CC.AQUA + TimeUtil.formatDuration(grant.getDuration()) + (grant.getDuration() != TimeUtil.PERMANENT ? CC.GRAY + " (剩餘" + TimeUtil.formatDuration(grant.getIssuedAt() + grant.getDuration() - System.currentTimeMillis()) + ")" : ""),
                                "",
                                CC.WHITE + " 執行者: " + CC.AQUA + grant.getIssuerName(),
                                CC.WHITE + " 執行原因: " + CC.AQUA + grant.getReason(),
                                "",
                                CC.YELLOW + "點擊移除本次升級紀錄!"
                        );
            } else {
                builder = new ItemBuilder(Material.RED_WOOL)
                        .name(CC.RED + "(無效) " + TimeUtil.formatDate(grant.getIssuedAt()))
                        .lore(
                                CC.WHITE + " 職階: " + CC.AQUA + grant.getRank().getDisplayName(true),
                                CC.WHITE + " 持續時間: " + CC.AQUA + TimeUtil.formatDuration(grant.getDuration()),
                                "",
                                CC.WHITE + " 執行者: " + CC.AQUA + grant.getIssuerName(),
                                CC.WHITE + " 執行原因: " + CC.AQUA + grant.getReason(),
                                "",
                                CC.WHITE + " 移除者: " + CC.AQUA + grant.getRevokerName(),
                                CC.WHITE + " 移除原因: " + CC.AQUA + grant.getRevokedReason(),
                                CC.WHITE + " 移除時間: " + CC.AQUA + TimeUtil.formatDuration(grant.getRevokedAt())
                        );
            }
            return builder.build();
        }

        @Override
        public boolean isAllowClick() {
            if (!grant.isActive()) {
                return false;
            }
            if (!player.hasPermission(MaidPermission.GRANT)) {
                return false;
            }
            return super.isAllowClick();
        }
    }
}
