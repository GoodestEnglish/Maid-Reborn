package rip.diamond.maid.disguise;

import lombok.*;
import rip.diamond.maid.api.user.IDisguise;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Disguise implements IDisguise {

    private final String name;
    private final UUID uniqueID;
    private final String skinValue;
    private final String skinSignature;
    private final UUID disguiseRankUUID;
}
