package rip.diamond.maid.util.menu.buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.ItemBuilder;

public abstract class ToggleButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(isEnabled(player) ? Material.REDSTONE_TORCH : Material.LEVER)
                .name(CC.AQUA + getOptionName())
                .lore("", CC.GRAY + getDescription(), "", CC.GREEN + (isEnabled(player) ? " » " : "   ") + CC.GREEN + "開啟", CC.RED + (!isEnabled(player) ? " » " : "   ") + "關閉", "")
                .build();
    }

    public abstract String getOptionName();

    public abstract String getDescription();

    public abstract boolean isEnabled(Player player);

    @Override
    public abstract void clicked(InventoryClickEvent event, Player player, ClickType clickType);
}
