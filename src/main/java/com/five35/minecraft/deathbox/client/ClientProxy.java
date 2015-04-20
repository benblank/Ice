package com.five35.minecraft.deathbox.client;

import com.five35.minecraft.deathbox.CommonProxy;
import com.five35.minecraft.deathbox.DeathBoxTileEntity;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SkinManager;

public class ClientProxy extends CommonProxy {
	@Override
	public void init(final FMLInitializationEvent event) {
		super.init(event);

		final SkinManager skinManager = Minecraft.getMinecraft().func_152342_ad();

		ClientRegistry.bindTileEntitySpecialRenderer(DeathBoxTileEntity.class, new DeathBoxRenderer(skinManager));
	}
}
