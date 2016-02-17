# In Case of Emergency

There's long been a desire for a "death mode" in Minecraft which falls somewhere between the default behavior of dropping everything on the ground and the `keepInventory` gamemode.  Traditionally, this is done by storing all your items in a block with an inventory — a "death chest", a gravestone, or the like.

In Case of Emergency offers a new alternative, with cool new features:

* ICE not only stores your items, but *also their position within your inventory*, allowing you to quickly re-equip everything, rather than having to pick it all up off the ground.
* ICE understands Minecraft's team system, and can be configured to allow teammates to retrieve your inventory for you when it's too dangerous to get yourself.

## How to Use

1. Install by dropping [the `.jar` file](https://github.com/benblank/Ice/releases/latest) into your mods folder (requires [Forge](http://www.minecraftforge.net/)).
2. Play Minecraft.
3. Die.  (In lava, for example.)
4. Run back to your "death marker" — a spinning copy of your head.
5. Right-click the death marker to re-equip everything!  (And none of your items were at risk of falling in the lava!)

## Configuration

By default, ICE will only allow you, the player who died, to re-equip your items by right-clicking on your death marker.  Anyone on your team, however, can "pop" your death marker and drop all the items on the ground by left-clicking.  Anyone not on your team **cannot interact with your death marker at all**.  It's unbreakable and unexplodable.

When you play Minecraft for the first time after installing ICE, a configuration file is created at `<.minecraft>/config/Ice.cfg`.  The settings and possible values are explained in that file:

    # Configuration file
    
    ##########################################################################################################
    # security
    #--------------------------------------------------------------------------------------------------------#
    # The allowable values for these actions are 'no' (completely disabled), 'owner' (only the player whose
    # death created the death marker), 'team' (anyone on the dying player's team), or 'yes' (anyone).
    ##########################################################################################################
    
    security {
        # Whether death markers can be 'popped' by hitting them.
        S:popping=team
    
        # Whether the contents of death markers can be recovered by right-clicking them.
        S:recovering=owner
    }

Note that if you set both to `no`, death markers will only be clearable by players in creative mode!  (So don't do that.)

## Mod Support

ICE can support non-vanilla inventories through dedicated mod support.  Currently, the supported mods are:

* [Baubles](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1294623)
* More coming soon?  Open an [issue](https://github.com/benblank/Ice/issues) to request support for your favorite mod!

### Public API

Alternatively, mods can add their own support for ICE using its public API.  This is done by creating a class implementing the `InventoryManager` interface (the simplest way to do this is by extending `GeneralInventoryManager`).  Take a look at [`BaublesInventoryManager`](src/main/java/com/five35/minecraft/ice/BaublesInventoryManager.java) to see how easy this can be!

Once your inventory manager has been created, register it with `InventoryManagerRegistry.register(...)`.  That's all it takes to add support for your mod to ICE's death markers.
