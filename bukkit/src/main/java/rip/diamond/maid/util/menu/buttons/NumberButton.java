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
public abstract class NumberButton<T> extends Button {

    private final T target;

    public abstract String getName();

    public abstract Function<T, Integer> read();

    public abstract BiConsumer<T, Integer> write();

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.GOLD_NUGGET)
                .name(getName())
                .lore(
                        "",
                        CC.WHITE + "現時數量: " + CC.AQUA + readAmount(),
                        "",
                        CC.YELLOW + "點擊左鍵提升數值",
                        CC.YELLOW + "點擊右鍵降低數值"
                )
                .build();
    }

    @Override
    public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
        int current = readAmount();
        int change = clickType.isShiftClick() ? 10 : 1;

        write().accept(target, current + (clickType.isRightClick() ? -change : change));
    }

    private int readAmount() {
        return read().apply(target);
    }
}
