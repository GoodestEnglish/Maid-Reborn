package rip.diamond.maid.util.json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import rip.diamond.maid.util.json.adapter.LongTypeAdapter;
import rip.diamond.maid.util.json.adapter.UUIDTypeAdapter;

import java.util.UUID;

public class GsonProvider {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
            .registerTypeAdapter(Long.class, new LongTypeAdapter())
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .setPrettyPrinting()
            .serializeNulls()
            .setLenient()
            .create();
}
