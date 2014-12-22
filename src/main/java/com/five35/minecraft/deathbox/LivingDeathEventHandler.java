package com.five35.minecraft.deathbox;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
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

		final int x = (int) player.posX;
		final int y = (int) player.posY;
		final int z = (int) player.posZ;

		final String message = String.format("Player %s died at %d,%d,%d in dimension %s, saving inventory.", playerName, x, y, z, world.provider.getDimensionName());
		DeathBox.getLogger().info(message);

		if (!world.isAirBlock(x, y, z)) {
			// TODO: find appropriate position for death box
		}

		world.setBlock(x, y, z, DeathBox.BLOCK);
		((DeathBoxTileEntity) world.getTileEntity(x, y, z)).store(player);
	}
}
