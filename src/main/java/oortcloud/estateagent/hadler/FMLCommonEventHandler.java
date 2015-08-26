package oortcloud.estateagent.hadler;

import java.util.ArrayList;

import oortcloud.estateagent.chunk.ChunkCoordIntPairWithDimension;
import oortcloud.estateagent.chunk.ChunkManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

public class FMLCommonEventHandler {

	private int tick = 0;
	
	@SubscribeEvent
	 public void onServerTick(TickEvent.ServerTickEvent event) {
		
		if (tick == 0) {
			for (ArrayList<ChunkCoordIntPairWithDimension> list : ChunkManager.list.values()) {
				for (ChunkCoordIntPairWithDimension i : list) {
					ChunkManager.forceChunk(i);
				}
			}
			tick = 1200;
		}
		
		tick--;
		
	}
	
	@SubscribeEvent
	public void EventPlayerJoint(PlayerLoggedInEvent event) {
		
		if (!event.player.worldObj.isRemote) {
			if (!ChunkManager.list.containsKey(event.player.getCommandSenderName())) {
				ChunkManager.list.put(event.player.getCommandSenderName(), new ArrayList<ChunkCoordIntPairWithDimension>());
			}
		}
		
	}
	
}
