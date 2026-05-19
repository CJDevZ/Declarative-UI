data modify storage homes:temp Pos set from entity @s Pos
execute store result score @s homes.x run data get storage homes:temp Pos[0] 100
execute store result score @s homes.y run data get storage homes:temp Pos[1] 100
execute store result score @s homes.z run data get storage homes:temp Pos[2] 100
