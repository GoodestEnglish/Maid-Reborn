package rip.diamond.maid.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.diamond.maid.api.user.IDisguise;

@Getter
@RequiredArgsConstructor
public class PlayerDisguiseEvent extends BaseEvent {

    private final Player player;
    private final IDisguise disguise;

}
