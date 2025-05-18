# JavaRPG

To Add:
- Camera with larger map
- Autofiring
- Enemy sprite sheet
- power ups 
  - Need to figure out what I want to do for this
- exp system
- UI Overlay
- sound effects

Power Up System:

- How exp is gained
  - gain exp on every kill (scale dfor more difficult enemies)
  - Small exp for surviving each wave
- Level Up
  - when exp reaches threshold, level up
  - present player with three random upgrade choices from large pool
  - player picks 1 which is immediately applied
  - Upgrade pool can include stats and abilities like:
Move speed, bullet speed, crit chance, bullet damage, multi shot, piercing shots, fire speed, health regen, shield boost, knockback
- Unique Weapons unlock system
  - Unlocking weapons
    - Defeat a boss (Major enemy every 10 rounds or so)
    - Unlock a unique weapon as a speciel item/reward
  - Weapon implementation
    - Unique weapons replace ammo system temporarily
    - Example:
      - Chicken dippers (Dippy time)
      - changes bullets to chicken dippers for 20 seconds
      - 60 second cooldown
      - has splash damage
      - does more damage to bird type enemies
    - Each unique weapon has diff special effects