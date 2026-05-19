execute unless score @s homes.has_home matches 1.. run return run tellraw @s ["",{"text":"You don't have any home !","color":"red"},{"text":"\n"},{"text":"/sethome","color":"yellow","click_event":{"action":"suggest_command","command":"/sethome"}}]
scoreboard players operation seconds homes.combat_cooldown = @s homes.combat_cooldown
scoreboard players operation seconds homes.combat_cooldown /= 20 __int__
execute if score @s homes.combat_cooldown matches 1.. run return run title @s actionbar [{"text":"You are in combat! You'll be able to teleport home in ","color":"red"},{score:{name:"seconds",objective:"homes.combat_cooldown"}},"s"]

function homes:update_pos
execute store result score seconds homes.teleporting store result score minutes homes.teleporting run scoreboard players operation @s homes.teleporting = delay homes.config
scoreboard players operation seconds homes.teleporting /= 20 __int__
scoreboard players operation minutes homes.teleporting /= 1200 __int__
scoreboard players operation seconds homes.teleporting %= 60 __int__
execute if score minutes homes.teleporting matches 1.. run return run title @s actionbar [{"text":"You'll be back home in ","color":"green"},{score:{name:"minutes",objective:"homes.teleporting"}},"m ",{score:{name:"seconds",objective:"homes.teleporting"}},"s"]
execute if score seconds homes.teleporting matches 1.. run return run title @s actionbar [{"text":"You'll be back home in ","color":"green"},{score:{name:"seconds",objective:"homes.teleporting"}},"s"]
title @s actionbar {"text":"You are back home","color":"green"}
