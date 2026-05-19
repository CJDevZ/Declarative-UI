scoreboard players set @s click-game.death_time 15
scoreboard players set @s click-game.death_timer 15
scoreboard players set @s click-game.counter 0
execute store result score @s click-game.random_slot run random value 0..26
declarative_ui open @s click-game:game
