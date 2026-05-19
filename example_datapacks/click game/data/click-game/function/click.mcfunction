playsound block.note_block.pling ui @s ~ ~ ~ 1
scoreboard players operation @s click-game.death_timer = @s click-game.death_time
scoreboard players add @s click-game.counter 1
declarative_ui open @s click-game:game
