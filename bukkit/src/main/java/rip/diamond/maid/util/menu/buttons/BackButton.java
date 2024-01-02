package rip.diamond.maid.util.menu.buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.ItemBuilder;
import rip.diamond.maid.util.menu.Menu;

public class BackButton extends Button {

	private final Menu backMenu;

	public BackButton(Menu backMenu) {
		this.backMenu = backMenu;
	}

	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name(CC.RED + "返回").lore(CC.GRAY + "點擊回到上一頁").build();
	}

	@Override
	public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
		backMenu.updateMenu();
	}
}
