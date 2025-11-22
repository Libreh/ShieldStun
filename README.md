# Shield Stun (Fabric)

A server-side Fabric mod that fixes an 8 year old Minecraft bug ([MC-268147](https://bugs.mojang.com/browse/MC-268147)) from 1.11.2.
This is done by porting Paper's `skip-vanilla-damage-tick-when-shield-blocked` setting to Fabric.
The patch is from [`LivingEntity.java`](https://github.com/PaperMC/Paper/blob/main/paper-server/patches/sources/net/minecraft/world/entity/LivingEntity.java.patch#L1241).

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
| /shieldstun reload  | shieldstun.reload (or op 3) | Reloads the config |
| /shieldstun status  | shieldstun.status           | Get stun status    |
| /shieldstun enable  | shieldstun.enable (or op 3) | Enable the mod     |
| /shieldstun disable | shieldstun.status (or op 3) | Disable the mod    |
