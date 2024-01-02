package rip.diamond.maid.util.menu.buttons;

import org.bukkit.Material;
import rip.diamond.maid.util.ItemBuilder;

public class PlaceholderButton extends DisplayButton {
    public PlaceholderButton() {
        super(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build());
    }
}
