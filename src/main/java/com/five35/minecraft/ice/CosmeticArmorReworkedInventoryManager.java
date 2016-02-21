package com.five35.minecraft.ice;

import lain.mods.cos.PlayerUtils;
import lain.mods.cos.CosmeticArmorReworked;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import com.five35.minecraft.ice.api.GeneralInventoryManager;

final class CosmeticArmorReworkedInventoryManager extends GeneralInventoryManager {
	@Override
	protected IInventory getInventory(EntityPlayer player) {
		return CosmeticArmorReworked.invMan.getCosArmorInventory(PlayerUtils.getPlayerID(player));
	}

	@Override
	public String getName() {
		return "CosmeticArmorReworked";
	}

}
