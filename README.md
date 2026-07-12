# Structures Only (Fabric Mod)

[![Platform](https://img.shields.io/badge/Minecraft-1.21.10-blue.svg)](https://minecraft.net/)
[![Loader](https://img.shields.io/badge/Loader-Fabric-lightgrey.svg)](https://fabricmc.net/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

**Structures Only** is a gameplay and terrain modification mod for Minecraft 1.21.10 using the Fabric toolchain. It transforms the world into a completely empty void where only naturally generated structures (such as trial chambers, strongholds, mansions, nether fortresses, bastions, ocean monuments, and dungeons) are retained. It is designed to create a challenging survival experience where players must gather resources solely by exploring generated structures.

---

## Features

- **Void World Structure-Only Generation**: All standard terrain generation (stone, dirt, grass, oceans) is completely removed, leaving only the naturally generated structures floating in a vast void.
- **Starter Platform**: Automatically spawns a 3x3 dirt platform and a starter chest at `(0, 64, 0)` filled with essential survival items (sapling, lava/water buckets, bread, basic tools) on first joining.
- **Lucky Chest Breaks**: Breaking naturally generated containers (chests, barrels, dispensers, spawners, vaults, shulker boxes, etc.) triggers a lucky roll, dropping additional high-quality items from standard Minecraft loot tables (e.g., Trial Chambers, Bastions, End Cities, Desert Pyramids).
- **Loyalty Trident Void Protection**: Any thrown trident enchanted with **Loyalty** that falls below the map threshold (into the void) will safely teleport back to the player's inventory/hand instead of being deleted.
- **Balanced Trident Crafting**: Adds a custom shaped recipe for crafting a Trident to assist in void travel.

---

## Custom Trident Recipe

You can craft a Trident using the following recipe layout:

| Ingredient | Resource ID |
|---|---|
| **P** | `minecraft:prismarine_shard` |
| **H** | `minecraft:heavy_core` |
| **Q** | `minecraft:quartz` |

```text
P H P
  Q  
  Q  
```

---

## Configuration

You can customize the gameplay parameters using the config file generated at `config/structuresonly.json`:

| Config Option | Default | Description |
|---|---|---|
| `keepStructureBlocks` | `true` | If `true`, retains all structure blocks (e.g., stone bricks, wood blocks). If `false`, only container blocks (chests, spawners, etc.) will generate. |
| `luckyChestDrops` | `true` | If `true`, breaking naturally generated containers yields additional random bonus loot. |
| `spawnStarterChest` | `true` | If `true`, spawns the starter platform and chest at `(0, 64, 0)` for new worlds. |
| `loyaltyTridentVoidReturn` | `true` | If `true`, loyalty-enchanted tridents falling into the void teleport back to the player. |

---

## Installation

### Player Setup
1. Install **Fabric Loader** (version `0.19.3` or higher) for Minecraft `1.21.10`.
2. Place the `structuresonly-1.0.0.jar` and the **Fabric API** jar in your `.minecraft/mods` directory.
3. Launch the game and create a new world.

### Developer Setup
1. Clone the repository.
2. Run `./gradlew genSources` to set up developer environments.
3. Build the mod using `./gradlew build`.

---

## Inspiration & Credits

- Inspired by the Minecraft community's "Void with Structures Only" survival challenges (popularized by BastiGHG and other creators).
- Built using the [Fabric Project Template](https://github.com/FabricMC/fabric-example-mod).
- Special thanks to the SpongePowered Mixin framework.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
