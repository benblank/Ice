package com.five35.minecraft.deathbox;

import com.five35.minecraft.deathbox.inventorymanager.BaublesInventoryManager;
import com.five35.minecraft.deathbox.inventorymanager.InventoryManagerRegistry;
import com.five35.minecraft.deathbox.inventorymanager.VanillaInventoryManager;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;

@Mod(modid = "DeathBox", dependencies = "after:Baubles")
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

	public static InventoryManagerRegistry getInventoryManagerRegistry() {
		return DeathBox.getInstance().inventoryManagerRegistry;
	}

	static Logger getLogger() {
		return DeathBox.getInstance().logger;
	}

	private DeathBoxConfiguration config;
	private InventoryManagerRegistry inventoryManagerRegistry;
	private Logger logger;

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
		GameRegistry.registerBlock(DeathBox.BLOCK, "deathbox");
		GameRegistry.registerTileEntity(DeathBoxTileEntity.class, "deathbox");

		this.logger.debug("Reading config.");
		this.config = new DeathBoxConfiguration(event.getSuggestedConfigurationFile());
	}
}
