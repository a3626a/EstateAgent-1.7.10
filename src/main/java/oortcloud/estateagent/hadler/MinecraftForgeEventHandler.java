package oortcloud.estateagent.hadler;

import java.util.HashMap;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.event.world.WorldEvent;
import oortcloud.estateagent.EstateAgent;
import oortcloud.estateagent.chunk.ChunkManager;
import oortcloud.estateagent.chunk.ChunkSavedData;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MinecraftForgeEventHandler {

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		
	}
	
}

