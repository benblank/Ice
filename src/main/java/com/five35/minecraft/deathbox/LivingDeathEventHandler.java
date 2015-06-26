package com.five35.minecraft.deathbox;

import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LivingDeathEventHandler {
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
		final BlockPos position = player.getPosition().offsetUp();
		final Map<String, Map<Integer, ItemStack>> inventories = DeathBox.getProxy().getInventoryManagerRegistry().extractAllInventories(player);

		if (inventories.isEmpty()) {
			final String message = String.format("Player %s died at %s in dimension %s, but had empty pockets.", playerName, position, world.provider.getDimensionName());
			DeathBox.getProxy().getLogger().info(message);

			return;
		}

		final String message = String.format("Player %s died at %s in dimension %s, saving inventory.", playerName, position, world.provider.getDimensionName());
		DeathBox.getProxy().getLogger().info(message);

		for (int delta = 0; delta < 16; delta++) {
			for (int dx = -delta; dx < delta; dx++) {
				for (int dy = -delta; dy < delta; dy++) {
					for (int dz = -delta; dz < delta; dz++) {
						final BlockPos target = position.add(dx, dy, dz);

						if (world.getBlockState(target).getBlock().isReplaceable(world, target)) {
							if (world.setBlockState(target, CommonProxy.BLOCK.getDefaultState())) {
								((DeathBoxTileEntity) world.getTileEntity(target)).store(player, inventories);

								return;
							}
						}
					}
				}
			}
		}

		// If no replaceable block was found nearby, just replace whatever the player was standing in.
		if (world.setBlockState(position, CommonProxy.BLOCK.getDefaultState())) {
			((DeathBoxTileEntity) world.getTileEntity(position)).store(player, inventories);

			return;
		}
	}
}
