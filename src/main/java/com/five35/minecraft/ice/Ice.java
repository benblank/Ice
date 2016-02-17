package com.five35.minecraft.ice;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "Ice", dependencies = "after:Baubles", updateJSON = "https://raw.githubusercontent.com/benblank/Ice/master/update.json")
public class Ice {
	@Instance
	private static Ice instance;

	@SidedProxy(clientSide = "com.five35.minecraft.ice.client.ClientProxy", serverSide = "com.five35.minecraft.ice.CommonProxy")
	private static CommonProxy proxy;

	public static CommonProxy getProxy() {
		return Ice.proxy;
	}

	@EventHandler
	@SuppressWarnings("static-method")
	public void init(final FMLInitializationEvent event) {
		Ice.proxy.init(event);
	}

	@EventHandler
	@SuppressWarnings("static-method")
	public void preInit(final FMLPreInitializationEvent event) {
		Ice.proxy.preInit(event);
	}
}
