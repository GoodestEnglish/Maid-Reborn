package rip.diamond.maid.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Getter
@RequiredArgsConstructor
public class PlayerUndisguiseEvent extends BaseEvent {

    private final Player player;

}
