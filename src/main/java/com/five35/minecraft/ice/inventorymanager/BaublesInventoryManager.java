package com.five35.minecraft.ice.inventorymanager;

import baubles.api.BaublesApi;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class BaublesInventoryManager extends GeneralInventoryManager {
	@Override
	protected IInventory getInventory(final EntityPlayer player) {
		return BaublesApi.getBaubles(player);
	}

	@Override
	public String getName() {
		return "Baubles";
	}
}
