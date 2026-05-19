advancement revoke @s only homes:attacked_by_entity
execute unless score damage_cancel homes.config matches 1 run return fail
execute unless score @s homes.teleporting matches 1.. run return fail

function homes:cancel_teleport
