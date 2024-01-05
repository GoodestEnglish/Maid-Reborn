package rip.diamond.maid.disguise;

import lombok.*;
import rip.diamond.maid.api.user.IDisguise;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Disguise implements IDisguise {

    private final String name;
    private final String skinName;
    private final UUID disguiseRankUUID;
}
