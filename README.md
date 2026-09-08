# Shield Stun (Fabric)

Server-side Fabric mod that adds Paper style shield stunning.

Since 1.11.2 a blocked hit runs a full damage tick, handing the defender invulnerability frames and swallowing the follow-up that used to launch them ([MC-268147](https://bugs.mojang.com/browse/MC/issues/MC-268147)). This mod skips that tick.

It matches Paper's [`skip-vanilla-damage-tick-when-shield-blocked`](https://docs.papermc.io/paper/reference/global-configuration/#unsupported_settings_skip_vanilla_damage_tick_when_shield_blocked), an opt-in under `unsupported-settings` rather than a fix. Skipping the tick was originally a Paper bug, but players got used to stunning with it, so Paper kept it as a setting.

## Configuration

The config file is located at `config/shieldstun.json`. It controls whether shield stuns are applied.
```json
{
  "enable_stuns": true
}
```

## Commands and permissions

| Command             | Permission                   | Description        |
|---------------------|------------------------------|--------------------|
| /shieldstun         | everyone                     | About info         |
| /shieldstun reload  | shieldstun.reload (or op 3)  | Reloads the config |
| /shieldstun status  | shieldstun.status            | Get stun status    |
| /shieldstun enable  | shieldstun.enable (or op 3)  | Enable the mod     |
| /shieldstun disable | shieldstun.disable (or op 3) | Disable the mod    |

## What this mod doesn't do

This mod does not enable shield stunning for fake players like those found in [Carpet](https://github.com/gnembon/fabric-carpet).
If you are using [TheobaldTheBird](https://github.com/TheobaldTheBird)'s [CarpetPvP](https://github.com/TheobaldTheBird/CarpetPVP) mod, type:

`/carpet shieldStunning true`

This will **temporarily** enable shieldStunning for fake players.
If you want it to be **permanent**, click the text that says '[Change permanently?]'.

This mod also doesn't enable stunning on Paper servers that have shield stunning disabled.
