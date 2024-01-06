package rip.diamond.maid.rank.menu;

import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.rank.Rank;
import rip.diamond.maid.redis.messaging.PacketHandler;
import rip.diamond.maid.redis.packets.bukkit.PermissionUpdatePacket;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.HeadUtil;
import rip.diamond.maid.util.ItemBuilder;
import rip.diamond.maid.util.menu.Menu;
import rip.diamond.maid.util.menu.MenuType;
import rip.diamond.maid.util.menu.buttons.Button;
import rip.diamond.maid.util.menu.buttons.ConversationButton;
import rip.diamond.maid.util.menu.pagination.PaginatedMenu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class RankParentsMenu extends PaginatedMenu {

    private final Rank rank;

    public RankParentsMenu(Player player, Menu backMenu, Rank rank) {
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
        for (UUID parentUUID : rank.getParents()) {
            buttons.put(buttons.size(), new Button() {
                final IRank parent = Maid.INSTANCE.getRankManager().getRanks().get(parentUUID);
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(Material.EMERALD)
                            .name("<" + parent.getColor() + ">" + parent.getName())
                            .lore(
                                    "",
                                    CC.YELLOW + "點擊移除該父職階"
                            )
                            .build();
                }

                @Override
                public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
                    rank.removeParent(parent.getUniqueID());
                    Maid.INSTANCE.getRankManager().saveRank(rank);
                    PacketHandler.send(new PermissionUpdatePacket(MaidAPI.INSTANCE.getPlatform().getServerID()));

                    updateMenu();
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
                return "請輸入你想新增的父職階";
            }

            @Override
            public BiConsumer<ConversationContext, String> getAction() {
                return ((cc, name) -> {
                    if (rank.getName().equalsIgnoreCase(name)) {
                        Common.sendMessage(player, CC.RED + "錯誤: 不能新增當前的職階到父職階");
                        return;
                    }
                    IRank parent = Maid.INSTANCE.getRankManager().getRank(name);
                    if (parent == null) {
                        Common.sendMessage(player, CC.RED + "錯誤: 未能找到該父職階");
                        return;
                    }
                    if (rank.containParent(parent.getUniqueID())) {
                        Common.sendMessage(player, CC.RED + "錯誤: 父職階已存在");
                        return;
                    }
                    if (rank.getPriority() < parent.getPriority()) {
                        Common.sendMessage(player, CC.RED + "錯誤: 當前的職階重量少於父職階");
                        return;
                    }
                    rank.addParent(parent.getUniqueID());
                    Maid.INSTANCE.getRankManager().saveRank(rank);
                    PacketHandler.send(new PermissionUpdatePacket(MaidAPI.INSTANCE.getPlatform().getServerID()));

                    Common.sendMessage(player, CC.GREEN + "成功新增父職階 " + CC.AQUA + parent.getName());
                    updateMenu();
                });
            }

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PLAYER_HEAD)
                        .texture(HeadUtil.LIME_PLUS.get())
                        .name(CC.AQUA + "新增父職階")
                        .lore("", CC.WHITE + "現時父職階數目: " + CC.AQUA + rank.getParents().size(), "", CC.YELLOW + "點擊新增父職階")
                        .build();
            }
        });
        return buttons;
    }

    @Override
    public String getPaginatedTitle() {
        return "職階父職階 - " + rank.getDisplayName();
    }
}
