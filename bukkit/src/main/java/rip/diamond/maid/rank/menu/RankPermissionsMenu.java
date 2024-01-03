package rip.diamond.maid.rank.menu;

import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.api.user.permission.RankPermission;
import rip.diamond.maid.rank.Rank;
import rip.diamond.maid.util.*;
import rip.diamond.maid.util.menu.Menu;
import rip.diamond.maid.util.menu.MenuType;
import rip.diamond.maid.util.menu.buttons.Button;
import rip.diamond.maid.util.menu.buttons.ConversationButton;
import rip.diamond.maid.util.menu.pagination.PaginatedMenu;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class RankPermissionsMenu extends PaginatedMenu {

    private final Rank rank;

    public RankPermissionsMenu(Player player, Menu backMenu, Rank rank) {
        super(player, 27, backMenu);
        this.rank = rank;
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.INVENTORY;
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();
        for (RankPermission permission : rank.getAllPermissions()) {
            IRank associatedRank = Maid.INSTANCE.getRankManager().getRanks().get(permission.getAssociatedRankUUID());
            buttons.put(buttons.size(), new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    ItemBuilder builder = new ItemBuilder(permission.isEnabled() ? Material.REDSTONE_TORCH : Material.LEVER)
                            .name((permission.isEnabled() ? CC.GREEN + "+ " : CC.RED + "- ") + permission.get())
                            .lore(
                                    "",
                                    CC.GRAY + CC.ITALIC.toString() + " 該權限是" + associatedRank.getDisplayName(true) + CC.GRAY + CC.ITALIC + "的一部分",
                                    "",
                                    CC.WHITE + " 加入時間: " + CC.AQUA + TimeUtil.formatDate(permission.getAddedAt()),
                                    ""
                            );
                    if (associatedRank == rank) {
                        builder.lore(CC.YELLOW + "點擊左鍵移除該權限", CC.YELLOW + "點擊右鍵切換權限狀態");
                    } else {
                        builder.lore(CC.RED + "該權限是" + associatedRank.getDisplayName(true) + CC.RED + "的一部分, 請到該權限的設置介面執行操作");
                    }
                    return builder.build();
                }

                @Override
                public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
                    if (associatedRank == rank) {
                        if (clickType.isLeftClick()) {
                            rank.removePermission(permission.get());
                            updateMenu();
                        } else if (clickType.isRightClick()) {
                            permission.setEnabled(!permission.isEnabled());
                            updateMenu();
                        }
                    }
                }
            });
        }
        return buttons;
    }

    @Override
    public Map<Integer, Button> getNavigationBar() {
        Map<Integer, Button> buttons = super.getNavigationBar();
        buttons.put(getSize() - 5, new ConversationButton() {
            @Override
            public String getInstruction() {
                return "請輸入你想新增的權限";
            }

            @Override
            public BiConsumer<ConversationContext, String> getAction() {
                return ((cc, permission) -> {
                    if (rank.containPermission(permission)) {
                        Common.sendMessage(player, CC.RED + "錯誤: 權限已存在");
                        return;
                    }
                    rank.addPermission(permission);
                    Maid.INSTANCE.getRankManager().saveRank(rank);

                    Common.sendMessage(player, CC.GREEN + "成功新增權限 " + CC.AQUA + permission);
                    updateMenu();
                });
            }

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PLAYER_HEAD)
                        .texture(HeadUtil.LIME_PLUS.get())
                        .name(CC.AQUA + "新增權限")
                        .lore(
                                "",
                                CC.WHITE + " 現時權限數目: " + CC.AQUA + rank.getPermissions().size(),
                                CC.WHITE + " 現時所有權限數目: " + CC.AQUA + rank.getAllPermissions().size(),
                                "",
                                CC.YELLOW + "點擊新增權限"
                        )
                        .build();
            }
        });
        return buttons;
    }

    @Override
    public String getPaginatedTitle() {
        return "職階權限 - " + rank.getDisplayName();
    }
}
