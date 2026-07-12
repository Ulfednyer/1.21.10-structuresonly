package com.chaosforge.structuresonly.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class ChestWorldSavedData extends SavedData {
    public boolean starterChestSpawned = false;

    // Codec for serialization
    private static final Codec<ChestWorldSavedData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.BOOL.fieldOf("starterChestSpawned").forGetter(data -> data.starterChestSpawned)
        ).apply(instance, starterChestSpawned -> {
            ChestWorldSavedData data = new ChestWorldSavedData();
            data.starterChestSpawned = starterChestSpawned;
            return data;
        })
    );

    // SavedDataType used by DimensionDataStorage.computeIfAbsent
    public static final SavedDataType<ChestWorldSavedData> TYPE = new SavedDataType<>(
        "structuresonly_data",
        ChestWorldSavedData::new,
        CODEC,
        DataFixTypes.LEVEL
    );

    public ChestWorldSavedData() {
    }
}
