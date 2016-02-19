package com.five35.minecraft.ice;

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
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public class DeathMarkerTileEntity extends TileEntity {
	private long age;

	private final Map<String, Map<Integer, ItemStack>> inventories = new HashMap<>();

	private GameProfile owner;

	private void dropStacks(BlockPos position, final Collection<ItemStack> stacks, boolean noPickupDelay) {
		for (final ItemStack stack : stacks) {
			final Random random = this.worldObj.rand;
			final double x = random.nextDouble() * 0.5 + position.getX();
			final double y = random.nextDouble() * 0.5 + position.getY();
			final double z = random.nextDouble() * 0.5 + position.getZ();

			final EntityItem entity = new EntityItem(this.worldObj, x, y, z, stack);

			if (noPickupDelay) {
				entity.setNoPickupDelay();
			}

			this.worldObj.spawnEntityInWorld(entity);
		}
	}

	public long getAge() {
		return this.worldObj.getTotalWorldTime() - this.age;
	}

	@Override
	public Packet getDescriptionPacket() {
		final NBTTagCompound tag = new NBTTagCompound();

		this.writeToNBT(tag);

		return new S35PacketUpdateTileEntity(this.pos, -1, tag);
	}

	public GameProfile getOwner() {
		return this.owner;
	}

	@Override
	public void onDataPacket(final NetworkManager manager, final S35PacketUpdateTileEntity packet) {
		this.readFromNBT(packet.getNbtCompound());
	}

	public void pop() {
		for (final Map<Integer, ItemStack> slots : this.inventories.values()) {
			this.dropStacks(this.pos, slots.values(), false);
		}

		this.worldObj.setBlockToAir(this.pos);
	}

	@Override
	public void readFromNBT(final NBTTagCompound tag) {
		super.readFromNBT(tag);

		this.age = tag.getLong("age");

		this.inventories.clear();

		final NBTTagCompound inventoriesTag = tag.getCompoundTag("inventories");

		for (final Object key : inventoriesTag.getKeySet()) {
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

		this.setOwner(NBTUtil.readGameProfileFromNBT(tag.getCompoundTag("owner")));
	}

	public void recover(final EntityPlayer player) {
		final List<ItemStack> leftovers = Ice.getProxy().getInventoryManagerRegistry().injectInventories(player, this.inventories);

		this.dropStacks(player.getPosition(), leftovers, true);
		this.worldObj.setBlockToAir(this.pos);
	}

	private void setOwner(final GameProfile owner) {
		this.owner = owner;

		this.markDirty();

		if (this.owner == null) {
			return;
		}

		final MinecraftServer server = MinecraftServer.getServer();

		if (server == null) {
			return;
		}

		if (!this.owner.isComplete() || this.owner.getProperties().get("textures").isEmpty()) {
			final GameProfile cachedOwner = server.getPlayerProfileCache().getGameProfileForUsername(this.owner.getName());

			if (cachedOwner != null) {
				this.owner = cachedOwner;

				if (this.owner.getProperties().get("textures").isEmpty()) {
					this.owner = server.getMinecraftSessionService().fillProfileProperties(this.owner, true);
				}
			}
		}
	}

	public void store(final EntityPlayer player, final Map<String, Map<Integer, ItemStack>> inventories) {
		this.age = player.worldObj.getTotalWorldTime();
		this.setOwner(player.getGameProfile());
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

		NBTUtil.writeGameProfile(ownerTag, this.owner);

		tag.setLong("age", this.age);
		tag.setTag("inventories", inventoriesTag);
		tag.setTag("owner", ownerTag);
	}
}
