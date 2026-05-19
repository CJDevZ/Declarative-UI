scoreboard players operation $old homes.x = @s homes.x
scoreboard players operation $old homes.y = @s homes.y
scoreboard players operation $old homes.z = @s homes.z

function homes:update_pos

execute if score @s homes.x = $old homes.x if score @s homes.y = $old homes.y if score @s homes.z = $old homes.z run return fail

function homes:cancel_teleport
