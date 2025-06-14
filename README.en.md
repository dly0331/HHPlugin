# HHPlugin — A Shout Plugin

[English]|[[中文]](https://github.com/dly0331/HHPlugin)

A Minecraft Velocity Plugin which allows you to shout message to all players in your Velocity group server.

### Features

* `/hh <msg>` to send a shout.

* `/hhop <player>` and `/hhvip <player>` to set permission groups.

* Custom cooldown durations for different permission groups.

* Click-to-teleport to sub-servers. (requires players to have `/server` permission)

* Customizable message fields. (with MiniMessage support)

* Configurable command aliases. (fixing)

### Compatibility

* Velocity 3.4.0+

### Config

```yml
# Message format (MiniMessage supported)
text: "<gold>【%server%】</gold> <white>%player%</white>：<aqua>%message%</aqua>"
# Button text
jointext: "<green>[Click to Join]</green>"
# Cooldown message
coolmsg: "<red>Please wait %time% seconds before using again!</red>"
# Error message
errmsg: "<red>Invalid format! Usage: /hh <message></red>"
# Cooldown (set to 0 to disable)
cd: 15
# VIP features
vip: false
vipcd: 5
# OP cooldown
opcd: 0
# Command aliases
aliases:
  - hh
  - shout
# - ...
```

### Contributing

* Submit an [Issue](https://github.com/dly0331/hhplugin/issues)

* Create a [Pull Request](https://github.com/dly0331/hhplugin/pulls)
