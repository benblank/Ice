package com.five35.minecraft.deathbox.inventorymanager;

import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface InventoryManager {
	void clearSlots();

	List<ItemStack> fillSlots(final EntityPlayer player, final Map<Integer, ItemStack> slots);

	String getName();

	Map<Integer, ItemStack> getSlots(final EntityPlayer player);
}
