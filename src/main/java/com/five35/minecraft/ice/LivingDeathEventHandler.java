package com.five35.minecraft.ice;

import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LivingDeathEventHandler {
	private static BlockPos clampPosition(final BlockPos position) {
		final int x = Math.max(-30000000, Math.min(position.getX(), 30000000));
		final int y = Math.max(0, Math.min(position.getY(), 255));
		final int z = Math.max(-30000000, Math.min(position.getZ(), 30000000));

		return new BlockPos(x, y, z);
	}

	private static boolean createDeathMarker(final BlockPos requestedPosition, final EntityPlayer player, final Map<String, Map<Integer, ItemStack>> inventories) {
		final World world = player.worldObj;
		final BlockPos actualPosition = LivingDeathEventHandler.clampPosition(requestedPosition);

		if (world.setBlockState(actualPosition, CommonProxy.BLOCK.getDefaultState())) {
			((DeathMarkerTileEntity) world.getTileEntity(actualPosition)).store(player, inventories);
			Ice.getProxy().getInventoryManagerRegistry().clearInventories(player, inventories.keySet());

			final String message = String.format("Stored inventory for player %s at %s in dimension %s.", player.getCommandSenderEntity().getName(), actualPosition, world.provider.getDimensionName());
			Ice.getProxy().getLogger().info(message);

			return true;
		}

		return false;
	}

	@SubscribeEvent
	@SuppressWarnings("static-method")
	public void onLivingDeathEvent(final LivingDeathEvent event) {
		if (!(event.entity instanceof EntityPlayer)) {
			return;
		}

		final EntityPlayer player = (EntityPlayer) event.entity;
		final World world = player.worldObj;

		if (world.isRemote) {
			return;
		}

		final String playerName = player.getCommandSenderEntity().getName();
		final BlockPos position = player.getPosition().up();
		final Map<String, Map<Integer, ItemStack>> inventories = Ice.getProxy().getInventoryManagerRegistry().getAllInventories(player);

		if (inventories.isEmpty()) {
			final String message = String.format("Player %s died at %s in dimension %s, but had empty pockets.", playerName, position, world.provider.getDimensionName());
			Ice.getProxy().getLogger().info(message);

			return;
		}

		for (int delta = 0; delta < 4; delta++) {
			for (int dx = -delta; dx <= delta; dx++) {
				for (int dy = -delta; dy <= delta; dy++) {
					for (int dz = -delta; dz <= delta; dz++) {
						final BlockPos target = position.add(dx, dy, dz);

						if (world.getBlockState(target).getBlock().isReplaceable(world, target)) {
							if (LivingDeathEventHandler.createDeathMarker(target, player, inventories)) {
								return;
							}
						}
					}
				}
			}
		}

		LivingDeathEventHandler.createDeathMarker(position, player, inventories);
	}
}
