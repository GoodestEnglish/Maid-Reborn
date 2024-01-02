package rip.diamond.maid.util.procedure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import rip.diamond.maid.util.Common;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class Procedure {

    private static boolean finished = false;
    @Getter private static final Map<UUID, Procedure> procedures = new HashMap<>();

    private final UUID uuid;
    private final Consumer<String> callback;

    public static void init(JavaPlugin plugin) {
        Validate.isTrue(!finished, "Procedure is already initialized!");

        plugin.getServer().getPluginManager().registerEvents(new ProcedureListener(), plugin);

        finished = true;
    }

    public static Procedure buildProcedure(Player player, String instructions, Consumer<String> callback) {
        Common.sendMessage(player, "<yellow>" + instructions, "<yellow>輸入 <red><bold>cancel <!bold><yellow>去強制終止過程");

        Procedure procedure = new Procedure(player.getUniqueId(), callback);
        if (procedures.containsKey(player.getUniqueId())) {
            procedures.replace(player.getUniqueId(), procedure);
        } else {
            procedures.put(player.getUniqueId(), procedure);
        }

        return procedure;
    }

    public void call(String o) {
        remove();
        callback.accept(o);
    }

    public void remove() {
        procedures.remove(uuid);
    }

}