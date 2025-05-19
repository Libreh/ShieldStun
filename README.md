# 🛡️ Shield Stun [Fabric]

[//]: # ([![Modrinth]&#40;https://img.shields.io/modrinth/v/shieldstun?label=Modrinth&style=flat-square&#41;]&#40;https://modrinth.com/mod/shieldstun&#41;)
[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.5-green?style=flat-square)](https://minecraft.net)
[![License](https://img.shields.io/github/license/Libreh/ShieldStun?style=flat-square)](LICENSE)
[![Fabric API](https://img.shields.io/badge/Requires-Fabric%20API-blue?style=flat-square)](https://modrinth.com/mod/fabric-api)

A server-side Fabric mod that fixes aims to balance the shield in PvP.

## 🔧 Technical Details

### The Problem
A minecraft bug ([MC-268147](https://bugs.mojang.com/browse/MC-268147)) from 1.11.2 introduced unintended shield behavior, making shields:
- More overpowered in PvP combat
- Trigger invulnerability tick behaviour (despite the player not turning red)
- Less skill-based for competitive play

### The Solution
This mod ports PaperMC's [`skip-vanilla-damage-tick-when-shield-blocked`](https://github.com/PaperMC/Paper/blob/main/paper-server/patches/sources/net/minecraft/world/entity/LivingEntity.java.patch#L1241) patch to Fabric, restoring:
- **Shield Stuns**: Rewarding aggressive playstyles more

## 📥 Installation

**Server-Side Only** (No client installation required)

1. Install **[Fabric Loader 1.21.5](https://fabricmc.net/use/)**
2. Install **[Fabric API](https://modrinth.com/mod/fabric-api)**
3. Download latest `shieldstun-X.X.X.jar` from **[Releases](https://github.com/Libreh/ShieldStun/releases)**
4. Place in server's `mods` folder
5. Start server!

## ⚙️ Configuration

Edit `config/shieldstun.json`:

```json
{
  "enable_stuns": true
}
```

| Option                   | Description                                                                  |
|--------------------------|------------------------------------------------------------------------------|
| `enable_stuns`           | Enables shield-stunning mechanic (true/false)                                |
---

## 🎮 Commands & Permissions

```bash
# Reload config (requires shieldstun.reload)
/shieldstun reload

# View all settings (requires shieldstun.main)
/shieldstun config show

# Get specific setting (requires shieldstun.main)
/shieldstun config get <option>

# Update setting (requires shieldstun.main)
/shieldstun config set <option> <value>
```

### Permission Nodes
| Permission            | Description                  |
|-----------------------|------------------------------|
| `shieldstun.main`     | Access the config subcommand |
| `shieldstun.reload`   | Reload configuration files   |# 🛡️ Shield Stun [Fabric]
