package com.five35.minecraft.deathbox;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;

@Mod(modid = "DeathBox")
public class DeathBox {
	public static final DeathBoxBlock BLOCK = new DeathBoxBlock();

	@Instance
	private static DeathBox instance;

	static DeathBoxConfiguration getConfig() {
		return DeathBox.getInstance().config;
	}

	public static DeathBox getInstance() {
		return DeathBox.instance;
	}

	static Logger getLogger() {
		return DeathBox.getInstance().logger;
	}

	private DeathBoxConfiguration config;
	private Logger logger;

	@EventHandler
	public void init(@SuppressWarnings("unused") final FMLInitializationEvent event) {
		this.logger.debug("Registering event handler.");
		MinecraftForge.EVENT_BUS.register(new LivingDeathEventHandler());
	}

	@EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		this.logger = event.getModLog();

		this.logger.debug("Registering block.");
		GameRegistry.registerBlock(DeathBox.BLOCK, "deathbox");
		GameRegistry.registerTileEntity(DeathBoxTileEntity.class, "deathbox");

		this.logger.debug("Reading config.");
		this.config = new DeathBoxConfiguration(event.getSuggestedConfigurationFile());
	}
}
