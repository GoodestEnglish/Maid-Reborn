package rip.diamond.maid.rank.menu;

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
import rip.diamond.maid.util.menu.Menu;
import rip.diamond.maid.util.menu.MenuType;
import rip.diamond.maid.util.menu.buttons.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class RankManageMenu extends Menu {

    private final Menu backMenu;
    private final Rank rank;

    public RankManageMenu(Player player, Menu backMenu, Rank rank) {
        super(player, 45);
        this.backMenu = backMenu;
        this.rank = rank;
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.INVENTORY;
    }

    @Override
    public String getTitle() {
        return "職階編輯 - " + rank.getDisplayName();
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(10, new ConversationButton() {
            @Override
            public String getInstruction() {
                return "請輸入一個新的名稱";
            }

            @Override
            public BiConsumer<ConversationContext, String> getAction() {
                return (cc, str) -> {
                    if (Maid.INSTANCE.getRankManager().getRank(str) != null) {
                        Common.sendMessage(player, CC.RED + "錯誤: 你不能更改到這個名稱, 職階已存在");
                        return;
                    }

                    rank.setName(str);
                    Maid.INSTANCE.getRankManager().saveRank(rank);

                    Common.sendMessage(player, CC.GREEN + "成功更改名稱到 " + CC.AQUA + rank.getName());
                    updateMenu();
                };
            }

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.OAK_SIGN)
                        .name(CC.AQUA + "更改名稱")
                        .lore("", CC.WHITE + " 現時名稱: " + CC.AQUA + rank.getName(), "", CC.YELLOW + "點擊更改")
                        .build();
            }
        });

        buttons.put(11, new ConversationButton() {
            @Override
            public String getInstruction() {
                return "請輸入一個新的顯示名稱";
            }

            @Override
            public BiConsumer<ConversationContext, String> getAction() {
                return (cc, str) -> {
                    rank.setDisplayName(str);
                    Maid.INSTANCE.getRankManager().saveRank(rank);

                    Common.sendMessage(player, CC.GREEN + "成功更改顯示名稱到 " + CC.AQUA + rank.getDisplayName(false));
                    updateMenu();
                };
            }

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.NAME_TAG)
                        .name(CC.AQUA + "更改顯示名稱")
                        .lore("", CC.WHITE + " 現時顯示名稱: " + CC.AQUA + rank.getDisplayName(false), "", CC.YELLOW + "點擊更改")
                        .build();
            }
        });

        buttons.put(12, new ConversationButton() {
            @Override
            public String getInstruction() {
                return "請輸入一個新的十六進位顏色";
            }

            @Override
            public BiConsumer<ConversationContext, String> getAction() {
                return (cc, str) -> {
                    if (!HexColorUtil.isHexColor(str)) {
                        Common.sendMessage(player, CC.RED + "錯誤: '" + str + "' 並不是一個有效的十六進位顏色");
                        return;
                    }

                    rank.setColor(str);
                    Maid.INSTANCE.getRankManager().saveRank(rank);

                    Common.sendMessage(player, CC.GREEN + "成功更改顏色到 " + "<" + rank.getColor() + ">" + rank.getColor());
                    updateMenu();
                };
            }

            @Override
            public ItemStack getButtonItem(Player player) {
                Material wool = Material.valueOf(HexColorUtil.getNearestWoolColor(rank.getColor()).name() + "_WOOL");
                return new ItemBuilder(wool)
                        .name(CC.AQUA + "更改顏色")
                        .lore("", CC.WHITE + " 現時顏色: " + "<" + rank.getColor() + ">" + rank.getColor(), "", CC.YELLOW + "點擊更改")
                        .build();
            }
        });

        buttons.put(13, new ConversationButton() {
            @Override
            public String getInstruction() {
                return "請輸入一個新的十六進位顏色";
            }

            @Override
            public BiConsumer<ConversationContext, String> getAction() {
                return (cc, str) -> {
                    if (!HexColorUtil.isHexColor(str)) {
                        Common.sendMessage(player, CC.RED + "錯誤: '" + str + "' 並不是一個有效的十六進位顏色");
                        return;
                    }

                    rank.setChatColor(str);
                    Maid.INSTANCE.getRankManager().saveRank(rank);

                    Common.sendMessage(player, CC.GREEN + "成功更改聊天顏色到 " + "<" + rank.getChatColor() + ">" + rank.getChatColor());
                    updateMenu();
                };
            }

            @Override
            public ItemStack getButtonItem(Player player) {
                Material dye = Material.valueOf(HexColorUtil.getNearestWoolColor(rank.getChatColor()).name() + "_DYE");
                return new ItemBuilder(dye)
                        .name(CC.AQUA + "更改聊天顏色")
                        .lore("", CC.WHITE + " 現時聊天顏色: " + "<" + rank.getChatColor() + ">" + rank.getChatColor(), "", CC.YELLOW + "點擊更改")
                        .build();
            }
        });

        buttons.put(14, new ConversationButton() {
            @Override
            public String getInstruction() {
                return "請輸入一個新的前綴";
            }

            @Override
            public BiConsumer<ConversationContext, String> getAction() {
                return (cc, str) -> {
                    rank.setPrefix(str);
                    Maid.INSTANCE.getRankManager().saveRank(rank);

                    Common.sendMessage(player, CC.GREEN + "成功更改前綴到 " + CC.AQUA + rank.getPrefix());
                    updateMenu();
                };
            }

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PAINTING)
                        .name(CC.AQUA + "更改前綴")
                        .lore("", CC.WHITE + " 現時前綴: " + CC.GRAY + "'" + CC.AQUA + rank.getPrefix() + CC.RESET + CC.GRAY + "'", "", CC.YELLOW + "點擊更改")
                        .build();
            }
        });

        buttons.put(15, new ConversationButton() {
            @Override
            public String getInstruction() {
                return "請輸入一個新的後綴";
            }

            @Override
            public BiConsumer<ConversationContext, String> getAction() {
                return (cc, str) -> {
                    rank.setSuffix(str);
                    Maid.INSTANCE.getRankManager().saveRank(rank);

                    Common.sendMessage(player, CC.GREEN + "成功更改後綴到 " + CC.AQUA + rank.getSuffix());
                    updateMenu();
                };
            }

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PAINTING)
                        .name(CC.AQUA + "更改後綴")
                        .lore("", CC.WHITE + " 現時後綴: " + CC.GRAY + "'" + CC.AQUA + rank.getSuffix() + CC.RESET + CC.GRAY + "'", "", CC.YELLOW + "點擊更改")
                        .build();
            }
        });

        buttons.put(16, new NumberButton<>(rank) {
            @Override
            public String getName() {
                return CC.AQUA + "更改重量";
            }

            @Override
            public Function<Rank, Integer> read() {
                return Rank::getPriority;
            }

            @Override
            public BiConsumer<Rank, Integer> write() {
                return (rank, priority) -> {
                    rank.setPriority(priority);
                    Maid.INSTANCE.getRankManager().saveRank(rank);

                    Common.sendMessage(player, CC.GREEN + "成功更改重量到 " + CC.AQUA + rank.getPriority());
                    updateMenu();
                };
            }

            @Override
            public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
                super.clicked(event, player, clickType);
                updateMenu();
            }
        });

        buttons.put(21, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.BOOK)
                        .name(CC.AQUA + "更改權限")
                        .lore(
                                "",
                                CC.WHITE + " 現時權限數目: " + CC.AQUA + rank.getPermissions().size(),
                                CC.WHITE + " 現時所有權限數目: " + CC.AQUA + rank.getAllPermissions().size(),
                                "",
                                CC.YELLOW + "點擊更改"
                        )
                        .build();
            }

            @Override
            public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
                new RankPermissionsMenu(player, RankManageMenu.this, rank).updateMenu();
            }
        });

        buttons.put(22, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.EMERALD)
                        .name(CC.AQUA + "更改父職階")
                        .lore("", CC.WHITE + " 現時父職階數目: " + CC.AQUA + rank.getParents().size(), "", CC.YELLOW + "點擊更改")
                        .build();
            }

            @Override
            public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
                new RankParentsMenu(player, RankManageMenu.this, rank).updateMenu();
            }
        });

        buttons.put(23, new ToggleButton<>(WordUtil.translate(true), WordUtil.translate(false), rank) {
            @Override
            public String getName() {
                return CC.AQUA + "更改預設職階狀態";
            }

            @Override
            public Function<Rank, Boolean> read() {
                return Rank::isDefault;
            }

            @Override
            public BiConsumer<Rank, Boolean> write() {
                return (rank, state) -> {
                    if (Maid.INSTANCE.getRankManager().getRanks().values().stream().filter(IRank::isDefault).count() == 1 && !state) {
                        Common.sendMessage(player, CC.RED + "錯誤: 你必須要把其他職階的預設職階狀態開啟才能關閉這個狀態");
                        return;
                    }
                    rank.setDefault(state);
                    Maid.INSTANCE.getRankManager().saveRank(rank);

                    Common.sendMessage(player, CC.GREEN + "成功更改預設職階狀態到 " + CC.AQUA + WordUtil.translate(rank.isDefault()));
                    updateMenu();
                };
            }
        });

        buttons.put(40, new BackButton(backMenu));

        return buttons;
    }
}
