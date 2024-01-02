package rip.diamond.maid.util.menu.buttons;

import lombok.Getter;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@Getter
public abstract class Button {

    private String id;

    public abstract ItemStack getButtonItem(Player player);

    public abstract void clicked(InventoryClickEvent event, Player player, ClickType clickType);

    public Sound sound() {
        return Sound.sound(org.bukkit.Sound.UI_BUTTON_CLICK.getKey(), Sound.Source.MASTER, 1f, 1f);
    }

    /**
     * Set the ID of this button
     * This ID can be used to identify conditions, and can be empty if ID is not needed
     *
     * @param id The ID of the button
     * @return the current button instance
     */
    public Button setID(String id) {
        this.id = id;
        return this;
    }

}
