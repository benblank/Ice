package com.five35.minecraft.ice.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public abstract class GeneralInventoryManager implements InventoryManager {
	@Override
	public void clearSlots(final EntityPlayer player) {
		final IInventory inventory = this.getInventory(player);

		if (inventory != null) {
			final int size = inventory.getSizeInventory();

			for (int i = 0; i < size; i++) {
				inventory.setInventorySlotContents(i, null);
			}

			inventory.markDirty();
		}
	}

	@Override
	public List<ItemStack> fillSlots(final EntityPlayer player, final Map<Integer, ItemStack> slots) {
		final List<ItemStack> leftovers = new ArrayList<>();
		final IInventory inventory = this.getInventory(player);

		if (inventory == null) {
			leftovers.addAll(slots.values());
		} else {
			for (final Entry<Integer, ItemStack> entry : slots.entrySet()) {
				final Integer slot = entry.getKey();
				final ItemStack playerStack = inventory.getStackInSlot(slot);

				if (playerStack != null) {
					leftovers.add(playerStack);
				}

				inventory.setInventorySlotContents(slot, entry.getValue());
			}

			inventory.markDirty();
		}

		return leftovers;
	}

	protected abstract IInventory getInventory(final EntityPlayer player);

	@Override
	public Map<Integer, ItemStack> getSlots(final EntityPlayer player) {
		final Map<Integer, ItemStack> slots = new HashMap<>();
		final IInventory inventory = this.getInventory(player);

		if (inventory != null) {
			final int size = inventory.getSizeInventory();

			for (int i = 0; i < size; i++) {
				final ItemStack stack = inventory.getStackInSlot(i);

				if (stack != null) {
					slots.put(i, stack);
				}
			}
		}

		return slots;
	}
}
