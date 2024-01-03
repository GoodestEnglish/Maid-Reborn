package rip.diamond.maid.util.menu.pagination;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import rip.diamond.maid.util.menu.buttons.BackButton;
import rip.diamond.maid.util.menu.buttons.Button;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public enum NavigationPosition {

    BOTTOM {
        /**
         * Get the navigation buttons
         * for the position type.
         *
         * @return the buttons
         */
        @Override
        public Map<Integer, Button> getNavigationButtons(PaginatedMenu menu) {
            final Map<Integer, Button> map = new HashMap<>();

            map.put(menu.getSize() - 9, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return menu.getPreviousPageItem();
                }

                @Override
                public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
                    menu.navigatePrevious();
                    event.setCancelled(true);
                }

                @Override
                public Sound sound() {
                    return Sound.sound(org.bukkit.Sound.ITEM_BOOK_PAGE_TURN.getKey(), Sound.Source.MASTER, 1f, 1f);
                }
            });

            map.put(menu.getSize() - 1, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return menu.getNextPageItem();
                }

                @Override
                public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
                    menu.navigateNext();
                    event.setCancelled(true);
                }

                @Override
                public Sound sound() {
                    return Sound.sound(org.bukkit.Sound.ITEM_BOOK_PAGE_TURN.getKey(), Sound.Source.MASTER, 1f, 1f);
                }
            });

            if (menu.getBackMenu() != null) {
                map.put(menu.getSize() - 4, new BackButton(menu.getBackMenu()));
            }

            return map;
        }

        /**
         * Get a list of buttons in the range of the
         * current menu's page.
         *
         * @param buttons the list of buttons to get the buttons in range from
         * @param menu    the menu to get the data from
         * @return the buttons in range
         */
        @Override
        public Map<Integer, Button> getButtonsInRange(Map<Integer, Button> buttons, PaginatedMenu menu) {
            final Map<Integer, Button> map = new HashMap<>();

            final int size = menu.getSize();
            final int page = menu.getPage();

            final int maxElements = menu.getMaxItemPerPage();

            final int start = (page - 1) * maxElements;
            final int end = (start + maxElements) - 1;

            for (int index = 0; index < buttons.size(); index++) {
                final Button button = buttons.get(index);

                if (button != null && index >= start && index <= end) {
                    map.put(index - ((maxElements) * (page - 1)), button);
                }
            }

            final Map<Integer, Button> navigationBar = menu.getNavigationBar();
            for (Map.Entry<Integer, Button> entry : navigationBar.entrySet()) {
                final int index = entry.getKey();
                final Button button = entry.getValue();

                if (button != null) {
                    map.put(index, button);
                }
            }

            return map;
        }
    };

    /**
     * Get the navigation buttons
     * for the position type.
     *
     * @param menu the menu to get the data from
     * @return the buttons
     */
    public abstract Map<Integer, Button> getNavigationButtons(PaginatedMenu menu);

    /**
     * Get a list of buttons in the range of the
     * current menu's page.
     *
     * @param buttons the list of buttons to get the buttons in range from
     * @param menu    the menu to get the data from
     * @return the buttons in range
     */
    public abstract Map<Integer, Button> getButtonsInRange(Map<Integer, Button> buttons, PaginatedMenu menu);

}