scoreboard players remove @a[scores={click-game.death_timer=1..}] click-game.death_timer 1
execute as @a[scores={click-game.death_timer=0}] run function click-game:lose_game
