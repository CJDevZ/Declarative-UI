execute if score @s homes.has_home matches 1.. run return run tellraw @s [{"text":"You already have a home","color":"red"},{"text":"\n"},{"text":"/delhome","color":"yellow","click_event":{"action":"suggest_command","command":"/delhome"}}]

title @s actionbar {"text":"You have successfully set a home","color":"green"}
scoreboard players operation $cur homes.player_id = @s homes.player_id
execute summon marker run function homes:sethome/summon
scoreboard players set @s homes.has_home 1
forceload add ~ ~
