data modify storage homes:temp delete_home_pos set from entity @s Pos
execute store result storage homes:temp delete_home_pos[0] double 16 run data get storage homes:temp delete_home_pos[0] 0.0625
execute store result storage homes:temp delete_home_pos[2] double 16 run data get storage homes:temp delete_home_pos[2] 0.0625
data modify entity @s Pos set from storage homes:temp delete_home_pos

kill @s
execute at @s unless entity @n[type=marker,tag=homes.home_point,y=-2100,dx=15,dy=6200,dz=15] run forceload remove ~ ~
