$scoreboard players set damage_cancel homes.config $(enabled)
tellraw @s ["Set teleport cancellation on damage to ",{"score":{"name":"damage_cancel","objective":"homes.config"}}]
