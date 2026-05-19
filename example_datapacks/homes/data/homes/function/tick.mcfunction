execute as @a unless score @s homes.player_id matches -2147483648..2147483647 store result score @s homes.player_id run scoreboard players add $counter homes.player_id 1

scoreboard players remove @a[scores={homes.teleporting=1..}] homes.teleporting 1
execute as @a[scores={homes.teleporting=..0}] at @s run function homes:teleport_process
scoreboard players remove @a[scores={homes.combat_cooldown=1..}] homes.combat_cooldown 1
