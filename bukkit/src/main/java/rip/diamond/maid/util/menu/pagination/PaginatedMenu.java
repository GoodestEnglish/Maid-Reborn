package rip.diamond.maid.util.menu.pagination;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.ItemBuilder;
import rip.diamond.maid.util.menu.Menu;
import rip.diamond.maid.util.menu.buttons.Button;

import java.util.Map;

@Getter
@Setter
public abstract class PaginatedMenu extends Menu {

    private NavigationPosition navigationPosition = NavigationPosition.BOTTOM;

    private ItemStack previousPageItem = new ItemBuilder(Material.ARROW)
            .name(CC.LIME_GREEN + "上一頁")
            .lore(CC.GRAY + "點擊這裏跳轉到上一頁")
            .build();
    private ItemStack nextPageItem = new ItemBuilder(Material.ARROW)
            .name(CC.LIME_GREEN + "下一頁")
            .lore(CC.GRAY + "點擊這裏跳轉到下一頁")
            .build();

    private int page = 1;
    private int previousPage = 1;
    private PaginationAction paginationAction = PaginationAction.NEXT;

    private final Menu backMenu;

    public PaginatedMenu(Player player, int size) {
        super(player, size);
        this.backMenu = null;
    }

    public PaginatedMenu(Player player, int size, Menu backMenu) {
        super(player, size);
        this.backMenu = backMenu;
    }

    public abstract String getPaginatedTitle();

    @Override
    public String getTitle() {
        return getPaginatedTitle() + " [" + page + "/" + getMaxPages() + "]";
    }

    /**
     * Navigate to the next menu page
     */
    public void navigateNext() {
        this.paginationAction = PaginationAction.NEXT;
        this.previousPage = page;

        if (hasNextPage()) {
            this.page += 1;
        }
        this.updateMenu();
    }

    /**
     * Navigate to the previous menu page
     */
    public void navigatePrevious() {
        this.paginationAction = PaginationAction.PREVIOUS;
        this.previousPage = page;

        if (hasPreviousPage()) {
            this.page -= 1;
        }
        this.updateMenu();
    }

    /**
     * Update the menu for the player
     */
    @Override
    public void updateMenu() {
        this.updateMenu(this.getButtonsInRange());
    }

    /**
     * Handle clicking on a button
     *
     * @param event the event called
     */
    @Override
    public void click(InventoryClickEvent event) {
        event.setCancelled(true);

        if (event.getClickedInventory() instanceof PlayerInventory) {
            getPlayerInventoryClickAction().accept(event);
            return;
        }

        final Map<Integer, Button> buttons = this.getButtonsInRange();
        final Button button = buttons.get(event.getSlot());

        if (button == null) {
            emptyClickAction.accept(event);
            return;
        }

        button.clicked(event, (Player) event.getWhoClicked(), event.getClick());
        event.getWhoClicked().playSound(button.sound());
    }

    /**
     * Get the list of buttons in the
     * range of the current page.
     *
     * @return the list of buttons
     */
    public Map<Integer, Button> getButtonsInRange() {
        return this.navigationPosition.getButtonsInRange(this.getButtons(), this);
    }

    /**
     * Get the list of buttons for the navigation bar.
     * <p>
     * These buttons will be displayed independent
     * of the current page of the menu.
     *
     * @return the list of buttons
     */
    public Map<Integer, Button> getNavigationBar() {
        return this.navigationPosition.getNavigationButtons(this);
    }

    /**
     * Get the maximum buttons the menu can be displayed
     *
     * @return the maximum number of buttons
     */
    public int getMaxItemPerPage() {
        return getSize() - 9;
    }

    public int getMaxPages() {
        int buttonAmount = getButtons().size();

        //至少要有一頁
        if (buttonAmount == 0) {
            return 1;
        }

        return (int) Math.ceil(buttonAmount / (double) getMaxItemPerPage());
    }

    /**
     * @return If the menu has previous page or not
     */
    public boolean hasPreviousPage() {
        return page > 1;
    }

    /**
     * @return If the menu has next page or not
     */
    public boolean hasNextPage() {
        return page < getMaxPages();
    }
}