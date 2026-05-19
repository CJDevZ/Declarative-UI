$scoreboard players set combat_cooldown homes.config $(ticks)
tellraw @s ["Set combat cooldown to ",{"score":{"name":"combat_cooldown","objective":"homes.config"}}," ticks"]
