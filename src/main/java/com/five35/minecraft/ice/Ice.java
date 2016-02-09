package com.five35.minecraft.deathbox;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "DeathBox", dependencies = "after:Baubles")
public class DeathBox {
	@Instance
	private static DeathBox instance;

	@SidedProxy(clientSide = "com.five35.minecraft.deathbox.client.ClientProxy", serverSide = "com.five35.minecraft.deathbox.CommonProxy")
	private static CommonProxy proxy;

	public static CommonProxy getProxy() {
		return DeathBox.proxy;
	}

	@EventHandler
	@SuppressWarnings("static-method")
	public void init(final FMLInitializationEvent event) {
		DeathBox.proxy.init(event);
	}

	@EventHandler
	@SuppressWarnings("static-method")
	public void preInit(final FMLPreInitializationEvent event) {
		DeathBox.proxy.preInit(event);
	}
}
