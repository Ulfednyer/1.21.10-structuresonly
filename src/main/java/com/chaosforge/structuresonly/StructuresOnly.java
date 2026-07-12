package com.chaosforge.structuresonly;

import com.chaosforge.structuresonly.config.ModConfig;
import com.chaosforge.structuresonly.world.ChestWorldSavedData;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;
import java.util.Map;
import java.util.Set;
import com.chaosforge.structuresonly.util.INaturallyGenerated;

public class StructuresOnly implements ModInitializer {
    public static final String MOD_ID = "structuresonly";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Thread-local flag to identify block placement during structure generation
    public static final ThreadLocal<Boolean> IS_GENERATING_STRUCTURE = ThreadLocal.withInitial(() -> false);

    // Loot tables pool for lucky chest drops
    private static final List<ResourceKey<LootTable>> LOOT_TABLES = List.of(
        BuiltInLootTables.SIMPLE_DUNGEON,
        BuiltInLootTables.ABANDONED_MINESHAFT,
        BuiltInLootTables.DESERT_PYRAMID,
        BuiltInLootTables.JUNGLE_TEMPLE,
        BuiltInLootTables.END_CITY_TREASURE,
        BuiltInLootTables.WOODLAND_MANSION,
        BuiltInLootTables.NETHER_BRIDGE,
        BuiltInLootTables.BASTION_TREASURE,
        BuiltInLootTables.SPAWN_BONUS_CHEST,
        BuiltInLootTables.UNDERWATER_RUIN_BIG,
        BuiltInLootTables.TRIAL_CHAMBERS_REWARD,
        BuiltInLootTables.TRIAL_CHAMBERS_REWARD_COMMON,
        BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS
    );

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Structures Only mod...");

        // Load configuration
        ModConfig.load();

        // Register player join event for starter platform & chest spawn
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.getPlayer();
            ServerLevel world = player.level();
            if (world.dimension() == Level.OVERWORLD) {
                ChestWorldSavedData data = world.getDataStorage().computeIfAbsent(ChestWorldSavedData.TYPE);
                if (!data.starterChestSpawned && ModConfig.get().spawnStarterChest) {
                    spawnStarterPlatformAndChest(world);
                    data.starterChestSpawned = true;
                    data.setDirty();

                    // Teleport the player onto the platform immediately on first world load
                    player.teleportTo(world, 0.5, 65.0, 0.5, Set.of(), 0.0f, 0.0f, false);
                }
            }
        });

        // AFTER: Drop lucky bonus loot for naturally generated chests
        // Vanilla handles dropping the chest block itself and its stored contents/structure loot
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (!world.isClientSide() && isChestOrLootBlock(state)) {
                if (blockEntity instanceof INaturallyGenerated gen && gen.structuresonly$isNaturallyGenerated()) {
                    ServerLevel serverLevel = (ServerLevel) world;

                    if (ModConfig.get().luckyChestDrops) {
                        dropLuckyLoot(serverLevel, pos, player);
                    }
                }
            }
        });
    }

    public static boolean isChestOrLootBlock(BlockState state) {
        Block block = state.getBlock();
        return block == Blocks.CHEST
            || block == Blocks.TRAPPED_CHEST
            || block == Blocks.BARREL
            || block == Blocks.DISPENSER
            || block == Blocks.DROPPER
            || block == Blocks.SPAWNER
            || block == Blocks.TRIAL_SPAWNER
            || block == Blocks.VAULT
            || block instanceof ShulkerBoxBlock;
    }

    private static void spawnStarterPlatformAndChest(ServerLevel world) {
        LOGGER.info("Spawning starter platform and chest at (0, 64, 0)...");

        // Spawn a 3x3 dirt platform
        BlockPos center = new BlockPos(0, 64, 0);
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                world.setBlockAndUpdate(center.offset(x, 0, z), Blocks.DIRT.defaultBlockState());
            }
        }

        // Spawn starter chest in the center of the platform
        BlockPos chestPos = new BlockPos(0, 65, 0);
        world.setBlockAndUpdate(chestPos, Blocks.CHEST.defaultBlockState());

        // Populate the starter chest with items
        BlockEntity blockEntity = world.getBlockEntity(chestPos);
        if (blockEntity instanceof ChestBlockEntity chest) {
            chest.clearContent();
            chest.setItem(0, new ItemStack(Items.OAK_SAPLING, 1));
            chest.setItem(1, new ItemStack(Items.DIRT, 4));
            chest.setItem(2, new ItemStack(Items.WATER_BUCKET, 1));
            chest.setItem(3, new ItemStack(Items.LAVA_BUCKET, 1));
            chest.setItem(4, new ItemStack(Items.IRON_AXE, 1));
            chest.setItem(5, new ItemStack(Items.STONE_PICKAXE, 1));
            chest.setItem(6, new ItemStack(Items.BREAD, 16));
        }

        // Set default world spawn position to the platform
        world.setRespawnData(LevelData.RespawnData.of(Level.OVERWORLD, chestPos, 0.0f, 0.0f));
    }

    private static void dropLuckyLoot(ServerLevel world, BlockPos pos, net.minecraft.world.entity.player.Player player) {
        try {
            RandomSource random = world.getRandom();
            ResourceKey<LootTable> lootTableKey = LOOT_TABLES.get(random.nextInt(LOOT_TABLES.size()));

            // CHEST param set only accepts ORIGIN (required) and THIS_ENTITY (optional)
            LootParams params = new LootParams.Builder(world)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withLuck(player.getLuck())
                .create(LootContextParamSets.CHEST);

            LootTable lootTable = world.getServer().reloadableRegistries().getLootTable(lootTableKey);
            ObjectArrayList<ItemStack> items = lootTable.getRandomItems(params);

            for (ItemStack item : items) {
                Block.popResource(world, pos, item);
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to generate lucky loot: {}", e.getMessage());
        }
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
