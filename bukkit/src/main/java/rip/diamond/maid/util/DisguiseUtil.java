package rip.diamond.maid.util;

import java.lang.reflect.Field;

public class DisguiseUtil {

    public static final Class<?> CRAFT_PLAYER_CLASS;
    public static final Class<?> GAME_PROFILE_CLASS;
    public static final Field GAME_PROFILE_NAME_FIELD;

    static {
        try {
            CRAFT_PLAYER_CLASS = CraftBukkitImplementation.obcClass("entity.CraftPlayer");
            GAME_PROFILE_CLASS = Class.forName("com.mojang.authlib.GameProfile");
            GAME_PROFILE_NAME_FIELD = GAME_PROFILE_CLASS.getDeclaredField("name");
            GAME_PROFILE_NAME_FIELD.setAccessible(true);
        } catch (NoSuchFieldException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
