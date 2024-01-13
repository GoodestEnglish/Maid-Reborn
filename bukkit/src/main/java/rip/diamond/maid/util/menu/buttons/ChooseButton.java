package rip.diamond.maid.util.menu.buttons;

import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.ItemBuilder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import java.util.function.Function;

@RequiredArgsConstructor
public abstract class ChooseButton<T> extends Button {

    private final T target;

    public abstract String getName();

    public abstract Material getIcon();

    public abstract Function<T, String> read();

    public abstract BiConsumer<T, String> write();

    public abstract List<String> getOptions();

    public List<String> getLore() {
        return ImmutableList.of("");
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(getIcon()).name(getName());
        builder.lore(getLore());
        builder.lore(getOptions().stream().map(option -> (matches(option) ? CC.GREEN + " » " : CC.GRAY + "   ") + option).toList());
        builder.lore("", CC.YELLOW + "點擊循環選項");

        return builder.build();
    }

    @Override
    public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
        String read = read().apply(target);
        int size = getOptions().size();
        int current = getOptions().indexOf(read);
        if (current == -1) {
            throw new NoSuchElementException("Cannot find the index of '" + read + "'");
        }
        int next = (current + 1) % size;
        write().accept(target, getOptions().get(next));
    }

    private boolean matches(String value) {
        String read = read().apply(target);
        return read.equals(value);
    }
}
