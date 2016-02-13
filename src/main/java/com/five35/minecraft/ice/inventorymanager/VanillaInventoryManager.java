package com.five35.minecraft.ice.inventorymanager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class VanillaInventoryManager extends GeneralInventoryManager {
	@Override
	protected IInventory getInventory(EntityPlayer player) {
		return player.inventory;
	}

	@Override
	public String getName() {
		return "Minecraft";
	}
}
