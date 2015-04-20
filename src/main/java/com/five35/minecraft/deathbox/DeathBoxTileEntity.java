package com.five35.minecraft.deathbox;

import com.mojang.authlib.GameProfile;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;

public class DeathBoxTileEntity extends TileEntity {
	private final Map<String, Map<Integer, ItemStack>> inventories = new HashMap<>();
	private GameProfile owner;

	private void dropStacks(final Collection<ItemStack> stacks) {
		for (final ItemStack stack : stacks) {
			final Random random = this.worldObj.rand;
			final double x = random.nextDouble() * 0.5 + this.xCoord;
			final double y = random.nextDouble() * 0.5 + this.yCoord;
			final double z = random.nextDouble() * 0.5 + this.zCoord;

			this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, x, y, z, stack));
		}
	}

	public GameProfile getOwner() {
		return this.owner;
	}

	public void pop() {
		for (final Map<Integer, ItemStack> slots : this.inventories.values()) {
			this.dropStacks(slots.values());
		}

		this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
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

		this.owner = NBTUtil.func_152459_a(tag.getCompoundTag("owner"));
	}

	public void recover(final EntityPlayer player) {
		final List<ItemStack> leftovers = DeathBox.getProxy().getInventoryManagerRegistry().injectInventories(player, this.inventories);

		this.dropStacks(leftovers);
		this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
	}

	public void store(final EntityPlayer player, final Map<String, Map<Integer, ItemStack>> inventories) {
		this.owner = player.getGameProfile();
		this.inventories.clear();
		this.inventories.putAll(inventories);
	}

	@Override
	public void writeToNBT(final NBTTagCompound tag) {
		super.writeToNBT(tag);

		final NBTTagCompound inventoriesTag = new NBTTagCompound();
		final NBTTagCompound ownerTag = new NBTTagCompound();

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

		NBTUtil.func_152460_a(ownerTag, this.owner);

		tag.setTag("inventories", inventoriesTag);
		tag.setTag("owner", ownerTag);
	}
}
