package com.five35.minecraft.deathbox;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = "DeathBox")
public class DeathBox {
	@Instance
	static DeathBox INSTANCE;

	DeathBoxConfiguration config;
	Logger logger;

	@EventHandler
	public void init(final FMLInitializationEvent event) {
		this.logger.debug("Registering event handler.");
		// TODO: register event handler
	}

	@EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		this.logger = event.getModLog();

		this.logger.debug("Registering block.");
		// TODO: register block

		this.logger.debug("Reading config.");
		this.config = new DeathBoxConfiguration(event.getSuggestedConfigurationFile());
	}
}
