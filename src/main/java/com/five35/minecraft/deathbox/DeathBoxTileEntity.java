package com.five35.minecraft.deathbox;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class DeathBoxTileEntity extends TileEntity {
	private static void arrayToMap(final ItemStack[] array, final Map<Integer, ItemStack> map) {
		for (int i = 0; i < array.length; i++) {
			final ItemStack stack = array[i];

			if (stack != null) {
				map.put(i, stack.copy());
				array[i] = null;
			}
		}
	}

	private final Map<String, Map<Integer, ItemStack>> inventories = new HashMap<>();
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

	public void store(final EntityPlayer player) {
		this.ownerName = player.getCommandSenderName();

		final Map<Integer, ItemStack> mainInventory = new HashMap<>();
		final Map<Integer, ItemStack> armorInventory = new HashMap<>();

		this.inventories.put("mainInventory", mainInventory);
		this.inventories.put("armorInventory", armorInventory);

		DeathBoxTileEntity.arrayToMap(player.inventory.mainInventory, mainInventory);
		DeathBoxTileEntity.arrayToMap(player.inventory.armorInventory, armorInventory);
	}

	@Override
	public void writeToNBT(final NBTTagCompound tag) {
		super.writeToNBT(tag);

		final NBTTagCompound inventoriesTag = new NBTTagCompound();

		for (final Entry<String, Map<Integer, ItemStack>> inventoryEntry : this.inventories.entrySet()) {
			final NBTTagList inventoryTag = new NBTTagList();

			for (final Entry<Integer, ItemStack> slotEntry : inventoryEntry.getValue().entrySet()) {
				final NBTTagCompound slotTag = new NBTTagCompound();

				slotEntry.getValue().writeToNBT(slotTag);
				slotTag.setByte("slot", slotEntry.getKey().byteValue());

				inventoryTag.appendTag(slotTag);
			}

			inventoriesTag.setTag(inventoryEntry.getKey(), inventoryTag);
		}

		tag.setTag("inventories", inventoriesTag);
		tag.setString("ownerName", this.ownerName);
	}
}
