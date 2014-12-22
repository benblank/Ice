package com.five35.minecraft.deathbox.inventorymanager;

import com.google.common.base.Preconditions;
import cpw.mods.fml.common.FMLCommonHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryManagerRegistry {
	private static Map<String, InventoryManager> managers = new HashMap<>();

	/**
	 * Remove all of a player's managed inventories and return them.
	 *
	 * @param player the player whose inventories to extract
	 * @return a map of the managed inventories
	 */
	@Nonnull
	public static Map<String, Map<Integer, ItemStack>> extractAllInventories(final EntityPlayer player) {
		Preconditions.checkNotNull(player);

		final Map<String, Map<Integer, ItemStack>> inventories = new HashMap<>();

		for (final InventoryManager manager : InventoryManagerRegistry.managers.values()) {
			try {
				final Map<Integer, ItemStack> slots = manager.getSlots(player);

				if (!slots.isEmpty()) {
					inventories.put(manager.getName(), slots);
					manager.clearSlots();
				}
			} catch (final Exception ex) {
				final String message = String.format("An error occurred while extracting the inventory managed by '%s'.", manager.getName());

				FMLCommonHandler.instance().getFMLLogger().error(message, ex);
			}
		}

		return inventories;
	}

	/**
	 * Attempt to merge the supplied inventories into a player's managed
	 * inventories.
	 *
	 * Any items which could not be merged (e.g. into already-filled slots) will
	 * be returned.
	 *
	 * @param player the player into whose inventories to merge
	 * @param inventories the inventories to merge
	 * @return a list of items which were not successfully merged
	 */
	@Nonnull
	public static List<ItemStack> injectInventories(final EntityPlayer player, final Map<String, Map<Integer, ItemStack>> inventories) {
		Preconditions.checkNotNull(player);
		Preconditions.checkNotNull(inventories);

		final List<ItemStack> leftovers = new ArrayList<>();

		for (final Entry<String, Map<Integer, ItemStack>> entry : inventories.entrySet()) {
			final String name = entry.getKey();

			if (!InventoryManagerRegistry.managers.containsKey(name)) {
				final String message = String.format("Unrecognized inventory manager '%s'.", name);

				FMLCommonHandler.instance().getFMLLogger().error(message);

				leftovers.addAll(entry.getValue().values());

				continue;
			}

			final InventoryManager manager = InventoryManagerRegistry.managers.get(name);

			try {
				leftovers.addAll(manager.fillSlots(player, entry.getValue()));
			} catch (final Exception ex) {
				final String message = String.format("An error occurred while filling the inventory managed by '%s'.", manager.getName());

				FMLCommonHandler.instance().getFMLLogger().error(message, ex);
			}
		}

		return leftovers;
	}

	/**
	 * Register a new inventory manager.
	 *
	 * @param manager the inventory manager to register
	 * @throws IllegalArgumentException when an inventory manager with the same
	 *         name has already been registered
	 */
	public static void register(final InventoryManager manager) {
		Preconditions.checkNotNull(manager);

		final String name = manager.getName();

		if (InventoryManagerRegistry.managers.containsKey(name)) {
			final String message = String.format("An inventory manager with the name '%s' has already been registered.", name);

			throw new IllegalArgumentException(message);
		}

		InventoryManagerRegistry.managers.put(name, manager);
	}
}
