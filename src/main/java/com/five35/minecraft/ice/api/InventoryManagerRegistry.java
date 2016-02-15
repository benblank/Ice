package com.five35.minecraft.ice.api;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Logger;

public class InventoryManagerRegistry {
	private final Logger logger;
	private final Map<String, InventoryManager> managers = new HashMap<>();

	public InventoryManagerRegistry(final Logger logger) {
		this.logger = logger;
	}

	/**
	 * Clear the selected managed inventories for a player.
	 * 
	 * @param player the player whose inventories should be cleared
	 * @param inventories the names of the managed inventories to clear
	 */
	public void clearInventories(final EntityPlayer player, Collection<String> inventories) {
		Preconditions.checkNotNull(player);
		Preconditions.checkNotNull(inventories);

		for (final String inventory : inventories) {
			this.managers.get(inventory).clearSlots(player);
		}
	}

	/**
	 * Get all of a player's managed inventories and return them.
	 *
	 * @param player the player whose inventories to get
	 * @return a map of the managed inventories
	 */
	@Nonnull
	public Map<String, Map<Integer, ItemStack>> getAllInventories(final EntityPlayer player) {
		Preconditions.checkNotNull(player);

		final Map<String, Map<Integer, ItemStack>> inventories = new HashMap<>();

		for (final InventoryManager manager : this.managers.values()) {
			try {
				final String message = String.format("Getting managed inventory '%s'.", manager.getName());
				this.logger.debug(message);

				final Map<Integer, ItemStack> slots = manager.getSlots(player);

				if (!slots.isEmpty()) {
					inventories.put(manager.getName(), slots);
				} else {
					this.logger.debug("Managed inventory was empty.");
				}
			} catch (final Exception ex) {
				final String message = String.format("An error occurred while extracting the inventory managed by '%s'.", manager.getName());

				this.logger.error(message, ex);
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
	public List<ItemStack> injectInventories(final EntityPlayer player, final Map<String, Map<Integer, ItemStack>> inventories) {
		Preconditions.checkNotNull(player);
		Preconditions.checkNotNull(inventories);

		final List<ItemStack> leftovers = new ArrayList<>();

		for (final Entry<String, Map<Integer, ItemStack>> entry : inventories.entrySet()) {
			final String name = entry.getKey();

			if (!this.managers.containsKey(name)) {
				final String message = String.format("Unrecognized inventory manager '%s'.", name);

				this.logger.error(message);

				leftovers.addAll(entry.getValue().values());

				continue;
			}

			final InventoryManager manager = this.managers.get(name);

			try {
				leftovers.addAll(manager.fillSlots(player, entry.getValue()));
			} catch (final Exception ex) {
				final String message = String.format("An error occurred while filling the inventory managed by '%s'.", manager.getName());

				this.logger.error(message, ex);
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
	public void register(final InventoryManager manager) {
		Preconditions.checkNotNull(manager);

		final String name = manager.getName();

		if (this.managers.containsKey(name)) {
			final String message = String.format("An inventory manager with the name '%s' has already been registered.", name);

			throw new IllegalArgumentException(message);
		}

		this.managers.put(name, manager);
	}
}
