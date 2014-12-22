package com.five35.minecraft.deathbox.inventorymanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class VanillaInventoryManager implements InventoryManager {
	static {
		InventoryManagerRegistry.register(new VanillaInventoryManager(false));
		InventoryManagerRegistry.register(new VanillaInventoryManager(true));
	}

	private final boolean isArmor;

	private VanillaInventoryManager(final boolean isArmor) {
		this.isArmor = isArmor;
	}

	@Override
	public void clearSlots(final EntityPlayer player) {
		Arrays.fill(this.getArray(player), null);
	}

	@Override
	public List<ItemStack> fillSlots(final EntityPlayer player, final Map<Integer, ItemStack> slots) {
		final List<ItemStack> leftovers = new ArrayList<>();
		final ItemStack[] inventory = this.getArray(player);

		for (final Entry<Integer, ItemStack> entry : slots.entrySet()) {
			if (inventory[entry.getKey()] == null) {
				inventory[entry.getKey()] = entry.getValue();
			} else {
				leftovers.add(entry.getValue());
			}
		}

		return leftovers;
	}

	private ItemStack[] getArray(final EntityPlayer player) {
		return this.isArmor ? player.inventory.armorInventory : player.inventory.mainInventory;
	}

	@Override
	public String getName() {
		return this.isArmor ? "Minecraft|Armor" : "Minecraft|Main";
	}

	@Override
	public Map<Integer, ItemStack> getSlots(final EntityPlayer player) {
		final Map<Integer, ItemStack> slots = new HashMap<>();
		final ItemStack[] inventory = this.getArray(player);

		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] != null) {
				slots.put(i, inventory[i]);
			}
		}

		return slots;
	}
}
