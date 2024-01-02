package rip.diamond.maid.util.json.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;

public class LongTypeAdapter implements JsonSerializer<Long>, JsonDeserializer<Long> {
    @Override
    public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Long.parseLong(json.getAsString());
    }
}
