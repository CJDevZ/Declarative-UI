particle minecraft:end_rod ~ ~0.8 ~ 0.2 0.5 0.2 0.1 25 normal
playsound minecraft:entity.enderman.teleport neutral @a ~ ~ ~ 1 1

scoreboard players operation $cur homes.player_id = @s homes.player_id
execute at @n[type=marker,tag=homes.home_point,predicate=homes:has_cur_player_id] run tp @s ~ ~ ~ ~ ~

execute positioned as @s run particle minecraft:end_rod ~ ~0.8 ~ 0.2 0.5 0.2 0.1 25 normal
execute positioned as @s run playsound minecraft:entity.enderman.teleport neutral @a ~ ~ ~ 1 1

scoreboard players reset @s homes.teleporting
