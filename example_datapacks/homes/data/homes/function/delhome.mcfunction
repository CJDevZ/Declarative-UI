execute unless score @s homes.has_home matches 1.. run return run tellraw @s ["",{"text":"You don't have any home !","color":"red"},{"text":"\n"},{"text":"/sethome","color":"yellow","click_event":{"action":"suggest_command","command":"/sethome"}}]

scoreboard players operation $cur homes.player_id = @s homes.player_id
execute as @n[type=marker,tag=homes.home_point,predicate=homes:has_cur_player_id] at @s run function homes:delhome/del
title @s actionbar {"text":"Your home has been successfully deleted","color":"red"}
scoreboard players reset @s homes.has_home
