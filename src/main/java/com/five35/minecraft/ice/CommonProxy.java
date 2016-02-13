package com.five35.minecraft.ice;

import com.five35.minecraft.ice.inventorymanager.BaublesInventoryManager;
import com.five35.minecraft.ice.inventorymanager.InventoryManagerRegistry;
import com.five35.minecraft.ice.inventorymanager.VanillaInventoryManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

public class CommonProxy {
	public static final DeathMarkerBlock BLOCK = new DeathMarkerBlock();

	private IceConfiguration config;

	private InventoryManagerRegistry inventoryManagerRegistry;

	private Logger logger;

	public IceConfiguration getConfig() {
		return this.config;
	}

	public InventoryManagerRegistry getInventoryManagerRegistry() {
		return this.inventoryManagerRegistry;
	}

	public Logger getLogger() {
		return this.logger;
	}

	@EventHandler
	public void init(@SuppressWarnings("unused") final FMLInitializationEvent event) {
		this.logger.debug("Registering event handler.");
		MinecraftForge.EVENT_BUS.register(new LivingDeathEventHandler());
	}

	@EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		this.logger = event.getModLog();

		this.logger.debug("Registering inventory managers.");
		this.inventoryManagerRegistry = new InventoryManagerRegistry(this.logger);
		this.inventoryManagerRegistry.register(new VanillaInventoryManager(false));
		this.inventoryManagerRegistry.register(new VanillaInventoryManager(true));

		if (Loader.isModLoaded("Baubles")) {
			this.logger.info("Integrating with Baubles.");
			this.inventoryManagerRegistry.register(new BaublesInventoryManager());
		}

		this.logger.debug("Registering block.");
		GameRegistry.registerBlock(CommonProxy.BLOCK, "deathmarker");
		GameRegistry.registerTileEntity(DeathMarkerTileEntity.class, "deathmarker");

		this.logger.debug("Reading config.");
		this.config = new IceConfiguration(event.getSuggestedConfigurationFile());
	}
}
