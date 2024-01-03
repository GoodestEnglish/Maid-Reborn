package rip.diamond.maid.rank.menu;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.rank.Rank;
import rip.diamond.maid.util.*;
import rip.diamond.maid.util.menu.MenuType;
import rip.diamond.maid.util.menu.buttons.Button;
import rip.diamond.maid.util.menu.buttons.ConversationButton;
import rip.diamond.maid.util.menu.pagination.PaginatedMenu;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class RanksMenu extends PaginatedMenu {
    public RanksMenu(Player player) {
        super(player, 27);
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.INVENTORY;
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();
        for (IRank rank : Maid.INSTANCE.getRankManager().getRanksInOrder()) {
            buttons.put(buttons.size(), new RankButton(rank));
        }
        return buttons;
    }

    @Override
    public Map<Integer, Button> getNavigationBar() {
        Map<Integer, Button> buttons = super.getNavigationBar();
        buttons.put(getSize() - 5, new ConversationButton() {
            @Override
            public String getInstruction() {
                return "請輸入你想創建的職階的名字";
            }

            @Override
            public BiConsumer<ConversationContext, String> getAction() {
                return ((cc, name) -> {
                    if (Maid.INSTANCE.getRankManager().getRank(name) != null) {
                        Common.sendMessage(player, CC.RED + "錯誤: 職階已存在");
                        return;
                    }
                    Rank rank = new Rank("#D3D3D3", name);
                    Maid.INSTANCE.getRankManager().saveRank(rank);

                    Common.sendMessage(player, CC.GREEN + "成功創建職階 " + rank.getDisplayName() + CC.AQUA + " <click:run_command:/rank manage " + rank.getName() + ">[點擊設置職階]</click>");
                    updateMenu();
                });
            }

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PLAYER_HEAD)
                        .texture(HeadUtil.LIME_PLUS.get())
                        .name(CC.AQUA + "創建新的職階")
                        .lore("", CC.YELLOW + "點擊創建一個新的職階")
                        .build();
            }
        });
        return buttons;
    }

    @Override
    public String getPaginatedTitle() {
        return "職階一覽";
    }

    @RequiredArgsConstructor
    class RankButton extends Button {
        private final IRank rank;

        @Override
        public ItemStack getButtonItem(Player player) {
            Material wool = Material.valueOf(HexColorUtil.getNearestWoolColor(rank.getColor()).name() + "_WOOL");
            return new ItemBuilder(wool)
                    .name(rank.getDisplayName(true))
                    .lore(
                            "",
                            CC.WHITE + " ID: " + CC.AQUA + rank.getUniqueID().toString(),
                            "",
                            CC.WHITE + " 重量: " + CC.AQUA + rank.getPriority(),
                            CC.WHITE + " 名稱: " + CC.AQUA + rank.getName(),
                            CC.WHITE + " 顯示名稱: " + CC.AQUA + rank.getDisplayName(true),
                            CC.WHITE + " 前綴: " + CC.GRAY + "'" + CC.AQUA + rank.getPrefix() + CC.RESET + "<!italic>" + CC.GRAY + "'",
                            CC.WHITE + " 後綴: " + CC.GRAY + "'" + CC.AQUA + rank.getSuffix() + CC.RESET + "<!italic>" + CC.GRAY + "'",
                            CC.WHITE + " 顏色: " + CC.AQUA + "<" + rank.getColor() + ">" + rank.getColor(),
                            CC.WHITE + " 聊天顏色: " + CC.AQUA + "<" + rank.getChatColor() + ">" + rank.getChatColor(),
                            "",
                            CC.WHITE + " 權限: " + CC.AQUA + rank.getPermissions().size(),
                            CC.WHITE + " 父職階: " + CC.AQUA + rank.getParents().size(),
                            "",
                            CC.WHITE + " 預設職階: " + CC.AQUA + WordUtil.translate(rank.isDefault()),
                            "",
                            CC.YELLOW + "點擊更改職階"
                    )
                    .build();
        }

        @Override
        public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
            new RankManageMenu(player, RanksMenu.this, (Rank) rank).updateMenu();
        }
    }
}
