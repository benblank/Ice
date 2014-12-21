package com.five35.minecraft.deathbox;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class DeathBoxTileEntity extends TileEntity {
	private final Map<Integer, ItemStack> inventory = new HashMap<>();
	private String ownerName;

	public String getOwnerName() {
		return this.ownerName;
	}

	public void pop() {
		// drop contents on ground
	}

	public void recover(final EntityPlayer player) {
		// add items to player, dropping stacks for conflicting slots

		// destroy death box
	}

	public void setOwnerName(final String ownerName) {
		this.ownerName = ownerName;
	}
}
