<center>

# Eslium Backport

[![Modrinth Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/available/modrinth_vector.svg)](https://modrinth.com/project/5gn0R1Gq)
[![Github Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/available/github_vector.svg)](https://github.com/1szco1/Eslium-Backport)
[![Discord Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/social/discord-singular_vector.svg)](https://discord.com/users/1134017208410968064)
[![YouTube Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/social/youtube-singular_vector.svg)](https://youtube.com/@1szco1)

[![Fabric Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/supported/fabric_vector.svg)](https://fabricmc.net/)
[![Quilt Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/supported/quilt_vector.svg)](https://quiltmc.org/)
[![NeoForge Icon](https://raw.githubusercontent.com/Solmeye/Mirror-Anticheat/2b0ed26bc9fd49f2f1c6b8aa289dbb81cd0db956/neoforge.svg)](https://neoforged.net/)
[![Forge Icon](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy-minimal/supported/forge_vector.svg)](https://files.minecraftforge.net/net/minecraftforge/forge/)

</center>

## What's Eslium ?

Eslium is a project to predict more things client-side.
For example, if you want to use a Crystal, you must wait for the server validation for it to appear.
This mod fixes that and therefore compensates for the ping.

## Credits

- **Original mod:** [Eslium on Modrinth](https://modrinth.com/project/zaVwh7an) by [@Solmeye](https://github.com/Solmeye)
- **Backport:** [@1szco1](https://github.com/1szco1)

## Predictions implemented

<details>
<summary>Minecarts</summary>

When using any type of minecart on a rail, the spawn of the minecart is predicted

</details>

<details>
<summary>End Crystal</summary>

When using an end crystal on obisidian or bedrock, the spawn of the end crystal is predicted

</details>

## Predictions Added so far* :
- Cooldowns
- Consumables
- Elytra
- Firework rocket
- Swap
- Inventory
- Entity pose
- Jukebox
- Crossbow
- Pickup entities
- Trident
- Weapons
- Jukebox
- Bottle o' Enchanting
- Experience
- Ender Pearls
- Potions
- Pick Block
- Cactus damage
- Bed
- Fishing Rod
- Lead
- Boat
- Snowball
- Spawn Eggs
- Egg
- Armor Stand
- Note block
- Item frame
- Dyes
- Interfaces
- Void damage
- Game mode change
- Potion Effects
- Tchat
- Sounds
- Crafting
- Scaffolding
- Knockback
- Fall damage
- Interaction with blocks
- Interaction with entities
- Interaction with items


## Configuration

The mod can be configured **in-game** through [Mod Menu](https://modrinth.com/mod/modmenu) (optional dependency) or with the rebindable keybind **`key.clienthings.open_config`** (unbound by default — set it in *Options → Controls*). The configuration is stored at `config/eslium.json`.

Every prediction section shares the same base options:

- `enabled` – toggle this prediction
- `servers` – which server types it applies to (applies if **any** is enabled):
  - `CUSTOM` – servers using the Eslium protocol
  - `MODDED` – modded servers
  - `PLUGIN` – plugin-based servers
  - `VANILLA` – vanilla servers
- `maxTime` – maximum time in ms to keep a predicted (not yet confirmed) action alive
- `maxDistance` – maximum distance in blocks for the prediction (where applicable)

<details>
<summary>Default configuration</summary>

```
{
  "enabled": true,
  "optout": false,
  "debug": false,
  "experimental": false,
  "latencyMargin": 25,
  "version": 1,
  "safeMode": false,
  "consumables": { "enabled": false, "servers": { "CUSTOM": true, "MODDED": true, "PLUGIN": true, "VANILLA": true }, "maxTime": 5000, "maxDistance": 16 },
  "cooldowns": { "enabled": false, "servers": { "CUSTOM": true, "MODDED": true, "PLUGIN": true, "VANILLA": true } },
  "crystals": {
    "enabled": true,
    "servers": { "CUSTOM": true, "MODDED": true, "PLUGIN": true, "VANILLA": true },
    "maxTime": 5000,
    "delay": 0,
    "dynamicDelay": true,
    "resync": 20,
    "autoDestroy": { "enabled": true, "maxTime": 5000 }
  },
  "elytras": { "enabled": false, "servers": { "CUSTOM": true, "MODDED": true, "PLUGIN": true, "VANILLA": true }, "maxTime": 5000, "maxDistance": 16 },
  "firework": { "enabled": false, "servers": { "CUSTOM": true, "MODDED": true, "PLUGIN": true, "VANILLA": true }, "maxTime": 5000, "maxDistance": 16 },
  "minecart": { "enabled": true, "servers": { "CUSTOM": true, "MODDED": true, "PLUGIN": true, "VANILLA": true }, "maxTime": 5000 },
  "pose": { "enabled": false, "servers": { "CUSTOM": true, "MODDED": true, "PLUGIN": true, "VANILLA": true } },
  "swap": { "enabled": false, "servers": { "CUSTOM": true, "MODDED": true, "PLUGIN": true, "VANILLA": true } },
  "weapons": { "enabled": false, "servers": { "CUSTOM": true, "MODDED": true, "PLUGIN": true, "VANILLA": true }, "maxTime": 5000, "maxDistance": 16 },
  "windcharge": { "enabled": false, "servers": { "CUSTOM": true, "MODDED": true, "PLUGIN": true, "VANILLA": true }, "maxTime": 5000, "maxDistance": 16 },
  "throwables": { "enabled": false, "servers": { "CUSTOM": true, "MODDED": true, "PLUGIN": true, "VANILLA": true }, "maxTime": 5000, "maxDistance": 16 },
  "placeables": { "enabled": false, "servers": { "CUSTOM": true, "MODDED": true, "PLUGIN": true, "VANILLA": true }, "maxTime": 5000, "maxDistance": 16 },
  "interactables": { "enabled": false, "servers": { "CUSTOM": true, "MODDED": true, "PLUGIN": true, "VANILLA": true }, "maxTime": 5000, "maxDistance": 16 },
  "tools": { "enabled": false, "servers": { "CUSTOM": true, "MODDED": true, "PLUGIN": true, "VANILLA": true }, "maxTime": 5000, "maxDistance": 16 },
  "combat": { "enabled": false, "servers": { "CUSTOM": true, "MODDED": true, "PLUGIN": true, "VANILLA": true }, "maxTime": 5000, "maxDistance": 16 },
  "redstone": { "enabled": false, "servers": { "CUSTOM": true, "MODDED": true, "PLUGIN": true, "VANILLA": true }, "maxTime": 5000, "maxDistance": 16 },
  "anchors": {
    "enabled": true,
    "servers": { "CUSTOM": true, "MODDED": true, "PLUGIN": true, "VANILLA": true },
    "maxTime": 3000,
    "glowstoneSound": true,
    "clientExplosion": true,
    "reInteract": true,
    "ghostAnchor": true
  },
  "placement": {
    "enabled": true,
    "servers": { "CUSTOM": true, "MODDED": true, "PLUGIN": true, "VANILLA": true },
    "dynamic": true,
    "fixedWindowMs": 1000,
    "floorMs": 50,
    "capMs": 300
  }
}
```

</details>

<details>
<summary>Explanation</summary>

**Global options**

- `enabled` – enable or disable the whole mod
- `optout` – opts this client out of the Eslium protocol
- `debug` – print debug information to the log (only active while `enabled` is true)
- `experimental` – allow experimental predictions
- `latencyMargin` – ping in ms added to your estimated latency. Useful to avoid considering actions the server denies after a latency spike. Should represent the current latency fluctuation of your connection.
- `version` – configuration version. Do not touch!
- `safeMode` – reduces the likelihood of being flagged by an impossible anticheat. The probability is already low when disabled. Warning: this can lead to bizarre behavior.

**Sections**

All sections below share `enabled`, `servers`, and (where shown) `maxTime` / `maxDistance`: `consumables`, `cooldowns`, `crystals`, `elytras`, `firework`, `minecart`, `pose`, `swap`, `weapons`, `windcharge`, `throwables`, `placeables`, `interactables`, `tools`, `combat`, `redstone`, `anchors`, `placement`.

- `crystals`
  - `delay` – fixed delay in ms before a hit crystal is hidden when `dynamicDelay` is off (0–200)
  - `dynamicDelay` – when enabled, the delay is estimated automatically from your latency instead of using `delay`
  - `resync` – number of ticks to hide/resync a real crystal before it is shown again (0–50)
  - `autoDestroy` – automatically remove the client-side crystal prediction after a delay
    - `enabled` / `maxTime`

- `anchors`
  - `glowstoneSound` – play the charge sound client-side
  - `clientExplosion` – show the detonation explosion client-side
  - `reInteract` – allow re-interacting with the anchor before the server confirms
  - `ghostAnchor` – hide a stale (ghost) anchor block

- `placement` – fixes block-placement "ghosting" (a placed block briefly reverting to its old state)
  - `dynamic` – derive the settle window automatically from your measured latency
  - `fixedWindowMs` – window used when `dynamic` is false
  - `floorMs` / `capMs` – lower/upper bounds for the dynamic window

</details>

## Disclaimer

Allowed on servers such as:
- `PvPClub`
- `FadedMC`
- `Turtled`
- `CatPvP`

Versions 4.0+ are designed not to trigger anticheats.
However, a bug is not inevitable, so it is still recommended to request authorization for this mod before using it on a server.

## FAQ
### How it works ?
This mod uses mixins to inject code into Minecraft code. During certain actions, if implemented, the behavior of a vanilla server is replicated on the client side. 

### Is this mod a cheat?
You might think this mod is a cheat because it gives an advantage to those who don't have it, and these kinds of mods often raise these kinds of questions.

Here are a few points :

- Some people naturally have lower ping, yet it is not considered cheating

- It is possible to have 0ms ping behavior in vanilla, but it is also possible to have it (as far as it is implemented) with any ping and Eslium.

- Some mods remain accepted by the community but their behavior is impossible to reproduce in vanilla.
I'm thinking in particular of Health Indicator, Armor HUD, AppleSkin, FreeCam, and Ok Zoomer.

- This mod has been accepted by Modrinth and some PvP servers.

- Sodium optimize FPS, Lithium optimize MSPT, Eslium optimize the ping's impact.

- Other ping's optimizer are allowed, such as Marlow's Crystal Optimizer, Hero's Elytra Optimizer, Consumable Optimizer or Anchor Optimizer

### Will versions 4.0+ be backported for minecraft 1.16.5?
Yes, it already have.

## Compatibility
Eslium should work on any client.
