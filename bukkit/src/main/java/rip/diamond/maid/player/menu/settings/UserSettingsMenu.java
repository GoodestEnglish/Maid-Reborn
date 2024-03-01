package rip.diamond.maid.player.menu.settings;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.api.user.UserSettings;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.menu.Menu;
import rip.diamond.maid.util.menu.MenuType;
import rip.diamond.maid.util.menu.buttons.Button;
import rip.diamond.maid.util.menu.buttons.ChooseButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class UserSettingsMenu extends Menu {

    private final IUser user;

    public UserSettingsMenu(Player player) {
        super(player, -1);
        this.user = Maid.INSTANCE.getUserManager().getUserNow(player.getUniqueId());;
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.INVENTORY;
    }

    @Override
    public String getTitle() {
        return "設定一覽";
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();
        for (UserSettings settings : UserSettings.values()) {
            if (settings.getPermission() == null || player.hasPermission(settings.getPermission())) {
                buttons.put(buttons.size(), new ChooseButton<>(user) {
                    @Override
                    public String getName() {
                        return CC.AQUA + settings.getName();
                    }

                    @Override
                    public Material getIcon() {
                        return Material.valueOf(settings.getIcon());
                    }

                    @Override
                    public Function<IUser, String> read() {
                        return u -> {
                            String value = u.getSettings().get(settings);
                            if (value == null) {
                                return settings.getDefaultOption();
                            }
                            return value;
                        };
                    }

                    @Override
                    public BiConsumer<IUser, String> write() {
                        return (u, option) -> {
                            u.getSettings().put(settings, option);
                            updateMenu();
                        };
                    }

                    @Override
                    public List<String> getOptions() {
                        return settings.getOptions();
                    }

                    @Override
                    public List<String> getLore() {
                        return settings.getDescription().stream().map(str -> CC.GRAY + str).toList();
                    }
                });
            }
        }
        return buttons;
    }

    @Override
    public void close(Player player, InventoryCloseEvent event) {
        Maid.INSTANCE.getUserManager().saveUser(user);
    }
}
