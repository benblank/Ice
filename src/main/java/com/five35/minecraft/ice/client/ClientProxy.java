package com.five35.minecraft.ice.client;

import com.five35.minecraft.ice.CommonProxy;
import com.five35.minecraft.ice.IceTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SkinManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {
	@Override
	public void init(final FMLInitializationEvent event) {
		super.init(event);

		final SkinManager skinManager = Minecraft.getMinecraft().getSkinManager();

		ClientRegistry.bindTileEntitySpecialRenderer(IceTileEntity.class, new IceRenderer(skinManager));
	}
}
