package com.five35.minecraft.ice;

import com.five35.minecraft.ice.api.GeneralInventoryManager;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class VanillaInventoryManager extends GeneralInventoryManager {
	@Override
	public List<ItemStack> fillSlots(EntityPlayer player, Map<Integer, ItemStack> slots) {
		List<ItemStack> leftovers = super.fillSlots(player, slots);

		// Simply marking the player's inventory as dirty doesn't actually
		// cause the server to synchronize it to the client.
		if (player instanceof EntityPlayerMP) {
			((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
		}

		return leftovers;
	}

	@Override
	protected IInventory getInventory(EntityPlayer player) {
		return player.inventory;
	}

	@Override
	public String getName() {
		return "Minecraft";
	}
}
