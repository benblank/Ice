package com.five35.minecraft.deathbox.client;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import com.five35.minecraft.deathbox.CommonProxy;
import com.five35.minecraft.deathbox.DeathBoxTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SkinManager;

public class ClientProxy extends CommonProxy {
	@Override
	public void init(final FMLInitializationEvent event) {
		super.init(event);

		final SkinManager skinManager = Minecraft.getMinecraft().getSkinManager();

		ClientRegistry.bindTileEntitySpecialRenderer(DeathBoxTileEntity.class, new DeathBoxRenderer(skinManager));
	}
}
