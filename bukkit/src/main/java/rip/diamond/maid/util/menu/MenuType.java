package rip.diamond.maid.util.menu;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public enum MenuType {

    HOPPER {
        @Override
        public Inventory createInventory(Menu menu) {
            return Bukkit.createInventory(null, InventoryType.HOPPER, menu.title());
        }
    },

    INVENTORY {
        @Override
        public Inventory createInventory(Menu menu) {
            return Bukkit.createInventory(null, menu.getSize(), menu.title());
        }
    },

    FURNACE {
        @Override
        public Inventory createInventory(Menu menu) {
            return Bukkit.createInventory(null, InventoryType.FURNACE, menu.title());
        }
    },

    BREWING_STAND {
        @Override
        public Inventory createInventory(Menu menu) {
            return Bukkit.createInventory(null, InventoryType.BREWING, menu.title());
        }
    },

    ENCHANTING {
        @Override
        public Inventory createInventory(Menu menu) {
            return Bukkit.createInventory(null, InventoryType.ENCHANTING, menu.title());
        }
    },

    BEACON {
        @Override
        public Inventory createInventory(Menu menu) {
            return Bukkit.createInventory(null, InventoryType.BEACON, menu.title());
        }
    },

    CRAFTING {
        @Override
        public Inventory createInventory(Menu menu) {
            return Bukkit.createInventory(null, InventoryType.CRAFTING, menu.title());
        }
    },

    DISPENSER {
        @Override
        public Inventory createInventory(Menu menu) {
            return Bukkit.createInventory(null, InventoryType.DISPENSER, menu.title());
        }
    },

    DROPPER {
        @Override
        public Inventory createInventory(Menu menu) {
            return Bukkit.createInventory(null, InventoryType.DROPPER, menu.title());
        }
    },

    ANVIL {
        @Override
        public Inventory createInventory(Menu menu) {
            return Bukkit.createInventory(null, InventoryType.ANVIL, menu.title());
        }
    },

    SMITHING {
        @Override
        public Inventory createInventory(Menu menu) {
            return Bukkit.createInventory(null, InventoryType.SMITHING, menu.title());
        }
    };

    /**
     * Create a new inventory with the menu type
     *
     * @param menu the menu to create it for
     * @return the inventory
     */
    public abstract Inventory createInventory(Menu menu);

}
