# Shield Stun (Fabric)

Server-side Fabric mod that fixes 8-year-old Minecraft shield invulnerability tick bug [MC-268147](https://bugs.mojang.com/browse/MC-268147) from 1.11.2.

This is done by porting Paper's [`skip-vanilla-damage-tick-when-shield-blocked`](https://github.com/PaperMC/Paper/blob/main/paper-server/patches/sources/net/minecraft/world/entity/LivingEntity.java.patch#L1241) setting to Fabric.

## Configuration

The config file is located at `config/shieldstun.json`.
```json5
{
  // Enable or disable the mod.
  "enable_stuns": true
}
```

## Commands and permissions

| Command             | Permission                  | Description        |
|---------------------|-----------------------------|--------------------|
| /shieldstun         | everyone                    | About info         |
| /shieldstun reload  | shieldstun.reload (or op 3) | Reloads the config |
| /shieldstun status  | shieldstun.status           | Get stun status    |
| /shieldstun enable  | shieldstun.enable (or op 3) | Enable the mod     |
| /shieldstun disable | shieldstun.status (or op 3) | Disable the mod    |

## What this mod doesn't do
This mod does not enable shield stunning for fake players like those found in [Carpet](https://github.com/gnembon/fabric-carpet).
If you are using [TheobaldTheBird](https://github.com/TheobaldTheBird)'s [CarpetPvP](https://github.com/TheobaldTheBird/CarpetPVP) mod, type:

`/carpet shieldStunning true`

This will **temporarily** enable shieldStunning for fake players.
If you want it to be **permanent**, click the text that says '[Change permanently?]'.

This mod also doesn't enable stunning on Paper servers that have shield stunning disabled.