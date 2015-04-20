package com.five35.minecraft.deathbox;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class LivingDeathEventHandler {
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

		final int x = (int) player.posX;
		final int y = (int) player.posY;
		final int z = (int) player.posZ;

		final Map<String, Map<Integer, ItemStack>> inventories = DeathBox.getProxy().getInventoryManagerRegistry().extractAllInventories(player);

		if (inventories.isEmpty()) {
			final String message = String.format("Player %s died at %d,%d,%d in dimension %s, but had empty pockets.", playerName, x, y, z, world.provider.getDimensionName());
			DeathBox.getProxy().getLogger().info(message);
		} else {
			final String message = String.format("Player %s died at %d,%d,%d in dimension %s, saving inventory.", playerName, x, y, z, world.provider.getDimensionName());
			DeathBox.getProxy().getLogger().info(message);

			if (!world.isAirBlock(x, y, z)) {
				// TODO: find appropriate position for death box
			}

			world.setBlock(x, y, z, CommonProxy.BLOCK);
			((DeathBoxTileEntity) world.getTileEntity(x, y, z)).store(player, inventories);
		}
	}
}
