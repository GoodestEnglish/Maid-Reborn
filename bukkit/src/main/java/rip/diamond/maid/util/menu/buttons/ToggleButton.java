package rip.diamond.maid.util.menu.buttons;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.ItemBuilder;

import java.util.function.BiConsumer;
import java.util.function.Function;

@RequiredArgsConstructor
public abstract class ToggleButton<T> extends Button {

    private final String enabled;
    private final String disabled;
    private final T target;

    public abstract String getName();

    public abstract Function<T, Boolean> read();

    public abstract BiConsumer<T, Boolean> write();

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(isEnabled() ? Material.REDSTONE_TORCH : Material.LEVER)
                .name(CC.AQUA + getName())
                .lore(
                        "",
                        (isEnabled() ? CC.GREEN + " » " : CC.GRAY + "   ") + enabled,
                        (!isEnabled() ? CC.GREEN + " » " : CC.GRAY + "   ") + disabled,
                        "",
                        CC.YELLOW + "點擊循環選項"
                )
                .build();
    }

    @Override
    public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
        write().accept(target, !isEnabled());
    }

    private boolean isEnabled() {
        return read().apply(target);
    }
}
