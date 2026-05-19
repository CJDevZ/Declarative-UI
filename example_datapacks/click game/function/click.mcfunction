execute unless score $cur click-game.random_slot = @s click-game.random_slot run return fail
playsound block.note_block.pling ui @s ~ ~ ~ 1
execute store result score @s click-game.random_slot run random value 0..26
scoreboard players operation @s click-game.death_timer = @s click-game.death_time
scoreboard players add @s click-game.counter 1
declarative_ui open @s click-game:game
