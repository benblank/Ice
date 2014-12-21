package com.five35.minecraft.deathbox;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class DeathBoxTileEntity extends TileEntity {
	private final Map<Integer, ItemStack> inventory = new HashMap<>();
	private String ownerName;

	public String getOwnerName() {
		return this.ownerName;
	}

	public void setOwnerName(final String ownerName) {
		this.ownerName = ownerName;
	}
}
