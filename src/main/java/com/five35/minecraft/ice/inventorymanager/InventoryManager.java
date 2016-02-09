package com.five35.minecraft.ice.inventorymanager;

import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface InventoryManager {
	/**
	 * Empty all slots in the managed inventory.
	 *
	 * @param player the player whose managed inventory should be cleared
	 */
	void clearSlots(EntityPlayer player);

	/**
	 * Attempt to fill the managed inventory using the supplied slots.
	 *
	 * Any item stacks which can't be added to the player's inventory (e.g.
	 * because the target slot is already filled) should be returned as a list.
	 *
	 * @param player the player on whose inventory to operate
	 * @param slots the slots with which to fill the player's inventory
	 * @return a list of item stacks which could not be added to the player's
	 *         inventory
	 */
	@Nonnull
	List<ItemStack> fillSlots(final EntityPlayer player, final Map<Integer, ItemStack> slots);

	/**
	 * Get the name of this inventory manager.
	 *
	 * This name must uniquely identify the type of inventory being managed.
	 * "Source[|Type]" is probably sufficient. (e.g. "Minecraft|Main",
	 * "Baubles", or "TCon")
	 *
	 * @return the name of this inventory manager
	 */
	@Nonnull
	String getName();

	/**
	 * Retrieve the contents of the managed inventory.
	 *
	 * @param player the player whose inventory should be retrieved
	 * @return a map of slots -> item stacks
	 */
	@Nonnull
	Map<Integer, ItemStack> getSlots(final EntityPlayer player);
}
