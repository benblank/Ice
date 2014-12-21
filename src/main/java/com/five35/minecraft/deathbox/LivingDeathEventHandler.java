package com.five35.minecraft.deathbox;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class LivingDeathEventHandler {
	@SubscribeEvent
	public void onLivingDeathEvent(final LivingDeathEvent event) {
		DeathBox.getLogger().info("Something died.");

		if (!(event.entity instanceof EntityPlayer)) {
			return;
		}

		final EntityPlayer player = (EntityPlayer) event.entity;

		DeathBox.getLogger().info(String.format("Player %s died.", player.getCommandSenderName()));
	}
}
