package com.five35.minecraft.deathbox;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class LivingDeathEventHandler {
	private static enum Validity {
		ALTERNATE,
		INVALID,
		VALID;
	}

	private static Validity isPositionValid(final World world, final int x, final int y, final int z) {
		if (x < -30000000 || x >= 30000000 || y < 0 && y >= 256 || z < -30000000 || z >= 30000000) {
			return Validity.INVALID;
		}

		if (world.getBlock(x, y, z).isReplaceable(world, x, y, z)) {
			return Validity.VALID;
		}

		if (world.getTileEntity(x, y, z) == null) {
			return Validity.ALTERNATE;
		}

		return Validity.INVALID;
	}

	@SubscribeEvent
	@SuppressWarnings("static-method")
	public void onLivingDeathEvent(final LivingDeathEvent event) {
		if (!(event.entity instanceof EntityPlayer)) {
			return;
		}

		final EntityPlayer player = (EntityPlayer) event.entity;
		final String playerName = player.getCommandSenderName();
		final World world = player.worldObj;

		if (world.isRemote) {
			return;
		}

		int x = (int) player.posX;
		int y = (int) player.posY + 1;
		int z = (int) player.posZ;

		final Map<String, Map<Integer, ItemStack>> inventories = DeathBox.getProxy().getInventoryManagerRegistry().extractAllInventories(player);

		if (inventories.isEmpty()) {
			final String message = String.format("Player %s died at %d,%d,%d in dimension %s, but had empty pockets.", playerName, x, y, z, world.provider.getDimensionName());
			DeathBox.getProxy().getLogger().info(message);
		} else {
			final String message = String.format("Player %s died at %d,%d,%d in dimension %s, saving inventory.", playerName, x, y, z, world.provider.getDimensionName());
			DeathBox.getProxy().getLogger().info(message);

			boolean found = false;
			int bx = -1;
			int by = -1;
			int bz = -1;

			for (int delta = 0; !found && delta < 16; delta++) {
				for (int cx = x - delta; !found && cx <= x + delta; cx++) {
					for (int cy = y - delta; !found && -1 < cy && 256 > cy && cy <= y + delta; cy++) {
						for (int cz = z - delta; !found && cz <= z + delta; cz++) {
							final Validity validity = LivingDeathEventHandler.isPositionValid(world, cx, cy, cz);

							if (validity == Validity.VALID) {
								x = cx;
								y = cy;
								z = cz;

								found = true;
							} else if (by == -1 && validity == Validity.ALTERNATE) {
								bx = cx;
								by = cy;
								bz = cz;
							}
						}
					}
				}
			}

			if (!found && by > -1) {
				x = bx;
				y = by;
				z = bz;
			}

			world.setBlock(x, y, z, CommonProxy.BLOCK);
			((DeathBoxTileEntity) world.getTileEntity(x, y, z)).store(player, inventories);
		}
	}
}
