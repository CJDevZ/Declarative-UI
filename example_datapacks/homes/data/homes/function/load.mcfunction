
#
#	SCORES
#

scoreboard objectives add homes.config dummy
scoreboard objectives add homes.player_id dummy
scoreboard objectives add homes.teleporting dummy
scoreboard objectives add homes.has_home dummy
scoreboard objectives add homes.combat_cooldown dummy
scoreboard objectives add __int__ dummy
scoreboard players set 20 __int__ 20
scoreboard players set 60 __int__ 60
scoreboard players set 1200 __int__ 1200

scoreboard objectives add homes.x dummy
scoreboard objectives add homes.y dummy
scoreboard objectives add homes.z dummy

schedule function homes:tick_1s 1s replace

tellraw @a {"text":"Homes Reloaded","color":"green"}