package rip.diamond.maid.util.procedure;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;

public class ProcedureListener implements Listener{

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        Procedure procedure = Procedure.getProcedures().get(player.getUniqueId());
        if (procedure == null) {
            return;
        }

        event.setCancelled(true);

        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        if (message.equalsIgnoreCase("cancel")) {
            procedure.remove();
            Common.sendMessage(player, CC.GREEN + "成功取消程序");
            return;
        }
        procedure.call(message);
    }

}
