package rip.diamond.maid.rank.menu;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.util.*;
import rip.diamond.maid.util.menu.MenuType;
import rip.diamond.maid.util.menu.buttons.Button;
import rip.diamond.maid.util.menu.pagination.PaginatedMenu;

import java.util.HashMap;
import java.util.Map;

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
        for (IRank rank : Maid.INSTANCE.getRankManager().getRanks().values()) {
            buttons.put(buttons.size(), new RankButton(rank));
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
                        .name(CC.AQUA + "創建新的職階")
                        .lore("", CC.YELLOW + "點擊創建一個新的職階")
                        .build();
            }

            @Override
            public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
                // TODO: 2/1/2024
            }
        });
        return buttons;
    }

    @Override
    public String getPaginatedTitle() {
        return "職階一覽";
    }

    @RequiredArgsConstructor
    static class RankButton extends Button {
        private final IRank rank;

        @Override
        public ItemStack getButtonItem(Player player) {
            Material wool = Material.valueOf(HexColorUtil.getNearestWoolColor(rank.getColor()).name() + "_WOOL");
            return new ItemBuilder(wool)
                    .name(rank.getDisplayName())
                    .lore(
                            "",
                            CC.WHITE + " ID: " + CC.AQUA + rank.getUniqueID().toString(),
                            "",
                            CC.WHITE + " 重量: " + CC.AQUA + rank.getPriority(),
                            CC.WHITE + " 顏色: " + CC.AQUA + "<" + rank.getColor() + ">" + rank.getColor(),
                            CC.WHITE + " 名稱: " + CC.AQUA + rank.getName(),
                            CC.WHITE + " 前綴: " + CC.AQUA + rank.getPrefix(),
                            CC.WHITE + " 後綴: " + CC.AQUA + rank.getSuffix(),
                            "",
                            CC.WHITE + " 權限: " + CC.AQUA + rank.getPermissions().size(),
                            CC.WHITE + " 父職階: " + CC.AQUA + rank.getParents().size(),
                            "",
                            CC.WHITE + " 預設職階: " + CC.AQUA + WordUtil.translate(rank.isDefault()),
                            "",
                            CC.YELLOW + " 點擊更改職階"
                    )
                    .build();
        }

        @Override
        public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {

        }
    }
}
