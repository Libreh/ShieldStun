# Shield Stun (Fabric)

A server-side Fabric mod that ports Paper's [`skip-vanilla-damage-tick-when-shield-blocked`](https://github.com/PaperMC/Paper/blob/main/paper-server/patches/sources/net/minecraft/world/entity/LivingEntity.java.patch#L1241) patch to Fabric.

## Configuration
The config file is located at `config/shieldstun.json`.
```json5
{
  // Enable or disable the mod.
  "enable_stuns": true
}
```

## Commands and permissions

| Command                                 | Permission               | Description        |
|-----------------------------------------|--------------------------|--------------------|
| /shieldstun reload                      | shieldstun.reload (op 3) | Reloads the config |
| /shieldstun config show                 | shieldstun.main (op 3)   | Shows the config   |
| /shieldstun config get <option>         | shieldstun.main (op 3)   | Gets option value  |
| /shieldstun config set <option> <value> | shieldstun.main (op 3)   | Sets option value  |