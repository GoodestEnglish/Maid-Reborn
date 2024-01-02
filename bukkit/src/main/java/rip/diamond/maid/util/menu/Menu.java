package rip.diamond.maid.util.menu;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.menu.buttons.Button;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Getter
@Setter
public abstract class Menu {


    protected final Player player;
    protected final int size;

    private boolean updated;

    // the inventory to use if the inventory already exists,
    // to avoid re-opening the inventory whenever updating.
    protected Inventory inventory;

    //當該欄位沒有Button, 這個就會觸發
    protected Consumer<InventoryClickEvent> emptyClickAction = event -> {};

    public Menu(Player player, int size) {
        this.player = player;
        this.size = size;

        this.registerMenu();
    }

    /**
     * Updates the buttons
     */

    public void updateMenu() {
        this.updateMenu(getButtons());
    }

    /**
     * Updates the buttons specified
     *
     * @param buttonMap the integer/button map
     */

    public void updateMenu(Map<Integer, Button> buttonMap) {
        if (Bukkit.isPrimaryThread()) {
            throw new RuntimeException("Cannot update the menu async");
        }

        final Inventory inventory = getMenuType().createInventory(this);

        if (buttonMap != null) {
            buttonMap.keySet().forEach(integer -> inventory.setItem(integer, buttonMap.get(integer).getButtonItem(player)));
        }

        //儲存剛剛創建的 Inventory Instance
        //原因不一開始創建 Inventory Instance 的時候就儲存是要避免如果在設置 buttons 的時候報錯, 那麼玩家顯示的 Inventory 將會停留在以前的 Inventory, 但是 this.inventory 已經代表著另外一個的 Inventory
        this.inventory = inventory;

        if (player.getOpenInventory().title().equals(title()) && player.getOpenInventory().getTopInventory().getSize() == getSize()) {
            player.getOpenInventory().getTopInventory().setStorageContents(inventory.getContents());
            return;
        }

        player.openInventory(inventory);
        this.registerMenu();
    }

    public abstract MenuType getMenuType();

    /**
     * The method to get the title
     */
    public abstract String getTitle();

    public Component title() {
        return Common.text(getTitle());
    }

    /**
     * The method to get the buttons for the current inventory tick
     */
    public abstract Map<Integer, Button> getButtons();

    /**
     * Register the menu to the menu handler
     */
    public void registerMenu() {
        MenuHandler.getInstance().register(this.player, this);
    }

    /**
     * Redirect the player's menu to a new menu
     *
     * @param menu the menu to redirect it to
     */
    public void redirect(Menu menu) {
        menu.updateMenu();
        this.registerMenu();
    }

    public Optional<Button> getButton(int slot) {
        final Map<Integer, Button> buttons = getButtons();
        if (buttons == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(buttons.get(slot));
    }

    /**
     * Handle clicking on a button
     *
     * @param event the event called
     */
    public void click(InventoryClickEvent event) {
        event.setCancelled(true);

        if (event.getClickedInventory() instanceof PlayerInventory) {
            getPlayerInventoryClickAction().accept(event);
            return;
        }

        final Button button = getButtons().get(event.getSlot());
        if (button == null) {
            emptyClickAction.accept(event);
            return;
        }

        button.clicked(event, (Player) event.getWhoClicked(), event.getClick());
        event.getWhoClicked().playSound(button.sound());
    }

    /**
     * Handle the player closing the inventory
     *
     * @param event the event called
     */
    public void handleClose(InventoryCloseEvent event) {
        this.updated = false;
        MenuHandler.getInstance().unregister((Player) event.getPlayer());
    }

    /**
     * Handles the closing of the inventory
     * This method should let other class to override
     *
     * @param player player closing the menu
     * @param event even called
     */
    public void close(Player player, InventoryCloseEvent event) {
    }

    /**
     * Called when the player clicks on PlayerInventory
     * This method should let other class to override
     */
    public Consumer<InventoryClickEvent> getPlayerInventoryClickAction() {
        return event -> {};
    }

}