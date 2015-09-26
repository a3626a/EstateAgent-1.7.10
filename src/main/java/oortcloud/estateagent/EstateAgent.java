package oortcloud.estateagent;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import oortcloud.estateagent.chunk.ChunkLoadingCallback;
import oortcloud.estateagent.chunk.ChunkManager;
import oortcloud.estateagent.chunk.ChunkSavedData;
import oortcloud.estateagent.command.CommandLandBook;
import oortcloud.estateagent.core.proxy.CommonProxy;
import oortcloud.estateagent.handler.FMLCommonEventHandler;
import oortcloud.estateagent.handler.MinecraftForgeEventHandler;
import oortcloud.estateagent.items.ModItems;
import oortcloud.estateagent.lib.References;
import oortcloud.network.HandlerGeneralClient;
import oortcloud.network.HandlerGeneralServer;
import oortcloud.network.PacketGeneralClient;
import oortcloud.network.PacketGeneralServer;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = References.MODID, name = References.MODNAME, version = References.VERSION)
public class EstateAgent {

	@SidedProxy(clientSide = References.CLIENTPROXYLOCATION, serverSide = References.COMMONPROXYLOCATION)
	public static CommonProxy proxy;

	@Mod.Instance
	public static EstateAgent instance;

	public static SimpleNetworkWrapper simpleChannel;

	public static Logger logger;
	
	public static ChunkSavedData chunkSaved;
	
	public static CreativeTabs tab = new CreativeTabs("tabEstateAgent") {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return ModItems.landbook;
		}
	};
	
	@Mod.EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		ModItems.init();
	}

	@Mod.EventHandler
	public static void Init(FMLInitializationEvent event) {
		ForgeChunkManager.setForcedChunkLoadingCallback(EstateAgent.instance, new ChunkLoadingCallback());

		simpleChannel = NetworkRegistry.INSTANCE.newSimpleChannel(References.MODNAME);
		simpleChannel.registerMessage(HandlerGeneralServer.class, PacketGeneralServer.class, 1, Side.SERVER);
		simpleChannel.registerMessage(HandlerGeneralClient.class, PacketGeneralClient.class, 2, Side.CLIENT);
	}

	@Mod.EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new MinecraftForgeEventHandler());
		FMLCommonHandler.instance().bus().register(new FMLCommonEventHandler());
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		ChunkManager.getInstance().requestTicket();
		
		event.registerServerCommand(new CommandLandBook());

		MapStorage storage = MinecraftServer.getServer().worldServers[0].perWorldStorage;
		chunkSaved = (ChunkSavedData) storage.loadData(ChunkSavedData.class, ChunkSavedData.key);
		if (chunkSaved == null) {
			chunkSaved = new ChunkSavedData();
			storage.setData(ChunkSavedData.key, chunkSaved);
		}
	}
	
	@EventHandler
	public void serverUnload(FMLServerStoppingEvent event) {
		ChunkManager.getInstance().init();
	}

}
