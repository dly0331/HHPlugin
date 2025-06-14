# HHPlugin喊话插件

[[English]](README.en.md)|[中文]

![喊话界面](https://github.com/user-attachments/assets/7a866dcf-329c-4390-a45a-b501dc2fcea2)

一个支持你在Velocity群组服中喊话让所有玩家看到的插件，本项目借助Deepseek辅助编辑。

### 插件功能

* `/hh <msg>` 发出喊话

* `/hhop <player>` 和 `/hhvip <player>` 设置不同的权限组

* 对于不同权限组可以设置不同冷却时长

* 点击传送至子服（请确保玩家拥有`/server`权限）

* 可自定义的消息字段（minimessage支持）

* 命令别名（维修中）

### 插件适配

* Velocity 3.4.0

### 下一步计划

* 重构史山（不史但是总归要自己打一遍）

* 将字段和配置分开

* 完成BC代理端版本

### Config配置

```yml
#消息字样，支持minimessage
text: "<gold>【%server%】</gold> <white>%player%</white>：<aqua>%message%</aqua>"
#按钮字样
jointext: "<green>[点击加入]</green>"
#冷却消息
coolmsg: "<red>还需等待 %time% 秒才能使用！</red>"
#错误消息
errmsg: "<red>格式不正确，请输入/hh 消息内容！</red>"
#冷却时间，不用冷却请设置为0
cd: 15
#是否开启Vip
vip: false
vipcd: 5
#op冷却
opcd: 0
#命令别名
aliases:
  - hh
  - shout
  - 广播
```

### 做出贡献

* 提交[Issue](https://github.com/dly0331/hhplugin/issues)（修不修是另外一回事）

* [拉取版本](https://github.com/dly0331/hhplugin/pulls)

另：最开始只有/hh发出简单喊话的功能，Deepseek写太史了，又是报错，所以后面config之类的自己写了，写的不好别喷。
