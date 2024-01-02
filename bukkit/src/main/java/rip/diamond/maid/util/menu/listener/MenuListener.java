package rip.diamond.maid.util.menu.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import rip.diamond.maid.util.menu.Menu;
import rip.diamond.maid.util.menu.MenuHandler;

import java.util.Optional;

@RequiredArgsConstructor
public class MenuListener implements Listener {

    private final MenuHandler menuHandler;

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final Optional<Menu> optional = this.menuHandler.findMenu(player);

        if (optional.isEmpty()) {
            return;
        }

        Menu menu = optional.get();
        menu.click(event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();
        final Optional<Menu> optional = this.menuHandler.findMenu(player);

        if (optional.isEmpty()) {
            return;
        }

        Menu menu = optional.get();
        menu.close(player, event);
        menu.handleClose(event);
    }
}