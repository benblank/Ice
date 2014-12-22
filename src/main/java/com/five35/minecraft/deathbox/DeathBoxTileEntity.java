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
	private final Map<String, Map<Integer, ItemStack>> inventories = new HashMap<>();
	private String ownerName;

	public String getOwnerName() {
		return this.ownerName;
	}

	public void pop() {
		// drop contents on ground
	}

	@Override
	public void readFromNBT(final NBTTagCompound tag) {
		super.readFromNBT(tag);

		this.inventories.clear();

		final NBTTagCompound inventoriesTag = tag.getCompoundTag("inventories");

		for (final Object key : inventoriesTag.func_150296_c()) {
			final String inventoryName = (String) key;
			final NBTTagList inventoryTag = inventoriesTag.getTagList(inventoryName, tag.getId());
			final Map<Integer, ItemStack> inventory = new HashMap<>();

			for (int i = 0; i < inventoryTag.tagCount(); i++) {
				final NBTTagCompound slotTag = inventoryTag.getCompoundTagAt(i);
				final ItemStack stack = ItemStack.loadItemStackFromNBT(slotTag);

				inventory.put((int) slotTag.getByte("slot"), stack);
			}

			this.inventories.put(inventoryName, inventory);
		}

		this.ownerName = tag.getString("ownerName");
	}

	public void recover(final EntityPlayer player) {
		// add items to player, dropping stacks for conflicting slots

		// destroy death box
	}

	public void store(final EntityPlayer player, final Map<String, Map<Integer, ItemStack>> inventories) {
		this.ownerName = player.getCommandSenderName();
		this.inventories.clear();
		this.inventories.putAll(inventories);
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
